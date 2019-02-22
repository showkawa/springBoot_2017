package com.kawa.spbfeign.controller;

import com.kawa.spbfeign.pojo.User;
import com.kawa.spbfeign.service.BrianFeignQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrianFeignController {

    @Autowired
    private BrianFeignQuery brianFeignQuery;

    @RequestMapping(path = "/queryUserById/{id}", method = RequestMethod.GET)
    public ResponseEntity queryUserById(@PathVariable Long id) {
        User user =  brianFeignQuery.queryUserById(id);
        return new ResponseEntity(user, HttpStatus.OK);
    }
}
