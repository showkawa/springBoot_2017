package com.controller;

import com.github.pagehelper.PageInfo;
import com.kawa.job.AsyncService;
import com.kawa.pojo.User;
import com.kawa.pojo.UserQuery;
import com.kawa.pojo.UserQueryList;
import com.kawa.sercice.BrianService;
import com.kawa.sercice.UserService;
import com.kawa.sercice.impl.UserServiceImpl;
import com.kawa2.sercice.UserService02;
import com.kawa2.sercice.impl.UserServiceImpl02;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 
 * @author Administrator
 *
 */
@RestController
public class UserController {

	@Autowired
	UserServiceImpl userService;

	@Autowired
	UserServiceImpl02 userService02;

	@Autowired
	AsyncService asyncService;

	@Autowired
	BrianService brianService;



	@GetMapping(path = "/getAll")
	public ResponseEntity<List<User>> queryUsers() {
		List<User> queryAllUsers = userService02.queryUserList(null);
		return new ResponseEntity<>(queryAllUsers, HttpStatus.OK);
	}


    @PostMapping(path = "/multiTransactionalTest")
    public ResponseEntity multiTransactionalTest(@RequestBody User user) {
        userService02.insert2Multi(user);
        return new ResponseEntity(user, HttpStatus.OK);
    }

	@RequestMapping(path = "/getAll", method = RequestMethod.POST)
	public ResponseEntity<UserQueryList> queryUsers(HttpServletRequest request, @RequestBody UserQuery userQuery) {
		if (userQuery.getPage() == null) {
			//第一页从1开始而不是0
			userQuery.setPage(1);
		}
		if (userQuery.getRows() == null) {
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
		return new ResponseEntity<>(queryList, HttpStatus.OK);
	}


	@RequestMapping(path = "/addUser", method = RequestMethod.POST)
	public ResponseEntity addUsers(@RequestBody User user) {
		userService.insertUser(user);
		return new ResponseEntity(user, HttpStatus.OK);
	}

	@RequestMapping(path = "/updateUser", method = RequestMethod.POST)
	public ResponseEntity updateUser(@RequestBody User user) {
		userService.updateUser(user);
		return new ResponseEntity(user, HttpStatus.OK);
	}

	@RequestMapping(path = "/deleteUserById/{id}", method = RequestMethod.GET)
	public ResponseEntity deleteUserById(@PathVariable Long id) {
		userService.deleteUserById(id);
		return new ResponseEntity(id, HttpStatus.OK);
	}

	@RequestMapping(path = "/queryUserById/{id}", method = RequestMethod.GET)
	public ResponseEntity queryUserById(@PathVariable Long id) {
		User user = userService.queryUserById(id);
		return new ResponseEntity(user, HttpStatus.OK);
	}

	@GetMapping("/async")
	public String testJob() {
		asyncService.brianAsync();
		return "Good job";
	}

	@GetMapping("/loop/sendMsg")
	public void loopSendMsg() {
		try {
			brianService.sendMessageByThredPool();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/loop/sendMsg/userInfo")
	public ResponseEntity addUserInfo2MQ(@RequestBody User user) throws ExecutionException, InterruptedException {
		brianService.sendMessageByThredPool(user);
		return new ResponseEntity(user, HttpStatus.OK);

	}
}