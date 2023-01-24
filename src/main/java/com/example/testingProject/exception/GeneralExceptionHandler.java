package com.example.testingProject.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * @ExceptionHandler This method handles all exceptions
     * which are inherited from the class BaseRuntimeException
     */
    @ExceptionHandler(BaseRuntimeException.class)
    public ResponseEntity<ErrorResponse> generalHandler(BaseRuntimeException exception) {
        ErrorResponse error = new ErrorResponse(
                exception.getStatus(),
                exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * This method handles all exceptions related to
     * with @Valid validation in dto models
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                ex.getAllErrors().stream().findFirst().get().getDefaultMessage());
        return new ResponseEntity<>(error, status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                "Invalid request body");
        return new ResponseEntity<>(error, status);
    }

    /**
     * This method handles all exceptions related to
     * with validation of @Valid GET requests in dto models
     */
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex,
                                                         HttpHeaders headers,
                                                         HttpStatus status,
                                                         WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                ex.getBindingResult().getAllErrors().stream().findFirst().get().getDefaultMessage());
        return new ResponseEntity<>(error, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                "Invalid request body");
        return new ResponseEntity<>(error, status);
    }

    @Data
    public static class ErrorResponse {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
        private LocalDateTime timestamp;
        private int status;
        private String message;

        public ErrorResponse(int status, String message) {
            this.timestamp = LocalDateTime.now();
            this.status = status;
            this.message = message;
        }
    }
}
