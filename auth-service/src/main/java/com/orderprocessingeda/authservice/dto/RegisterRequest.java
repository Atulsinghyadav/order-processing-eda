package com.orderprocessingeda.authservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class RegisterRequest {

    private String username;

    private String password;

}
