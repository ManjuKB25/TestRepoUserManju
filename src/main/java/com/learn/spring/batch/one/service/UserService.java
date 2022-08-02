package com.learn.spring.batch.one.service;

import java.util.List;
import java.util.Map;

import com.learn.spring.batch.one.entity.UserData;
import com.learn.spring.batch.one.exceptions.InvalidUserIDException;
import com.learn.spring.batch.one.exceptions.UserNotFoundException;

public interface UserService {
	public List<UserData> getAllUsers();

	public UserData getUserById(String id) throws InvalidUserIDException, UserNotFoundException;

	public List<UserData> getUsersSortBy(String field);

	public List<UserData> getUsersFilterByName(String name);

	public List<UserData> getUsersFilterByAge(int age);

	public List<UserData> getUsersFilterBy(Map<String, String> dataQuery);
	
	public List<UserData> getUsersPaginatedData(int pageNo, int pageSize);

	public List<UserData> getUsersPaginatedDataAndSort(int pageNo, int pageSize, String field);

}
