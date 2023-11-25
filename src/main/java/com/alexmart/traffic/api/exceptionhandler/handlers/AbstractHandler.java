package com.alexmart.traffic.api.exceptionhandler.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.context.request.WebRequest;
import java.net.URI;
import java.util.Map;
/**
 * Interface defining the contract to handle errors using the "Problem Details for HTTP APIs" standardized
 * following RFC 9457 - Problem Details for HTTP APIs.
 *
 * @see <a href="https://www.rfc-editor.org/info/rfc9457">RFC 9457</a>
 */
public interface AbstractHandler {

    ProblemDetail handler(Exception ex, WebRequest request, String url);

    String getHandlerName();

    HttpStatus getHttpStatus();

    /**
     * Provides a default implementation to create a 'ProblemDetail' object with specified fields, status, title, and type.
     *
     * @param fields when exist, list fields and their errors
     * @param status The HTTP status associated with the error.
     * @param title  A short, human-readable summary of the problem type.
     * @param type   A URI reference that identifies the "problem type".
     *               This URI could be create to provide human-readable
     *               documentation for the problem type.
     *               When this member is not present, its value is assumed to be "about:blank".
     * @return A 'ProblemDetail' object with the specified details.
     */
    default ProblemDetail getProblemDetail(Map<String, String> fields, HttpStatus status, String title, String type) {
        ProblemDetail problemDetail = getProblemDetail(status, title, type);
        problemDetail.setProperty("fields", fields);
        return problemDetail;
    }

    default ProblemDetail getProblemDetail(HttpStatus status, String title, String type) {
        if (type == null) {
            type = "about:blank";
        }
        ProblemDetail problemDetail = ProblemDetail.forStatus(status.value());
        problemDetail.setTitle(title);
        problemDetail.setType(URI.create(type));

        return problemDetail;
    }
}
