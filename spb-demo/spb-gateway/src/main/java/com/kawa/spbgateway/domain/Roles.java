package com.kawa.spbgateway.domain;


import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@ToString
@Table(value = "roles")
public class Roles {
    @Id
    @Column("username")
    private String userName;

    @Column("role")
    private String role;
}
