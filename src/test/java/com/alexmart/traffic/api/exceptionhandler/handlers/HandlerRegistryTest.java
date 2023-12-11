package com.alexmart.traffic.api.exceptionhandler.handlers;

import com.alexmart.traffic.domain.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class HandlerRegistryTest {

    @Mock
    private ContextRefreshedEvent event;
    @Mock
    private ApplicationContext applicationContext;

    private HandlerRegistry handlerRegistry;
    private Map<String, AbstractHandler> handlerMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handlerMap = getStringAbstractHandlerMap();
        handlerRegistry = new HandlerRegistry();
        when(event.getApplicationContext()).thenReturn(applicationContext);
        when(event.getApplicationContext().getBeansOfType(AbstractHandler.class)).thenReturn(handlerMap);
    }

    @Test
    void Given_on_application_context_refreshed_event_When_register_handlers_Then_register_handlers() {
        //Arrange
        String business = BusinessExceptionHandler.class.getName();
        String entityNotFound = EntityNotFoundExceptionHandler.class.getName();
        String dataIntegrityViolation = DataIntegrityViolationExceptionHandler.class.getName();

        //Act
        handlerRegistry.onApplicationEvent(event);

        //Assertion
        assertThat(handlerMap.size()).isEqualTo(3);
        assertThat(handlerMap.get(business).getClass().getName()).isEqualTo(business);
        assertThat(handlerMap.get(entityNotFound).getClass().getName()).isEqualTo(entityNotFound);
        assertThat(handlerMap.get(dataIntegrityViolation).getClass().getName()).isEqualTo(dataIntegrityViolation);
    }

    @Test
    void Given_an_Exception_When_getId_Then_return_id() {
        //Arrange
        Exception exception = new EntityNotFoundException("mgs");
        String id = "EntityNotFoundException";

        HandlerRegistry handlerRegistry = new HandlerRegistry();

        //Act
        String result = handlerRegistry.getId(exception);

        //Assertion
        assertThat(result).isEqualTo(id);
    }

    @Test
    void Given_on_application_context_refreshed_event_When_get_handler_Then_get_handler() {
        //Arrange
        String id = EntityNotFoundException.class.getSimpleName();
        handlerRegistry.onApplicationEvent(event);

        //Act
        AbstractHandler handler = handlerRegistry.getHandler(id);

        //Assertion
        assertThat(handler).isNotNull();
        assertThat(handler).isInstanceOf(EntityNotFoundExceptionHandler.class);
        assertThat(handler.getClass().getName()).isEqualTo(EntityNotFoundExceptionHandler.class.getName());
    }
    private Map<String, AbstractHandler> getStringAbstractHandlerMap() {
        Map<String, AbstractHandler> handlerMap = new HashMap<>();
        BusinessExceptionHandler businessExceptionHandler = new BusinessExceptionHandler();
        EntityNotFoundExceptionHandler entityNotFoundExceptionHandler = new EntityNotFoundExceptionHandler();
        DataIntegrityViolationExceptionHandler dataIntegrityViolationExceptionHandler = new DataIntegrityViolationExceptionHandler();
        handlerMap.put(BusinessExceptionHandler.class.getName(), businessExceptionHandler);
        handlerMap.put(EntityNotFoundExceptionHandler.class.getName(), entityNotFoundExceptionHandler);
        handlerMap.put(DataIntegrityViolationExceptionHandler.class.getName(), dataIntegrityViolationExceptionHandler);
        return handlerMap;
    }
}