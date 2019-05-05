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
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class UserServiceImpl02 implements UserService02 {
	
	@Resource
	UserDao02 userDao02;

	@Resource
	UserDao userDao;


	@Override
	public List<User> queryUserList(Map<String, Object> params) {
		return userDao02.queryUserList(params);
	}

	@Override
	public Integer insertUser(User user) {
		user.setPassword("10086");
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		return userDao02.addUser(user);
	}


	/**
	 * 多数据源的事务测试
	 * @param user
	 * @return
	 */
	@Transactional()
	public Integer insert2Multi(User user) {
		user.setPassword("10086");
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		Integer res = userDao.addUser(user);
		Integer res2 = userDao02.addUser(user);
		int err = 1 / 0;
		return res+ res2;
	}

	/*@CachePut(value = "user",key = "#result.id")
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
