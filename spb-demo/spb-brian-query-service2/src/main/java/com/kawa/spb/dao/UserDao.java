package com.kawa.spb.dao;


import com.kawa.spb.pojo.User;
import org.springframework.data.repository.PagingAndSortingRepository;


/**
 * 
 * @author huangliang
 *
 */
public interface UserDao extends PagingAndSortingRepository<User, Long> {
}
