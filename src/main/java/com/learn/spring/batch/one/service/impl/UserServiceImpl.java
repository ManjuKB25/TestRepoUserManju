package com.learn.spring.batch.one.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.learn.spring.batch.one.entity.UserData;
import com.learn.spring.batch.one.entity.validations.CustomValidate;
import com.learn.spring.batch.one.exceptions.EmptyUserRepoException;
import com.learn.spring.batch.one.exceptions.InvalidNumericValueException;
import com.learn.spring.batch.one.exceptions.RequiredFieldsException;
import com.learn.spring.batch.one.exceptions.UserNotFoundException;
import com.learn.spring.batch.one.repository.UserRepo;
import com.learn.spring.batch.one.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepo repo;

	@Override
	public List<UserData> getAllUsers() {
		List<UserData> userResult = repo.findAll();
		if (userResult.isEmpty()) {
			logger.warn("User Data repository is empty on get all users flow");
			throw new EmptyUserRepoException("Users repository is empty");
		}
		return userResult;
	}

	@Override
	public UserData getUserById(String id) {
		if (!CustomValidate.isValidUserId(id)) {
			throw new InvalidNumericValueException("Provided UserID parameter is invalid : " + id);
		}
		return repo.findById(Long.parseUnsignedLong(id))
				.orElseThrow(() -> new UserNotFoundException("No user found with the provided userID : " + id));
	}

	@Override
	public List<UserData> getUsersSortBy(String field) {
		if (!CustomValidate.isValidField(field)) {
			throw new RequiredFieldsException("'" + field + "' is not a valid field for sorting");
		}
		List<UserData> userResult = repo.findAll(Sort.by(field));
		if (userResult.isEmpty()) {
			logger.warn("User Data repository is empty on soryby flow");
			throw new EmptyUserRepoException("Users repository is empty");
		}
		return userResult;
	}

	@Override
	public List<UserData> getUsersFilterByName(String name) {
		List<UserData> userData = repo.findByName(name);
		if (userData.isEmpty()) {
			logger.warn("No data existing for this query on filterByName flow");
			throw new EmptyUserRepoException("No data existing for this query");
		}
		return userData;
	}

	@Override
	public List<UserData> getUsersFilterByAge(String age) {
		if (!CustomValidate.isValidAge(age)) {
			throw new InvalidNumericValueException("Provided age parameter is invalid : " + age);
		}
		List<UserData> userData = repo.findByAge(Integer.parseUnsignedInt(age));
		if (userData.isEmpty()) {
			logger.warn("No data existing for this query on filterByAge flow");
			throw new EmptyUserRepoException("No data existing for this query");
		}
		return userData;
	}

	@Override
	public List<UserData> getUsersFilterBy(Map<String, String> dataQuery) {
		if (!CustomValidate.isValidFields(dataQuery)) {
			throw new RequiredFieldsException("Fields provided for filtering are not valid");
		}
		String name = dataQuery.get("name");
		String age = dataQuery.get("age");
		String sortby = dataQuery.get("sortby");
		if (null != name && null != age) {
			if (!CustomValidate.isValidAge(age)) {
				throw new InvalidNumericValueException("Provided age parameter is invalid : " + age);
			}
			List<UserData> userData = new ArrayList<UserData>();
			if (sortby.isEmpty()) {
				userData = repo.findByNameAndAge(name, Integer.parseInt(age));
			} else {
				userData = repo.findByNameAndAgeAndSort(name, Integer.parseInt(age), Sort.by(sortby));
			}
			if (userData.isEmpty()) {
				logger.warn("No data existing for this query on filterBy flow");
				throw new EmptyUserRepoException("No data existing for this query");
			}
			return userData;
		}
		if (null != age) {
			if (!CustomValidate.isValidAge(age)) {
				throw new InvalidNumericValueException("Provided age parameter is invalid : " + age);
			}
			List<UserData> userData = new ArrayList<UserData>();
			if (sortby.isEmpty()) {
				userData = repo.findByAge(Integer.parseUnsignedInt(age));
			} else {
				userData = repo.findByAge(Integer.parseUnsignedInt(age), Sort.by(sortby));
			}
			return userData;
		}
		List<UserData> userData = new ArrayList<UserData>();
		if (sortby.isEmpty()) {
			userData = repo.findByName(name);
		} else {
			userData = repo.findByName(name, Sort.by(sortby));
		}
		return userData;
	}

	@Override
	public List<UserData> getUsersPageData(Map<String, String> pathVarsMap) {
		Pageable paging = getPagingObject(pathVarsMap);
		Page<UserData> pagedUsers = repo.findAll(paging);
		if (pagedUsers.isEmpty()) {
			logger.warn("No data existing for this query on paging flow");
			throw new EmptyUserRepoException("No data existing for this query");
		}
		return pagedUsers.toList();
	}

	private Pageable getPagingObject(Map<String, String> pathVarsMap) {
		Pageable paging = null;
		if (!CustomValidate.isVaildPagingOptions(pathVarsMap)) {
			throw new RequiredFieldsException(
					"For Pagination, both offset and size is required and should be positive integers.");
		}
		int offset = Integer.parseUnsignedInt(pathVarsMap.get("offset"));
		int size = Integer.parseUnsignedInt(pathVarsMap.get("size"));
		if (pathVarsMap.size() > 2) {
			if (CustomValidate.isValidField(pathVarsMap.get("field"))) {
				paging = PageRequest.of(offset, size, Sort.by(pathVarsMap.get("field")));
			} else {
				logger.warn("'" + pathVarsMap.get("field") + "' is not a valid field for sorting, ignoring");
				paging = PageRequest.of(offset, size);
			}
		} else if (pathVarsMap.size() == 2) {
			paging = PageRequest.of(offset, size);
		} else {
			throw new RequiredFieldsException("For Pagination, both offset and size is required.");
		}
		return paging;
	}

}
