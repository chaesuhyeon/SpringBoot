package io.spring.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@EnableBatchProcessing // 애플리케이션 내에서 한번만 적용, 배치 잡 수행에 필요한 인프라스트럭처를 제공
@SpringBootApplication
public class Chapter04Application {

	private final JobBuilderFactory jobBuilderFactory;

	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job job(){
		return this.jobBuilderFactory.get("basicJob")
				.start(step1())
				.build();
	}

	@Bean
	public Step step1(){
		return this.stepBuilderFactory.get("step1")
				.tasklet(helloWorldTasklet(null)).build();
	}

	@StepScope // 잡 파라미터를 늦은 바인딩하게 해줌 --> 스텝의 실행범위(StepScope) , 잡의 실행범위(JobScope)에 들어갈 때 까지 빈의 생성을 지연시키는 것 --> 이렇게 함으로써 명령행 또는 다른 소스에서 받아들인 잡 파라미터를 빈 생성 시점에 주입할 수 있음
	@Bean
	public Tasklet helloWorldTasklet(
			@Value("#{jobParameters['name']}") String name){
		return (stepContribution, chunkContext) -> {
			System.out.println(String.format("Hello, %s!" , name));

			return RepeatStatus.FINISHED;
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(Chapter04Application.class, args);
	}

}
