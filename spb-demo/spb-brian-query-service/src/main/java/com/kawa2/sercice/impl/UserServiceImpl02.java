package com.kawa2.sercice.impl;

import com.github.pagehelper.PageHelper;
import com.kawa.dao.UserDao;
import com.kawa.pojo.User;
import com.kawa2.dao.UserDao02;
import com.kawa2.sercice.UserService02;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service
public class UserServiceImpl02 implements UserService02 {
	
	@Autowired
	UserDao02 userDao02;


	@Override
	public List<User> queryUserList(Map<String, Object> params) {
		return userDao02.queryUserList(params);
	}

	/*@Override
	public Integer insertUser(User user) {
		return userDao02.addUser(user);
	}

	@CachePut(value = "user",key = "#result.id")
	@Override
	public Boolean updateUser(User user) {
		return userDao02.updateUser(user);
	}

	@CacheEvict(value = "user",key = "#id")
	@Override
	public Integer deleteUserById(Long id) {
		return userDao02.deleteUserById(id);
	}

	@Cacheable(value = {"user"})
	@Override
	public User queryUserById(Long id) {
		return userDao02.queryUserById(id);
	}*/
}
