package com.kawa.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.kawa.pojo.User;

/**
 * 
 * @author huangliang
 *
 */
@Mapper
public interface UserDao {
	Integer addUser(User user);
	Integer updateUser(User user);
	Integer deleteUserByIds(String[] ids);
	User queryUserById(Long id);
	List<User> queryUserList(Map<String,Object> params);
}
