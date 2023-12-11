package com.alexmart.traffic.api.exceptionhandler.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class MethodArgumentNotValidExceptionHandlerTest {

    public static final String CUSTOMIZED_ERROR_MESSAGE = "customized error message";
    public static final String FIELD_NAME = "fieldName";
    public static final String OBJECT_NAME = "objectName";
    public static final String INVALID_PARAMS = "Invalid params";
    public static final String FIELDS = "fields";
    public static final String HTTPS_DOMAIN_RESOURCE_ERROR = "https://domain/resource/error";

    @Mock
    private HandlerRegistry handlerRegistry;
    @Mock
    private MessageSource messageSource;
    @Mock
    private MethodArgumentNotValidException exception;
    @Mock
    private WebRequest request;
    @Mock
    private BindingResult bindingResult;

    private MethodArgumentNotValidExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exceptionHandler = new MethodArgumentNotValidExceptionHandler(handlerRegistry, messageSource);
    }

    @Test
    void testHandlerWithNoFieldErrors() {
        // arrange
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());

        // act
        ProblemDetail problemDetail = exceptionHandler.handler(exception, request, HTTPS_DOMAIN_RESOURCE_ERROR);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals(INVALID_PARAMS, problemDetail.getTitle());
        assertEquals(URI.create(HTTPS_DOMAIN_RESOURCE_ERROR), problemDetail.getType());

        Map<String, Object> properties = problemDetail.getProperties();
        assertNotNull(properties);

        // Verify that methods were called as expected
        verify(exception, times(1)).getBindingResult();
        verify(bindingResult, times(1)).getFieldErrors();
        verifyNoInteractions(messageSource);
    }

    @Test
    void testHandler() {
        // Arrange
        String url = HTTPS_DOMAIN_RESOURCE_ERROR;
        FieldError fieldError = new FieldError(OBJECT_NAME, FIELD_NAME, "default error message");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
        when(messageSource.getMessage(eq(fieldError), any(Locale.class))).thenReturn(CUSTOMIZED_ERROR_MESSAGE);

        // Act
        ProblemDetail problemDetail = exceptionHandler.handler(exception, request, url);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals(INVALID_PARAMS, problemDetail.getTitle());
        assertEquals(URI.create(url), problemDetail.getType());

        Map<String, Object> properties = problemDetail.getProperties();
        if (properties == null) {
            fail("properties null at problemDetail");
        } else assertEquals(1, properties.size());

        Map<String, String> fields = (Map<String, String>) properties.get(FIELDS);
        if (fields == null) {
            fail("fields null at problemDetail");
        } else assertEquals(1, fields.size());

        assertEquals(CUSTOMIZED_ERROR_MESSAGE, fields.get(FIELD_NAME));

        // Verify that methods were called as expected
        verify(exception, times(1)).getBindingResult();
        verify(bindingResult, times(1)).getFieldErrors();
        verify(messageSource, times(1)).getMessage(eq(fieldError), any(Locale.class));
    }

    @Test
    void testHandlerWithMultipleFieldErrors() {
        // Arrange
        String url = HTTPS_DOMAIN_RESOURCE_ERROR;
        FieldError fieldError1 = new FieldError(OBJECT_NAME, FIELD_NAME, "error message 1");
        FieldError fieldError2 = new FieldError(OBJECT_NAME, "anotherField", "error message 2");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));
        when(messageSource.getMessage(eq(fieldError1), any(Locale.class))).thenReturn("customized error message 1");
        when(messageSource.getMessage(eq(fieldError2), any(Locale.class))).thenReturn("customized error message 2");

        // Act
        ProblemDetail problemDetail = exceptionHandler.handler(exception, request, url);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(), problemDetail.getStatus());
        assertEquals(INVALID_PARAMS, problemDetail.getTitle());
        assertEquals(URI.create(url), problemDetail.getType());

        Map<String, Object> properties = problemDetail.getProperties();
        assertNotNull(properties);
        assertEquals(1, properties.size());

        Map<String, String> fields = (Map<String, String>) properties.get(FIELDS);
        assertNotNull(fields);
        assertEquals(2, fields.size());
        assertEquals("customized error message 1", fields.get(FIELD_NAME));
        assertEquals("customized error message 2", fields.get("anotherField"));

        // Verify that methods were called as expected
        verify(exception, times(1)).getBindingResult();
        verify(bindingResult, times(1)).getFieldErrors();
        verify(messageSource, times(1)).getMessage(eq(fieldError1), any(Locale.class));
        verify(messageSource, times(1)).getMessage(eq(fieldError2), any(Locale.class));
    }

    @Test
    void testGetHandlerName() {
        assertEquals("MethodArgumentNotValidException", exceptionHandler.getHandlerName());
    }

    @Test
    void testGetHttpStatus() {
        assertEquals(HttpStatus.BAD_REQUEST, exceptionHandler.getHttpStatus());
    }
}
