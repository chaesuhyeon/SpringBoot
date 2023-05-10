package com.jojoldu.spring.batch.job;

import com.jojoldu.spring.batch.domain.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

/**
 * JpaPagingItemReader
 * 현재 Querydsl, Jooq 등을 통한 ItemReader 구현체는 공식 지원 x -> CustomItemReader 구현체를 만들어야 함 -> https://docs.spring.io/spring-batch/docs/4.0.x/reference/html/readersAndWriters.html#customReader [공식 문서]
 * JPA에는 Cursor 기반 Database 접근을 지원하지 않음(Paging 기반만 지원)
 */
@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
public class JpaPagingItemReaderJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private int chunkSize = 10;

    @Bean
    public Job jpaPagingItemReaderJob() {
        return jobBuilderFactory.get("jpaPagingItemReaderJob")
                .start(jpaPagingItemReaderStep())
                .build();
    }

    @Bean
    public Step jpaPagingItemReaderStep() {
        return stepBuilderFactory.get("jpaPagingItemReaderStep")
                .<Pay, Pay>chunk(chunkSize)
                .reader(jpaPagingItemReader())
                .writer(jpaPagingItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Pay> jpaPagingItemReader() {
        return new JpaPagingItemReaderBuilder<Pay>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory) // EntityManagerFactory를 지정하는 것 외에 JdbcPagingItemReader와 크게 다른 점은 없음
                .pageSize(chunkSize)
                .queryString("SELECT p FROM Pay p WHERE amount >= 2000")
                .build();
    }

    private ItemWriter<Pay> jpaPagingItemWriter() {
        return list -> {
            for (Pay pay: list) {
                log.info("JPA Current Pay={}", pay);
            }
        };
    }
}

/** [배치 실행 결과]
 * JPA Current Pay=Pay(id=2, amount=2000, txName=trade2, txDateTime=2018-09-10T00:00)
 * JPA Current Pay=Pay(id=3, amount=3000, txName=trade3, txDateTime=2018-09-10T00:00)
 * JPA Current Pay=Pay(id=4, amount=4000, txName=trade4, txDateTime=2018-09-10T00:00)
 */