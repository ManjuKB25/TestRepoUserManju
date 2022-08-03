package com.learn.spring.batch.one.service;

import java.util.List;
import java.util.Map;

import com.learn.spring.batch.one.entity.UserData;
import com.learn.spring.batch.one.exceptions.EmptyUserRepoException;
import com.learn.spring.batch.one.exceptions.InvalidNumericValueException;
import com.learn.spring.batch.one.exceptions.RequiredFieldsException;
import com.learn.spring.batch.one.exceptions.UserNotFoundException;

/* 
 * Service Interface for User Service
 * */
public interface UserService {
	public List<UserData> getAllUsers(String sortType) throws EmptyUserRepoException;

	public UserData getUserById(String id) throws InvalidNumericValueException, UserNotFoundException;

	public List<UserData> getUsersSortBy(Map<String, String> pathVarsMap) throws RequiredFieldsException, EmptyUserRepoException;

	public List<UserData> getUsersFilterByName(String name) throws EmptyUserRepoException;

	public List<UserData> getUsersFilterByAge(String age) throws InvalidNumericValueException, EmptyUserRepoException;

	public List<UserData> getUsersFilterBy(Map<String, String> dataQuery)
			throws RequiredFieldsException, InvalidNumericValueException, EmptyUserRepoException;

	public List<UserData> getUsersPageData(Map<String, String> pathVarsMap)
			throws RequiredFieldsException, InvalidNumericValueException, EmptyUserRepoException;

}
