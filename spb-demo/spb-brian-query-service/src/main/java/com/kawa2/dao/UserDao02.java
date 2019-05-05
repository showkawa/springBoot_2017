package com.kawa2.dao;

import com.kawa.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author huangliang
 *
 */
//@Mapper
public interface UserDao02 {

	/*Integer addUser(User user);

	Boolean updateUser(User user);

	Integer deleteUserById(Long id);

	User queryUserById(Long id);*/

	@Select("SELECT * FROM user")
	List<User> queryUserList(Map<String, Object> params);

}
