package io.spring.batch.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

/**
 * 잡 파라미터의 유효성 검증기
 */
public class ParameterValidator implements JobParametersValidator {

    /**
     * 반환타입이 void이므로 JobParametersInvalidException이 발생하지 않는다면 유효성 검증을 통과했다고 판단
     * fileName 파라미터가 없거나 파라미터 값이 .csv로 끝나지 않으면 예외가 발생되며 잡이 실행되지 않음
     */
    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String fileName = parameters.getString("fileName");

        if(!StringUtils.hasText(fileName)){ // fileName이 없는 경우
            throw new JobParametersInvalidException("fileName parameter is missing");
            
        } else if(!StringUtils.endsWithIgnoreCase(fileName, "csv")){ // .csv로 끝나지 않는 경우
            throw new JobParametersInvalidException("fileName parameter does not use the csv file extension");
        }
    }
}
