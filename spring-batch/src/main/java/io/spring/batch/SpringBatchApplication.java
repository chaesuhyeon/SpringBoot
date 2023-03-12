package io.spring.batch;

import io.spring.batch.common.listener.JobLoggerListener;
import io.spring.batch.common.listener.JobLoggerListener1;
import io.spring.batch.common.timestamper.DailyJobTimeStamper;
import io.spring.batch.common.validator.ParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.listener.JobListenerFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@EnableBatchProcessing // 배치 잡 수행에 필요한 인프라트럭처 제공
@SpringBootApplication
public class SpringBatchApplication {

/*
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    */
/**
     * 필수 파라미터 , 선택적 파라미터 검증기
     *//*

*/
/*    @Bean
    public JobParametersValidator validator(){
        DefaultJobParametersValidator validator = new DefaultJobParametersValidator(); // 파라미터 존재 여부를 제외한 다른 유효성 검증을 수행하지 않음. 더 강력한 유효성 검증이 필요하다면 JobParametersValidator를 용도에 맞게 직접 구현해야함

        validator.setRequiredKeys(new String[]{"fileName"}); // 필수 파라미터
        validator.setOptionalKeys(new String[]{"name"}); // 필수가 아닌 옵션 파마리터
        return validator;
    }*//*


*/
/*    @Bean
    public CompositeJobParametersValidator validator(){
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator(); // 1개 이상의 검증기를 등록할 수 있도록 해줌
        DefaultJobParametersValidator defaultJobParametersValidator = new DefaultJobParametersValidator(new String[]{"fileName"}, new String[]{"name", "run.id"}); //new DefaultJobParametersValidator(필수 파라미터, 옵션 파라미터 ); / run.id -> 동일한 job을 여러번 실행하기 위해 설정

        defaultJobParametersValidator.afterPropertiesSet();
        validator.setValidators(Arrays.asList(new ParameterValidator() , defaultJobParametersValidator)); // 2개의 검증기 등록
        return validator;
    }*//*


    @Bean
    public CompositeJobParametersValidator validator(){
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator(); // 1개 이상의 검증기를 등록할 수 있도록 해줌
        DefaultJobParametersValidator defaultJobParametersValidator = new DefaultJobParametersValidator(new String[]{"fileName"}, new String[]{"name", "currentDate"}); //new DefaultJobParametersValidator(필수 파라미터, 옵션 파라미터 );

        defaultJobParametersValidator.afterPropertiesSet();
        validator.setValidators(Arrays.asList(new ParameterValidator() , defaultJobParametersValidator)); // 2개의 검증기 등록
        return validator;
    }
    @Bean
    public Job job(){
        return this.jobBuilderFactory.get("basicJob")
                .start(step1())
                .validator(validator()) // 유효성 검증기
//                .incrementer(new RunIdIncrementer()) // 같은 job을 여러번 수행하기 위해 설정
//                .listener(new JobLoggerListener1())
                .incrementer(new DailyJobTimeStamper())
                .listener(JobListenerFactoryBean.getListener(new JobLoggerListener()))
                .build();
    }

*/
/*    @Bean
    public Step step1(){
        return this.stepBuilderFactory.get("step1")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("Hello World!");
                    return RepeatStatus.FINISHED;
                })).build();
    }*//*


    @Bean
    public Step step1(){
        return this.stepBuilderFactory.get("step1")
                .tasklet(helloworldTasklet(null,null))
                .build();
    }



    @StepScope // 스텝의 실행범위나 잡의 실행범위에 들어갈 때 까지 빈 생성을 지연시키는 것 -> 명령행 또는 다른 소스에서 받아들인 잡 파라미터를 빈 생성 시점에 주입할 수 있음
    @Bean
    public Tasklet helloworldTasklet(@Value("#{jobParameters['name']}") String name,
                                     @Value("#{jobParameters['fileName']}") String fileName){
        return ((stepContribution, chunkContext) -> {
*/
/*            String name = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("name");*//*

            System.out.println(String.format("Hello,%s!", name));
            System.out.println(String.format("fileName = %s", fileName));
            return RepeatStatus.FINISHED;
        });
    }
*/

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
    }

}
