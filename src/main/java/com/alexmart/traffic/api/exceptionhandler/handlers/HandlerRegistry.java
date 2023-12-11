package com.alexmart.traffic.api.exceptionhandler.handlers;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for managing and providing access to handlers in the application.
 */
@Component
public class HandlerRegistry implements ApplicationListener<ContextRefreshedEvent> {

    // Map to store handler instances with their names
    private final Map<String, AbstractHandler> handlerMap = new HashMap<>();

    /**
     * This method is triggered when the application context is refreshed.
     * It populates the handlerMap with instances of AbstractHandler on application startup.
     *
     * @param event The context refreshed event.
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Populate the handlerMap on application startup
        event.getApplicationContext()
                .getBeansOfType(AbstractHandler.class)
                .values()
                .forEach(handler ->
                        handlerMap.put(handler.getHandlerName(), handler)
                );
    }

    /**
     * Retrieves an AbstractHandler instance based on the provided handler name.
     *
     * @param handlerName The name of the handler to retrieve.
     * @return The AbstractHandler instance associated with the given handler name, or null if not found.
     */
    public AbstractHandler getHandler(String handlerName) {

        return handlerMap.get(handlerName);
    }

    /**
     * Extracts and returns a simplified identifier for the given exception.
     * This identifier is derived from the simple class name of the root cause of the exception.
     *
     * @param exception The exception for which to obtain the identifier.
     * @return A simplified identifier based on the simple class name of the root cause.
     */
    public String getId(Exception exception) {
        // Traverse the exception chain to find the root cause
        Throwable rootCause = exception;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }

        // Return the simple class name of the root cause as the identifier
        return rootCause.getClass().getSimpleName();
    }
}
