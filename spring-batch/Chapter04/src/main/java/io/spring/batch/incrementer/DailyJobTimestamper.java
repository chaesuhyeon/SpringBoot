package io.spring.batch.incrementer;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.util.Date;

/**
 * Incrementer : 같은 파라미터로 잡을 실행할 수 있도록 함(Incrementer 설정이 없으면 같은 파라미터로 잡 실행시 오류 발생)
 * DailyJobTimestamper : 잡 실행 시마다 타임스탬프를 파라미터로 사용할 수 있음 (JobParametersIncrementer 커스텀 구현)
 */
public class DailyJobTimestamper implements JobParametersIncrementer {

    @Override
    public JobParameters getNext(JobParameters parameters) {
        return new JobParametersBuilder(parameters)
                .addDate("currentDate" , new Date()) // currentDate : 파라미터 이름
                .toJobParameters();
    }
}
