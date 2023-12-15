package com.alexmart.traffic.api.exceptionhandler;

import com.alexmart.traffic.api.exceptionhandler.handlers.AbstractHandler;
import com.alexmart.traffic.api.exceptionhandler.handlers.BusinessExceptionHandler;
import com.alexmart.traffic.api.exceptionhandler.handlers.HandlerRegistry;
import com.alexmart.traffic.domain.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
class ApiExceptionHandlerTest {

    @Mock
    private HandlerRegistry handlerRegistry;
    @Mock
    private HttpServletRequest servletRequest;
    @Mock
    private WebRequest request;

    private Object body;
    private HttpHeaders headers;
    private HttpStatusCode statusCode;
    private ApiExceptionHandler apiExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        apiExceptionHandler = new ApiExceptionHandler(handlerRegistry, servletRequest);
    }

    @Test
    void Given_BusinessExceptionHandler_When_HandleExceptionInternal_Then_ReturnResponseEntityWithBody() {
        //Arrange
        String msg = "Any Business Exception";
        String id = "BusinessException";
        String url = "/any";
        Exception exception = new BusinessException(msg);
        AbstractHandler handler = new BusinessExceptionHandler();
        statusCode = HttpStatus.BAD_REQUEST;
        ProblemDetail problemDetail = ProblemDetail.forStatus(statusCode.value());
        problemDetail.setTitle(msg);
        problemDetail.setType(URI.create(url + "/error"));
        body = problemDetail;
        headers = new HttpHeaders();
        StringBuffer requestURL = new StringBuffer(url);
        when(servletRequest.getRequestURL()).thenReturn(requestURL);
        when(handlerRegistry.getId(exception)).thenReturn(id);
        when(handlerRegistry.getHandler(id)).thenReturn(handler);

        //Act
        ResponseEntity<Object> responseEntity = apiExceptionHandler.handleExceptionInternal(exception, body, headers, statusCode, request);
        ProblemDetail responseEntityBody = (ProblemDetail) responseEntity.getBody();

        //Assert
        assertThat(responseEntityBody.getType()).isEqualTo(problemDetail.getType());
        assertThat(responseEntityBody.getTitle()).isEqualTo(problemDetail.getTitle());
        assertThat(responseEntityBody.getStatus()).isEqualTo(problemDetail.getStatus());
    }
}
