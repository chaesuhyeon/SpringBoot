package io.spring.batch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

/**
 * 모든 잡은 생명주기를 갖는데, 스프링 배치는 생명주기의 여러 시점에 로직을 추가할 수 있음
 * 잡 리스너를 작성하는 두가지 방법이 있는데 첫 번째는 JobExecutionListener 인터페이스를 직접 구현하는 방법
 *                                    두 번째는 JobExecutionListener 인터페이스를 직접 구현하지 않고 어노테이션 활용(@BeforeJob/@AfterJob)
 * JobExecutionListener : 잡의 시작이나 종료를 다른 시스템에 알리는 메시지 큐 메시지를 생성하는 역할
 * beforeJob : JobExecution을 전달받아 실행되며, 잡이 실행되기 전에 실행
 * afterJob : JobExecution을 전달받아 실행되며, 잡이 실행된 후에 실행, 이 메서드는 잡의 완료 상태에 관계 없이 호출 되기 때문에 잡의 종료 상태에 따라 어떤 일을 수행할지 결정할 수 있음
 */
public class JobLoggerListener /*implements JobExecutionListener*/ {

    private static String START_MESSAGE = "%s is beginning execution";
    private static String END_MESSAGE = "%s has completed with the status %s";

//    @Override
    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        System.out.println(String.format(START_MESSAGE, jobExecution.getJobInstance().getJobName()));
    }

//    @Override
    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        System.out.println(String.format(END_MESSAGE, jobExecution.getJobInstance().getJobName(), jobExecution.getStatus()));
    }
}
