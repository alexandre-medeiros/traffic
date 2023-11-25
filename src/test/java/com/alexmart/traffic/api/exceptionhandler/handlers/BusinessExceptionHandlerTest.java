package com.alexmart.traffic.api.exceptionhandler.handlers;

import com.alexmart.traffic.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class BusinessExceptionHandlerTest {

    @Test
    void handler_ShouldReturnProblemDetailWithBadRequestStatus() {
        // Arrange
        BusinessExceptionHandler handler = new BusinessExceptionHandler();
        WebRequest request = mock(WebRequest.class);
        String url = "/test-url";
        BusinessException businessException = new BusinessException("Test Business Exception");

        // Act
        ProblemDetail problemDetail = handler.handler(businessException, request, url);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("Test Business Exception", problemDetail.getTitle());
    }

    @Test
    void getHandlerName_ShouldReturnBusinessExceptionSimpleName() {
        // Arrange
        BusinessExceptionHandler handler = new BusinessExceptionHandler();

        // Act
        String handlerName = handler.getHandlerName();

        // Assert
        assertEquals("BusinessException", handlerName);
    }

    @Test
    void getHttpStatus_ShouldReturnBadRequestStatus() {
        // Arrange
        BusinessExceptionHandler handler = new BusinessExceptionHandler();

        // Act
        HttpStatus httpStatus = handler.getHttpStatus();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, httpStatus);
    }

    @Test
    void getProblemDetail_ShouldReturnProblemDetailWithSpecifiedFields() {
        // Arrange
        AbstractHandler abstractHandler = new AbstractHandler() {
            @Override
            public ProblemDetail handler(Exception ex, WebRequest request, String url) {
                return null;
            }

            @Override
            public String getHandlerName() {
                return null;
            }

            @Override
            public HttpStatus getHttpStatus() {
                return null;
            }
        };

        // Act
        ProblemDetail problemDetail = abstractHandler.getProblemDetail(null, HttpStatus.BAD_REQUEST, "Test Title", "http://example.com/error");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("Test Title", problemDetail.getTitle());
        assertEquals("http://example.com/error", problemDetail.getType().toString());
    }
}
