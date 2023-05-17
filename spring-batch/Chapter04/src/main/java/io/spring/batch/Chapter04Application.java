package io.spring.batch;

import io.spring.batch.incrementer.DailyJobTimestamper;
import io.spring.batch.listener.JobLoggerListener;
import io.spring.batch.validator.ParameterValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobListenerFactoryBean;
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

import java.util.Arrays;

@RequiredArgsConstructor
@EnableBatchProcessing // 애플리케이션 내에서 한번만 적용, 배치 잡 수행에 필요한 인프라스트럭처를 제공
@SpringBootApplication
public class Chapter04Application {

	private final JobBuilderFactory jobBuilderFactory;

	private final StepBuilderFactory stepBuilderFactory;

	/**
	 *  JobBuilder의 메서드는 하나의 JobParameterValidator 인스턴스만 지정하게 되어 있음
	 *  스프링 배치는 CompositeJobParametersValidator를 제공한다.
	 *  CompositeJobParametersValidator를 사용하여 두 개 이상의 validator를 묶어서 사용할 수 있다.
	 *
	 *
	 */
	@Bean
	public CompositeJobParametersValidator validator(){
		CompositeJobParametersValidator validator = new CompositeJobParametersValidator();

		/*======== [DefaultJobParametersValidator]  ========*/
		/* DefaultJobParametersValidator는 기본적으로 제공되는 파라미터 유효성 validator이다. */
		/* requiredKeys와 optionalKeys 라는 선택전인 의존성 존재*/
		/* 둘 다 문자열 배열로써 파라미터 이름 목록이 담김 */
		/* fileName은 필수키, name은 옵션키 */
		/* 옵션 키가 구성되어 있지 않고 필수키만 구성돼 있다면, 필수키를 전달하기만 하면 그 외 어떤 키의 조합을 전달 하더라도 유효성 검증을 통과 */
		/* 필수키, 옵션키가 모두 구성되어 있을 경우 그 외 다른 파라미터 변수가 전달되면 유효성 검증을 실패 */
		/* DefaultJobParametersValidator는 파라미터 존재 여부를 제외한 다른 유효성 검증을 수행하지 않음 -->  더 강력한 유효성 검증이 필요하다면  JobParametersValidator를 용도에 맞게 직접 구현해야 함*/
		/* run.id : JobParametersIncrementer를 사용하기 위한 추가 파라미터 */

//		DefaultJobParametersValidator defaultJobParametersValidator = new DefaultJobParametersValidator(new String[]{"fileName"}, new String[]{"name" , "run.id"}); // RunIdIncrementer 사용
		DefaultJobParametersValidator defaultJobParametersValidator = new DefaultJobParametersValidator(new String[]{"fileName"}, new String[]{"name" , "currentDate"}); // DailyJobTimestamper 사용
		defaultJobParametersValidator.afterPropertiesSet();

		validator.setValidators(Arrays.asList(new ParameterValidator(), defaultJobParametersValidator));
		return  validator;
	}

	@Bean
	public Job basicJob(){
		return this.jobBuilderFactory.get("basicJob")
				.start(basicStep1())
				.validator(validator())
				// Job은 동일한 파라미터로 수행했을 때 예외가 발생 -> JobParametersIncrementer: 사용해서 Job에서 사용할 파라미터를 고유하게 생성할 수 있도로록 스프링 배치가 제공하는 인터페이스
				// 기본적으로 파라미터 이름이 run.id인 long 타입 파라미터의 값을 증가시킴
//				.incrementer(new RunIdIncrementer())  // run.id 파라미터를 실행할 때 마다 1씩 증가시킨다.
				.incrementer(new DailyJobTimestamper())  // 잡 실행 시마다 타임스탬프를 파라미터(currentDate)로 사용
				.listener(JobListenerFactoryBean.getListener(new JobLoggerListener())) // [@BeforeJob/@AfterJob 어노테이션 사용]
//				.listener(new JobLoggerListener()) // [JobExecutionListener 인터페이스 직접 구현]잡 리스터 등록(잡 실행 전에 beforeJob, 잡 내의 처리가 완료되면 afterJob 메서드 호출)
				.build();
	}

	@Bean
	public Step basicStep1(){
		return this.stepBuilderFactory.get("step1")
				.tasklet(basicHelloWorldTasklet(null,null)).build();
	}

	@StepScope // 잡 파라미터를 늦은 바인딩하게 해줌 --> 스텝의 실행범위(StepScope) , 잡의 실행범위(JobScope)에 들어갈 때 까지 빈의 생성을 지연시키는 것 --> 이렇게 함으로써 명령행 또는 다른 소스에서 받아들인 잡 파라미터를 빈 생성 시점에 주입할 수 있음
	@Bean
	public Tasklet basicHelloWorldTasklet(
			@Value("#{jobParameters['name']}") String name,
			@Value("#{jobParameters['fileName']}") String fileName

	){
		return (stepContribution, chunkContext) -> {
			System.out.println(String.format("Hello, %s!" , name));
			System.out.println(String.format("fileName = %s", fileName));

			return RepeatStatus.FINISHED;
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(Chapter04Application.class, args);
	}

}
