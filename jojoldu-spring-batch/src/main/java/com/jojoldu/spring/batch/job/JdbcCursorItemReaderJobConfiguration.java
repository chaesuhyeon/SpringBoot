package com.jojoldu.spring.batch.job;

import com.jojoldu.spring.batch.domain.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

/**
 * CursorItemReader > JdbcCursorItemReader 구현
 * <주의> : Jpa는 CursorItemReader가 없음
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcCursorItemReaderJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource; // DataSource DI

    private static final int chunkSize = 10;

    @Bean
    public Job jdbcCursorItemReaderJob() {
        return jobBuilderFactory.get("jdbcCursorItemReaderJob")
                .start(jdbcCursorItemReaderStep())
                .build();
    }

    /**
     *  chunk : <Pay, Pay> -> 첫 번째 Pay는 Reader에서 반환할 타입, 두 번째 Pay는 Writer에 파라미터로 넘어갈 타입
     */
    @Bean
    public Step jdbcCursorItemReaderStep() {
        return stepBuilderFactory.get("jdbcCursorItemReaderStep")
                .<Pay, Pay>chunk(chunkSize)
                .reader(jdbcCursorItemReader())
                .writer(jdbcCursorItemWriter())
                .build();
    }

    /**
     *  fetchSize : DB에서 한번에 가져올 데이터 양
     *      Paging과 다른점은 Paging은 실제 쿼리를 limit, offset을 이용해서 분할 처리하는 반면,
     *      Cursor에서는 쿼리를 분할 처리 없이 실행하나 내부적으로 가져오는 데이터는 FetchSize만큼 가져와 read()를 통해서 하나씩 가져옴
     *  dataSource : DB에 접근하기 위해 사용할 Datasource 객체를 할당
     *  rowMapper : 쿼리 결과를 Java 인스턴스로 매핑하기 위한 Mapper
     *  sql : Reader로 사용할 쿼리문 작성
     *  name : reader의 이름을 지정 , Bean의 이름이 아니며 ExecutionContext에 저장될 이름
     *  itemReader의 가장 큰 장점은 데이터를 Streaming 할 수 있다는 점
     */
    @Bean
    public JdbcCursorItemReader<Pay> jdbcCursorItemReader() {
        return new JdbcCursorItemReaderBuilder<Pay>()
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Pay.class))
                .sql("SELECT id, amount, tx_name, tx_date_time FROM pay")
                .name("jdbcCursorItemReader")
                .build();
    }

    /**
     * reader는 Tasklet이 아니기 때문에 reader 만으로는 수행될 수 없고, writer 필요
     * processor는 필수가 아님 reader -> processor -> writer가 아니여도 됨. reader -> writer 가능
     */
    private ItemWriter<Pay> jdbcCursorItemWriter() {
        return list -> {
            for (Pay pay: list) {
                log.info("Current Pay={}", pay);
            }
        };
    }

    /**
     * [배치 실행 결과]
     * Current Pay=Pay(id=1, amount=1000, txName=trade1, txDateTime=2018-09-10T00:00)
     * Current Pay=Pay(id=2, amount=2000, txName=trade2, txDateTime=2018-09-10T00:00)
     * Current Pay=Pay(id=3, amount=3000, txName=trade3, txDateTime=2018-09-10T00:00)
     * Current Pay=Pay(id=4, amount=4000, txName=trade4, txDateTime=2018-09-10T00:00)
     * 
     * 등록한 데이터가 잘 조회되어 Writer에 명시한대로 데이터를 출력
     */

}
