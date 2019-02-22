package com.kawa.spbfeign.service;


import com.kawa.spbfeign.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value="brian-query-service",path = "/kawa")
public interface BrianFeignQuery {
    @RequestMapping("/queryUserById/{id}")
    public User queryUserById(@PathVariable("id") Long id);

}
