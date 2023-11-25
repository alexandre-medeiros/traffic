package com.alexmart.traffic.api.exceptionhandler.handlers;

import com.alexmart.traffic.domain.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
@AllArgsConstructor
@Service
public class BusinessExceptionHandler implements AbstractHandler {

    @Override
    public ProblemDetail handler(Exception ex, WebRequest request, String url) {
        BusinessException error = (BusinessException) ex;
        String title = error.getMessage();
        return getProblemDetail(getHttpStatus(), title, url);
    }

    @Override
    public String getHandlerName() {
        return BusinessException.class.getSimpleName();
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
