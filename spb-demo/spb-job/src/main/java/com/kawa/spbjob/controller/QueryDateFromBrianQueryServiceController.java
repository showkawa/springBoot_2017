package com.kawa.spbjob.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kawa.spbjob.domian.User;
import com.kawa.spbjob.service.QueryBrainById;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryDateFromBrianQueryServiceController {
    @Autowired
    QueryBrainById queryBrainById;

    @RequestMapping(path = "/queryUserById/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> queryUserById(@PathVariable Long id) {
        ObjectMapper mapper = new ObjectMapper();
        Object user = queryBrainById.queryBrian(id);
        User u = mapper.convertValue(user, User.class);
        return new ResponseEntity(u, HttpStatus.OK);
    }
}
