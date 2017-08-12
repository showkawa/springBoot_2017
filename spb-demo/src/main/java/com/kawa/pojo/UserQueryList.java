package com.kawa.pojo;

import java.util.List;

public class UserQueryList {
	private List<User> users;
	private Integer totlePage;
	private Integer totleRecords;
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public Integer getTotlePage() {
		return totlePage;
	}
	public void setTotlePage(Integer totlePage) {
		this.totlePage = totlePage;
	}
	public Integer getTotleRecords() {
		return totleRecords;
	}
	public void setTotleRecords(Integer totleRecords) {
		this.totleRecords = totleRecords;
	}
	
}
