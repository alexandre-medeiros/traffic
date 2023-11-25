package com.alexmart.traffic.api.exceptionhandler.handlers;

import com.alexmart.traffic.domain.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.context.request.WebRequest;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class EntityNotFoundExceptionHandlerTest {

    @Test
    void handler_ShouldReturnCorrectProblemDetail() {
        // Arrange
        EntityNotFoundExceptionHandler handler = new EntityNotFoundExceptionHandler();
        EntityNotFoundException exception = new EntityNotFoundException("Entity not found");
        WebRequest request = mock(WebRequest.class);
        String url = "/example";

        // Act
        ProblemDetail problemDetail = handler.handler(exception, request, url);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), problemDetail.getStatus());
        assertEquals("Entity not found", problemDetail.getTitle());
        assertEquals(URI.create("/example"), problemDetail.getType());
    }

    @Test
    void getHandlerName_ShouldReturnCorrectHandlerName() {
        // Arrange
        EntityNotFoundExceptionHandler handler = new EntityNotFoundExceptionHandler();

        // Act
        String handlerName = handler.getHandlerName();

        // Assert
        assertEquals("EntityNotFoundException", handlerName);
    }

    @Test
    void getHttpStatus_ShouldReturnCorrectHttpStatus() {
        // Arrange
        EntityNotFoundExceptionHandler handler = new EntityNotFoundExceptionHandler();

        // Act
        HttpStatus httpStatus = handler.getHttpStatus();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, httpStatus);
    }

    @Test
    void getProblemDetail_ShouldReturnCorrectProblemDetail() {
        // Arrange
        EntityNotFoundExceptionHandler handler = new EntityNotFoundExceptionHandler();

        // Act
        ProblemDetail problemDetail = handler.getProblemDetail(HttpStatus.BAD_REQUEST, "Validation failed", "example:validation-error");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals("Validation failed", problemDetail.getTitle());
        assertEquals(URI.create("example:validation-error"), problemDetail.getType());
    }
}
