package io.spring.batch.common.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class ParameterValidator implements JobParametersValidator {
    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String fileName = parameters.getString("fileName");

        if(!StringUtils.hasText(fileName)){
            throw new JobParametersInvalidException("fileName parameter is missing");

        } else if (!StringUtils.endsWithIgnoreCase(fileName, "csv")) { // 문자열의 마지막이 마지막 문자열과 같다면 true를 반환한다. (대소문자 구분 x)
            throw new JobParametersInvalidException("fileName parameter does" +
                                                                "not use the csv file extension");
        }
    }
}
