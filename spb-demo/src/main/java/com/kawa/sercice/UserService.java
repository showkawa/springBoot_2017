package com.kawa.sercice;

import java.util.List;
import java.util.Map;

import com.kawa.pojo.User;

public interface UserService {
	List<User> queryUserList(Map<String,Object> params);

}
