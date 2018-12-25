package com.kawa.spbjob.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class QueryBrainById {

    @Autowired
    RestTemplate restTemplate;

    public Object queryBrian(Long id){
        Object forObject = restTemplate.getForObject("http://brian-query-service/kawa/queryUserById/1",Object.class);
        return forObject;
    }
}
