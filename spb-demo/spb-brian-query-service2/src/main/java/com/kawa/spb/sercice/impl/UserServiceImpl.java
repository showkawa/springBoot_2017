package com.kawa.spb.sercice.impl;

import com.kawa.spb.dao.UserDao;
import com.kawa.spb.pojo.User;
import com.kawa.spb.sercice.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;



@Service
public class UserServiceImpl implements UserService {

   Logger  logger  = LoggerFactory.getLogger(UserServiceImpl.class);

   @Autowired
	UserDao userDao;


	@Cacheable(value = {"user"})
	@Override
	public User queryUserById(Long id) {
		id = Long.parseLong("34");
		Optional<User> user = userDao.findById(id);
		logger.info("------------------------------query-------service---------");
		return user.get();
	}
}
