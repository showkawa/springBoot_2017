package com.kawa.spbgateway.domain;


import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Roles {
    @Id
    private String username;
    private String role;
}
