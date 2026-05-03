package com.orderprocessingeda.orderservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request){

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> { errors.put(error.getField(), error.getDefaultMessage());
        });

        ErrorResponse response = new ErrorResponse("Validation failed", errors, ErrorCodes.VALIDATION_FAILED, request.getRequestURI() , LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request){

        log.error("Unexpected Error at {}",request.getRequestURI(), ex);
        ErrorResponse response = new ErrorResponse("Internal Server Error", null, ErrorCodes.INTERNAL_ERROR, request.getRequestURI(), LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStock(InsufficientStockException ex, HttpServletRequest request){

        log.warn("Insufficient stock at {}", request.getRequestURI());
        ErrorResponse response = new ErrorResponse(ex.getMessage(), null,ErrorCodes.INSUFFICIENT_STOCK, request.getRequestURI(),  LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InventoryUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleInventoryUnavailable(InventoryUnavailableException ex, HttpServletRequest request){

        log.error("Inventory unavailable at {}", request.getRequestURI());
        ErrorResponse response = new ErrorResponse(ex.getMessage(), null, ErrorCodes.INVENTORY_UNAVAILABLE, request.getRequestURI(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(feign.FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(feign.FeignException ex, HttpServletRequest request){
        log.error("Inventory service call failed at {}", request.getRequestURI(), ex);
        ErrorResponse response = new ErrorResponse("Inventory Service Unavailable", null, ErrorCodes.INVENTORY_UNAVAILABLE, request.getRequestURI(), LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
