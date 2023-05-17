package io.spring.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@EnableBatchProcessing
@Configuration
public class BatchConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job(){
        return this.jobBuilderFactory.get("job")
                .start(step1())
                .build();
    }

    @Bean
    public Step step1(){
        return this.stepBuilderFactory.get("step1")
                .tasklet(helloWorldTasklet())
                .build();
    }

    @Bean
    public Tasklet helloWorldTasklet(){
        return new HelloWorld();
    }

    @Bean
    public StepExecutionListener promotionListener(){
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();

        listener.setKeys(new String[] {"name"});
        return listener;
    }

    public static class HelloWorld implements Tasklet {

        private static final String HELLO_WORLD = "Hello, %s";

        @Override
        public RepeatStatus execute(StepContribution step, ChunkContext context) throws Exception {

            String name = (String) context.getStepContext()
                    .getJobParameters()
                    .get("name");

            ExecutionContext jobContext = context.getStepContext()
                    .getStepExecution()
                    .getExecutionContext();

            jobContext.put("name" , name);

            System.out.println(String.format(HELLO_WORLD,name));
            return RepeatStatus.FINISHED;
        }
    }
}
