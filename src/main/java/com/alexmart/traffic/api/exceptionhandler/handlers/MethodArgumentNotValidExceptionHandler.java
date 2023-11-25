package com.alexmart.traffic.api.exceptionhandler.handlers;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MethodArgumentNotValidExceptionHandler implements AbstractHandler {

    private final HandlerRegistry handlerRegistry;
    private final MessageSource messageSource;

    @Override
    public ProblemDetail handler(Exception ex, WebRequest request, String url) {
        MethodArgumentNotValidException error = (MethodArgumentNotValidException) ex;
        /*
         * List of invalid params and theirs error messages customized at
         * messages.properties file
         */
        Map<String, String> fields = error
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, e -> messageSource.getMessage(e, LocaleContextHolder.getLocale())));

        String title = "Invalid params";
        return getProblemDetail(fields, getHttpStatus(), title, url);
    }

    @Override
    public String getHandlerName() {
        return MethodArgumentNotValidException.class.getSimpleName();
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
