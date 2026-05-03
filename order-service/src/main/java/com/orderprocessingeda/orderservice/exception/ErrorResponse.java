package com.orderprocessingeda.orderservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String message;

    private Map<String, String> errors;

    private String errorCode;

    private String errorPath;

    private LocalDateTime timestamp;
}
