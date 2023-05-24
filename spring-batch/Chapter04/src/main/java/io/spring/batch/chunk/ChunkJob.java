package io.spring.batch.chunk;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class ChunkJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job chunkBasedJob(){
        return this.jobBuilderFactory.get("chunkBasedJob")
                .start(chunkStep())
                .build();
    }

    @Bean
    public Step chunkStep(){
        return this.stepBuilderFactory.get("chunkStep")
                .<String, String >chunk(completionPolicy())
                .reader(itemReader())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public ListItemReader<String> itemReader(){
        List<String> items = new ArrayList<>();

        for (int i = 0; i < 100000; i++) {
            items.add(UUID.randomUUID().toString());
        }

        return new ListItemReader<>(items);
    }
    
    @Bean
    public ItemWriter<String> itemWriter(){
        return items -> {
            for (String item : items) {
                System.out.println(">> current item = " + item);
            }
        };
    }

    /**
     * SimpleCompletionPolicy : 처리된 아이템 개수를 세는데, 이 개수가 미리 구성해둔 임계값에 도달하면 청크 완료로 표시
     * TimeoutTerminationPolicy : 타임아웃 값을 구성하면, 청크 내에서 처리 시간이 해당 시간이 넘을 때 안전하게 빠져나갈 수 있다. 안전하게 빠져나간다는 말은 해당 청크가 완료된 것으로 간주되고 모든 트랜잭션 처리가 정상적으로 계속됨을 의미
     * CompositeCompletionPolicy를 사용하면 청크 완료 여부를 결정하는 여러 정책을 함께 구성할 수 있다. 자신이 포함하고 있는 여러 정책 중 하나라도 청크 완료라고 판단되면 해당 청크가 완료된 것으로 표시한다.
     * 아래 코드는 청크 완료를 판단할 때, 아이템의 정상 커밋 개수 200개 및 3밀리초의 타임아웃을 이용하는 코드이다.
     * 
     */
    @Bean
    public CompletionPolicy completionPolicy(){
        CompositeCompletionPolicy policy = new CompositeCompletionPolicy();
        policy.setPolicies(
                new CompletionPolicy[]{
                        new TimeoutTerminationPolicy(3) , new SimpleCompletionPolicy(1000)});
        
        return policy;
    }
    
    
}
