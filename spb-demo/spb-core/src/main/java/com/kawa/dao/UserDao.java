package com.kawa.dao;

import com.kawa.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author huangliang
 *
 */
@Mapper
public interface UserDao {

	Integer addUser(User user);

	Boolean updateUser(User user);

	Integer deleteUserById(Long id);

	User queryUserById(Long id);

	List<User> queryUserList(Map<String,Object> params);
}
