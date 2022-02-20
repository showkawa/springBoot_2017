package com.kawa.spbgateway.domain;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(value = "roles")
public class Roles {
    @Id
    private String username;
    private String role;
}
