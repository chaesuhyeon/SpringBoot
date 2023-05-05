package com.jojoldu.spring.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration // Spring Batch의 모든 Job은 @Configuration으로 등록해서 사용
public class SimpleJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job simpleJob() { // Job은 하나의 배치 작업 단위, Job안에는 여러개의 Step이 존재하고, Step안에 Tasklet 혹은 Reader & Processor & Writer 묶음이 존재
        return jobBuilderFactory.get("simpleJob") // simpleJob이라는 이름으로 Batch Job을 생성
                .start(simpleStep1(null))
                .next(simpleStep2(null))
                .build();
    }

    @Bean
    @JobScope // 파라미터 사용시 필수 어노테이션ㅔㅁ
    public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("simpleStep1") // simpleStep1이라는 이름으로 Batch Step을 생성
                .tasklet(((contribution, chunkContext) -> { // Step안에서 수행될 기능들을 명시 , tasklet은 Step안에서 단일로 수행될 커스텀한 기능들을 선언할 때 사용
                    log.info(">>>>> This is Step1"); // Batch가 수행되면 로그가 출력 되도록 함
                    log.info(">>>>> requestDate = {}" , requestDate);

//                    throw new IllegalArgumentException("step1에서 실패합니다");
                    return RepeatStatus.FINISHED;

                }))
                .build();
    }

    @Bean
    @JobScope // 파라미터 사용시 필수 어노테이션ㅔㅁ
    public Step simpleStep2(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("simpleStep2")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step2");
                    log.info(">>>>> requestDate = {}" , requestDate);
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }
}
