package com.alexmart.traffic.api.exceptionhandler;

import com.alexmart.traffic.api.exceptionhandler.handlers.AbstractHandler;
import com.alexmart.traffic.api.exceptionhandler.handlers.HandlerRegistry;
import com.alexmart.traffic.domain.exception.BusinessException;
import com.alexmart.traffic.domain.exception.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.net.URI;

/**
 * RestControllerAdvice that handles exceptions and provides a standardized
 * problem details response following RFC 9457 - Problem Details for HTTP APIs.
 *
 * @see <a href="https://www.rfc-editor.org/info/rfc9457">RFC 9457</a>
 */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private HandlerRegistry handlerRegistry;

    @Autowired
    private HttpServletRequest servletRequest;

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        return handleException(ex, headers, request);
    }

    @ExceptionHandler({BusinessException.class, EntityNotFoundException.class, DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleBusinessException(Exception ex, WebRequest request) {
        return handleException(ex, HttpHeaders.EMPTY, request);
    }

    private ResponseEntity<Object> handleException(Exception ex, HttpHeaders headers, WebRequest request) {
        String id = handlerRegistry.getId(ex);
        AbstractHandler handler = handlerRegistry.getHandler(id);

        if (handler != null) {
            /*
             * This url could be create in application to provide human-readable documentation for the
             * problem type. If not, its value is assumed to be "about:blank".
             * Remove this url defining null if url will not be create
             */
            String url = servletRequest.getRequestURL().toString().concat(("/error"));
            ProblemDetail body = handler.handler(ex, request, url);
            HttpStatusCode statusCode = handler.getHttpStatus();
            return super.handleExceptionInternal(ex, body, headers, statusCode, request);
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setType(URI.create("about:blank"));
        return super.handleExceptionInternal(ex, problemDetail, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
