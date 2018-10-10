package com.kawa.sercice.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.kawa.dao.UserDao;
import com.kawa.pojo.User;
import com.kawa.sercice.UserService;


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserDao userDao;


	@Override
	public List<User> queryUserList(Map<String, Object> params) {
		PageHelper.startPage(Integer.parseInt(params.get("page").toString()) , Integer.parseInt(params.get("rows").toString()));
		return userDao.queryUserList(params);
	}

	@Override
	public Integer insertUser(User user) {
		return userDao.addUser(user);
	}

	@CachePut(value = "user",key = "#result.id")
	@Override
	public Boolean updateUser(User user) {
		return userDao.updateUser(user);
	}

	@CacheEvict(value = "user",key = "#id")
	@Override
	public Integer deleteUserById(Long id) {
		return userDao.deleteUserById(id);
	}

	@Cacheable(value = {"user"})
	@Override
	public User queryUserById(Long id) {
		return userDao.queryUserById(id);
	}
}
