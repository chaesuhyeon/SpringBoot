package com.jojoldu.spring.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StepNextConditionalJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * step1 실패 시나리오 : step1 -> step3
     * step1 성공 시나리오 : step1 -> step2 -> step3
     */
    @Bean
    public Job stepNextConditionalJob(){
        return jobBuilderFactory.get("stepNextConditionalJob")
                .start(conditionalJobStp1())
                    .on("FAILED") // FAILED일 경우 --> 캐치할 ExitStatus 지정
                    .to(conditionalJobStep3()) // step3으로 이동한다. --> 다음에 이동할 Step 지정
                    .on("*") // step3의 결과에 상관 없이
                    .end() // step3으로 이동하면 flow가 종료된다.
                .from(conditionalJobStp1()) // step1으로부터
                    .on("*") // FAILED외에 모든 경우
                    .to(conditionalJobStep2()) // step2로 이동한다.
                    .next(conditionalJobStep3()) // step2가 정상종료 되면 step3으로 이동한다.
                    .on("*") // step3의 결과에 상관 없이
                    .end() // step3으로 이동하면 flow가 종료된다.
                .end() // Job 종료
                .build();
    }

    @Bean
    public Step conditionalJobStp1(){
        return stepBuilderFactory.get("step1")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>> This is stepNextConditionalJob Step1");

                    /**
                     *  ExisStatus를 FAILED로 지정한다.
                     *  해상 status를 보고 flow가 진행된다.
                     */
                    contribution.setExitStatus(ExitStatus.FAILED);
                    return  RepeatStatus.FINISHED;
                }))
                .build();
    }


    @Bean
    public Step conditionalJobStep2(){
        return stepBuilderFactory.get("conditionalJobStep2")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>> This is stepNextConditionalJob Step2");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step conditionalJobStep3(){
        return stepBuilderFactory.get("conditionalJobStep3")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>>>> This is stepNextConditionalJob Step3");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

}
