package com.learn.spring.batch.one.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.learn.spring.batch.one.entity.UserData;
import com.learn.spring.batch.one.exceptions.InvalidUserIDException;
import com.learn.spring.batch.one.exceptions.UserNotFoundException;
import com.learn.spring.batch.one.repository.UserRepo;
import com.learn.spring.batch.one.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo repo;

	@Override
	public List<UserData> getAllUsers() {
		return repo.findAll();
	}

	@Override
	public UserData getUserById(String id) {
		long userId;
		try {
			userId = Long.parseUnsignedLong(id);
		} catch (NumberFormatException ex) {
			throw new InvalidUserIDException("Provided UserID parameter is invalid : " + id);
		}
		return repo.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("No user found with the provided userID : " + userId));
	}

	@Override
	public List<UserData> getUsersSortBy(String field) {
		return repo.findAll(Sort.by(field));
	}

	@Override
	public List<UserData> getUsersFilterByName(String name) {
		return repo.findByName(name);
	}

	@Override
	public List<UserData> getUsersFilterByAge(int age) {
		return repo.findByAge(age);
	}

	@Override
	public List<UserData> getUsersFilterBy(Map<String, String> dataQuery) {
		String name = dataQuery.get("name");
		String age = dataQuery.get("age");
		if (null != name && null != age)
			return repo.findByNameAndAge(name, Integer.parseInt(age));
		if (null != age)
			return getUsersFilterByAge(Integer.parseInt(age));
		if (null != name)
			return getUsersFilterByName(name);
		return getAllUsers();
	}

	@Override
	public List<UserData> getUsersPaginatedData(int pageNo, int pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<UserData> pagedUsers = repo.findAll(paging);
		return pagedUsers.toList();
	}

	@Override
	public List<UserData> getUsersPaginatedDataAndSort(int pageNo, int pageSize, String field) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(field));
		Page<UserData> pagedUsers = repo.findAll(paging);
		return pagedUsers.toList();
	}

}
