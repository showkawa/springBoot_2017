package com.kawa.spb.controller;

import com.kawa.spb.pojo.User;
import com.kawa.spb.sercice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * 
 * @author Administrator
 *
 */
@RestController
public class UserController {

	@Autowired
	UserService userService;

	@RequestMapping(path = "/queryUserById/{id}", method = RequestMethod.GET)
	public ResponseEntity queryUserById(@PathVariable Long id) {
		User user = userService.queryUserById(id);
		return new ResponseEntity(user, HttpStatus.OK);
	}

}