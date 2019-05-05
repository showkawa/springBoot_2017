package com.kawa2.sercice;

import com.kawa.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService02 {
	List<User> queryUserList(Map<String, Object> params);
	Integer insertUser(User user);
	/*Boolean updateUser(User user);
	Integer deleteUserById(Long id);
	User queryUserById(Long id);*/
}
