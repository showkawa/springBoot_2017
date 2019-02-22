package com.kawa.spbfeign.service;

import com.kawa.spbfeign.pojo.User;
import org.springframework.stereotype.Component;


@Component
public class BrianQueryFallback implements BrianFeignQuery {


    @Override
    public User queryUserById(Long id) {
        return new User();
    }
}
