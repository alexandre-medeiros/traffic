package com.alexmart.traffic.api.exceptionhandler.handlers;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import java.sql.SQLIntegrityConstraintViolationException;
@AllArgsConstructor
@Service
public class DataIntegrityViolationExceptionHandler implements AbstractHandler {

    @Override
    public ProblemDetail handler(Exception ex, WebRequest request, String url) {
        DataIntegrityViolationException error = (DataIntegrityViolationException) ex;
        String title = "Resource in use";
        ProblemDetail problemDetail = getProblemDetail(getHttpStatus(), title, url);
        problemDetail.setDetail(error.getMessage());
        return problemDetail;
    }

    @Override
    public String getHandlerName() {
        return SQLIntegrityConstraintViolationException.class.getSimpleName();
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}
