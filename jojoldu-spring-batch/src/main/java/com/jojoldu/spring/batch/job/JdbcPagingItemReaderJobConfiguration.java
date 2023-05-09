package com.jojoldu.spring.batch.job;

import com.jojoldu.spring.batch.domain.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * PagingItemReader > JdbcPagingItemReader 구현
 * Database Cursor를 사용하는 대신 여러 쿼리를 실행하여 각 쿼리가 결과의 일부를 가져오는 방법 -> Paging이라고 함
 * Spring Batch에서는 offset과 limit을 PageSize에 맞게 자동으로 생성해준다.
 * 다만, 각 쿼리는 개별적으로 실행한 다는 점을 유의 해야한다.
 * 각 페이지마다 새로운 쿼리를 실행하므로 페이징시 결과를 정렬하는 것이 중요 함 -> 데이터 결과의 순서가 보장될 수 있도록 order by가 권장 됨
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcPagingItemReaderJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource; // DataSource DI

    private static final int chunkSize = 10;

    @Bean
    public Job jdbcPagingItemReaderJob() throws Exception {
        return jobBuilderFactory.get("jdbcPagingItemReaderJob")
                .start(jdbcPagingItemReaderStep())
                .build();
    }

    @Bean
    public Step jdbcPagingItemReaderStep() throws Exception {
        return stepBuilderFactory.get("jdbcPagingItemReaderStep")
                .<Pay, Pay>chunk(chunkSize)
                .reader(jdbcPagingItemReader())
                .writer(jdbcPagingItemWriter())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<Pay> jdbcPagingItemReader() throws Exception {
        Map<String, Object> parameterValues = new HashMap<>(); // 쿼리에 대한 매개변수 값의 Map을 지정
        parameterValues.put("amount", 2000); //queryProvider.setWhereClause("where amount >= :amount") 의 매개변수로 사용

        return new JdbcPagingItemReaderBuilder<Pay>()
                .pageSize(chunkSize)
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Pay.class))
                .queryProvider(createQueryProvider()) // JdbcCursorItemReader와 다른 점 -> JdbcCursorItemReader을 사용할 때는 단순히 String타입으로 쿼리를 생성 / PagingItemReader에서는 PagingQueryProvider를 통해 쿼리를 생성
                .parameterValues(parameterValues)
                .name("jdbcPagingItemReader")
                .build();
    }

    private ItemWriter<Pay> jdbcPagingItemWriter() {
        return list -> {
            for (Pay pay: list) {
                log.info("Paging >> Current Pay={}", pay);
            }
        };
    }

    @Bean
    public PagingQueryProvider createQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource); // Database에 맞는 PagingQueryProvider를 선택하기 위해
        queryProvider.setSelectClause("id, amount, tx_name, tx_date_time");
        queryProvider.setFromClause("from pay");
        queryProvider.setWhereClause("where amount >= :amount");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("id", Order.ASCENDING);

        queryProvider.setSortKeys(sortKeys);

        return queryProvider.getObject();
    }

    /**
     * [배치 실행 결과] / amount가 2000이상 인 것만 조회
     *
     * SQL used for reading first page: [SELECT id, amount, tx_name, tx_date_time FROM pay WHERE amount >= :amount ORDER BY id ASC LIMIT 10]
     * LIMIT을 위에서 지정 해준적이 없는데 10인 이유 :  JdbcPagingItemReader에서 선언된 pageSize (Cursor에서는 fetchSize) 에 맞게 자동으로 쿼리에 추가해줌
     *
     * Paging >> Current Pay=Pay(id=2, amount=2000, txName=trade2, txDateTime=2018-09-10T00:00)
     * Paging >> Current Pay=Pay(id=3, amount=3000, txName=trade3, txDateTime=2018-09-10T00:00)
     * Paging >> Current Pay=Pay(id=4, amount=4000, txName=trade4, txDateTime=2018-09-10T00:00)
     */
}
