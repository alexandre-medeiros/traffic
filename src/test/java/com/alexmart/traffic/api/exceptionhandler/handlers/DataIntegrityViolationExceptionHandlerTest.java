package com.alexmart.traffic.api.exceptionhandler.handlers;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.context.request.WebRequest;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class DataIntegrityViolationExceptionHandlerTest {

    @Test
    void testHandler() {
        DataIntegrityViolationExceptionHandler handler = new DataIntegrityViolationExceptionHandler();
        WebRequest request = mock(WebRequest.class);
        String url = "/example";
        String errorMessage = "Data integrity violation error message";

        DataIntegrityViolationException exception = new DataIntegrityViolationException(errorMessage);

        ProblemDetail problemDetail = handler.handler(exception, request, url);

        assertEquals(HttpStatus.CONFLICT.value(), problemDetail.getStatus());
        assertEquals("Resource in use", problemDetail.getTitle());
        assertEquals(URI.create(url), problemDetail.getType());
        assertEquals(errorMessage, problemDetail.getDetail());
        // Add more assertions if needed
    }

    @Test
    void testGetHandlerName() {
        DataIntegrityViolationExceptionHandler handler = new DataIntegrityViolationExceptionHandler();
        assertEquals("SQLIntegrityConstraintViolationException", handler.getHandlerName());
    }

    @Test
    void testGetHttpStatus() {
        DataIntegrityViolationExceptionHandler handler = new DataIntegrityViolationExceptionHandler();
        assertEquals(HttpStatus.CONFLICT, handler.getHttpStatus());
    }

    @Test
    void testGetProblemDetail() {
        AbstractHandler abstractHandler = new AbstractHandler() {
            @Override
            public ProblemDetail handler(Exception ex, WebRequest request, String url) {
                return null; // Not used in this test
            }

            @Override
            public String getHandlerName() {
                return null; // Not used in this test
            }

            @Override
            public HttpStatus getHttpStatus() {
                return null; // Not used in this test
            }
        };

        ProblemDetail problemDetail = abstractHandler.getProblemDetail(HttpStatus.BAD_REQUEST, "Validation Error", null);

        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("Validation Error", problemDetail.getTitle());
        assertEquals("about:blank", problemDetail.getType().toString());
    }
}
