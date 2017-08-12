package com.kawa.sercice.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
	


}
