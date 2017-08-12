package com.kawa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.kawa.pojo.User;
import com.kawa.pojo.UserQuery;
import com.kawa.pojo.UserQueryList;
import com.kawa.sercice.UserService;

/**
 * 
 * @author Administrator
 *
 */
@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	@RequestMapping(path="/getAll",method=RequestMethod.POST)
	public ResponseEntity<UserQueryList> queryUsers(HttpServletRequest request,@RequestBody UserQuery userQuery){
		if(userQuery.getPage() == null){
			//第一页从1开始而不是0
			userQuery.setPage(1);
		}
		if(userQuery.getRows() == null){
			userQuery.setRows(10);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("page", userQuery.getPage());
		map.put("rows", userQuery.getRows());
		map.put("username", userQuery.getUsername());
		map.put("phone", userQuery.getPhone());
		map.put("email", userQuery.getEmail());
		List<User> queryAllUsers = userService.queryUserList(map);
		PageInfo<User> pageInfo = new PageInfo<>(queryAllUsers);
		UserQueryList queryList = new UserQueryList();
		queryList.setUsers(queryAllUsers);
		queryList.setTotlePage(pageInfo.getPages());
		Integer total = new Long(pageInfo.getTotal()).intValue();
		queryList.setTotleRecords(total);
		return new ResponseEntity<UserQueryList>(queryList,HttpStatus.OK);
	}

}
