package com.example.demo.global.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class CustomFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        //HttpStatus
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());

        if (responseStatus.is5xxServerError()) {
            //5xx 애러 핸들링
            return new CustomException(ErrorCode.API_4xx_ERROR);
        } else if (responseStatus.is4xxClientError()) {
            //4xx 애러 핸들링
            return new CustomException(ErrorCode.API_5xx_ERROR);
        } else {
            return new Exception("Generic exception");
        }
    }
}