package com.kawa2.dao;

import com.kawa.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author huangliang
 *
 */
public interface UserDao02 {

	@Insert("INSERT INTO user (id,username,password,phone,email,created,updated) VALUES (#{id},#{username},#{password},#{phone},#{email},#{created},#{updated})")
	Integer addUser(User user);

	@Select("SELECT * FROM user")
	List<User> queryUserList(Map<String, Object> params);

}
