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
import org.springframework.data.domain.Sort.Direction;
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
	// Below properties can be read from a properties file to make them
	// customizable.
	private static final int defaultPageOffset = 0;
	private static final int defaultPageSize = 100;
	private static final String defaultSortField = "id";

	@Autowired
	private UserRepo repo;

	@Override
	public List<UserData> getAllUsers(String sortType) {
		Pageable paging = PageRequest.of(defaultPageOffset, defaultPageSize,
				Direction.fromOptionalString(sortType).orElse(Direction.DESC), defaultSortField);
		Page<UserData> userResult = repo.findAll(paging);
		if (userResult.isEmpty()) {
			logger.warn("User Data repository is empty on get all users flow");
			throw new EmptyUserRepoException("Users repository is empty");
		}
		return userResult.toList();
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
	public List<UserData> getUsersSortBy(Map<String, String> field) {
		if (!CustomValidate.isValidFields(field)) {
			throw new RequiredFieldsException("'" + field.get("sortby") + "' is not a valid field for sorting");
		}
		Pageable paging = PageRequest.of(defaultPageOffset, defaultPageSize,
				Direction.fromOptionalString(field.get("sortType")).orElse(Direction.DESC), field.get("sortby"));
		Page<UserData> userResult = repo.findAll(paging);
		if (userResult.isEmpty()) {
			logger.warn("User Data repository is empty on soryby flow");
			throw new EmptyUserRepoException("Users repository is empty");
		}
		return userResult.toList();
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
		if (!CustomValidate.isValidField(dataQuery)) {
			throw new RequiredFieldsException("Fields provided for filtering are not valid");
		}
		Pageable paging = PageRequest.of(defaultPageOffset, defaultPageSize,
				Direction.fromOptionalString(dataQuery.get("sortType")).orElse(Direction.DESC), defaultSortField);
		if (dataQuery.size() == 0) {
			Page<UserData> userData = repo.findAll(paging);
			if (userData.isEmpty()) {
				logger.warn("No data existing for this query on filterBy flow");
				throw new EmptyUserRepoException("No data existing for this query");
			}
			return userData.toList();
		}
		String name = dataQuery.get("name");
		String age = dataQuery.get("age");
		String sortby = dataQuery.get("sortby") != null ? dataQuery.get("sortby") : "id";
		Direction sortType = Direction.fromOptionalString(dataQuery.get("sortType")).orElse(Direction.DESC);
		paging = PageRequest.of(defaultPageOffset, defaultPageSize, sortType, sortby);

		if (null != name && null != age) {
			if (!CustomValidate.isValidAge(age)) {
				throw new InvalidNumericValueException("Provided age parameter is invalid : " + age);
			}
			List<UserData> userData = new ArrayList<UserData>();
			if (sortby.isEmpty()) {
				userData = repo.findByNameAndAge(name, Integer.parseInt(age));
			} else {
				userData = repo.findByNameAndAgeAndSort(name, Integer.parseInt(age), paging);
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
				userData = repo.findByAge(Integer.parseUnsignedInt(age), paging);
			}
			if (userData.isEmpty()) {
				logger.warn("No data existing for this query on filterBy flow");
				throw new EmptyUserRepoException("No data existing for this query");
			}
			return userData;
		}
		if (null != name) {
			List<UserData> userData = new ArrayList<UserData>();
			if (sortby.isEmpty()) {
				userData = repo.findByName(name);
			} else {
				userData = repo.findByName(name, paging);
			}
			if (userData.isEmpty()) {
				logger.warn("No data existing for this query on filterBy flow");
				throw new EmptyUserRepoException("No data existing for this query");
			}
			return userData;
		}
		Page<UserData> userData = repo.findAll(paging);
		if (userData.isEmpty()) {
			logger.warn("No data existing for this query on filterBy flow");
			throw new EmptyUserRepoException("No data existing for this query");
		}
		return userData.toList();
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
