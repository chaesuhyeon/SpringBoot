package com.spring.batch.config;

import com.spring.batch.student.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * Spring Batch를 사용하여 CSV 파일에서 데이터를 읽고 각 줄을 'Student' 객체로 매핑하는 구성 클래스를 작성
 */
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    /**
     * [FlatFileItemReader]
     * 파일로부터 데이터를 한 줄씩 읽어서 처리하는 역할을 함
     * 다양한 형식의 파일을 읽을 수 있음
     */
    @Bean
    public FlatFileItemReader<Student> itemReader() {
        FlatFileItemReader<Student> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/students.csv")); // 리더가 읽을 파일의 경로를 설정
        itemReader.setName("csvReader"); // 리더에 이름 부여
        itemReader.setLinesToSkip(1); // 파일에서 특정 줄을 건너뛰게 할 수 있음 (첫 번째 줄을 건너뛰도록 설정)
        itemReader.setLineMapper(lineMapper()); // 읽은 각 줄을 특정 객체로 변환하는 역할
        return itemReader;
    }

    /**
     * [DefaultLineMapper]
     * 파일에서 읽은 각 줄을 특정 객체로 매핑하는데 사용
     *
     * [DelimitedLineTokenizer]
     * 주로 CSV 파일과 같은 구분자로 분리된 파일을 처리할 때 사용
     * 각 줄을 구분자를 기준으로 토큰화하여 필드값으로 분리
     */
    private LineMapper<Student> lineMapper() {
        DefaultLineMapper<Student> lineMapper = new DefaultLineMapper<>(); // 각 줄을 Student 객체로 매핑하는데 사용
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer(); // 각 줄을 토큰으로 분리하는데 사용
        lineTokenizer.setDelimiter(","); // 구분자를 콤마(,) 로 설정 (CSV 파일의 각 필드는 콤마로 구분됨)
        lineTokenizer.setStrict(false); // 엄격 모드를 비활성화 (줄이 예상된 필드 수와 정확히 일치하지 않더라도 오류를 발생시키지 않음)
        lineTokenizer.setNames("id", "firstName", "lastName", "age"); // CSV 파일의 각 필드 이름을 설정 (Student 객체의 필드와 매핑됨)

        BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Student.class); // 매핑할 대상 타입을 Student 클래스로 설정

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }
}
