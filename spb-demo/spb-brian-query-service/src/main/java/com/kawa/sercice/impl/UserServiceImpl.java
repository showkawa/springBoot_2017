package com.kawa.sercice.impl;

import com.github.pagehelper.PageHelper;
import com.kawa.dao.UserDao;
import com.kawa.pojo.User;
import com.kawa.sercice.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {
	
	@Resource
	UserDao userDao;


	@Override
	public List<User> queryUserList(Map<String, Object> params) {
		PageHelper.startPage(Integer.parseInt(params.get("page").toString()) , Integer.parseInt(params.get("rows").toString()));
		return userDao.queryUserList(params);
	}

	@Override
	public Integer insertUser(User user) {
		user.setPassword("10086");
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
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
