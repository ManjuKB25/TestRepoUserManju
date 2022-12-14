package com.learn.spring.batch.one.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learn.spring.batch.one.entity.UserData;
import com.learn.spring.batch.one.exceptions.EmptyUserRepoException;
import com.learn.spring.batch.one.exceptions.InvalidNumericValueException;
import com.learn.spring.batch.one.exceptions.RequiredFieldsException;
import com.learn.spring.batch.one.exceptions.UserNotFoundException;
import com.learn.spring.batch.one.service.UserService;

@RestController
@RequestMapping("/api/V1.0")
public class UserRestController {

	@Autowired
	private UserService service;

	/*
	 * Get of Users. Limited to top 100 records, along with default sort order as
	 * DESC on 'id' field. Ex: /users or /users?sortType=asc
	 */
	@GetMapping("/users")
	public List<UserData> getAllUsers(@RequestParam(required = false, defaultValue = "DESC") String sortType) {
		return service.getAllUsers(sortType);
	}

	/*
	 * Get of specific user when valid userId is provided. Validations of userId is honored.
	 */
	@GetMapping("/users/{id}")
	public UserData getUserById(@PathVariable String id) {
		return service.getUserById(id);
	}

	/*
	 * Get of specific user when valid userId is provided. Validations of userId is honored.
	 */
	@GetMapping(value = { "/users/sortby" })
	public List<UserData> getUsersSortBy(@RequestParam(required = true) Map<String, String> pathVarsMap) {
		return service.getUsersSortBy(pathVarsMap);
	}

	@GetMapping(value = { "/users/filterByName/{name}", "/users/filterByName" })
	public List<UserData> getUsersFilterByName(@PathVariable(required = true) String name) {
		return service.getUsersFilterByName(name);
	}

	@GetMapping(value = { "/users/filterByAge/{age}", "/users/filterByAge" })
	public List<UserData> getUsersFilterByAge(@PathVariable(required = true) String age) {
		return service.getUsersFilterByAge(age);
	}

	@GetMapping(value = { "/users/paging", "/users/paging/{offset}", "/users/paging/{offset}/{size}",
			"/users/paging/{offset}/{size}/{field}" })
	public List<UserData> getUsersPaginatedData(@PathVariable(required = true) Map<String, String> pathVarsMap) {
		return service.getUsersPageData(pathVarsMap);
	}

	/* Exception Handling */
	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<String> UserNotFoundException(UserNotFoundException userNotFoundException) {
		return new ResponseEntity<String>(userNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = InvalidNumericValueException.class)
	public ResponseEntity<String> InvalidUserIDException(InvalidNumericValueException invalidUserIDException) {
		return new ResponseEntity<String>(invalidUserIDException.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = EmptyUserRepoException.class)
	public ResponseEntity<String> EmptyUserRepoException(EmptyUserRepoException emptyUserRepoException) {
		return new ResponseEntity<String>(emptyUserRepoException.getMessage(), HttpStatus.OK);
	}

	@ExceptionHandler(value = RequiredFieldsException.class)
	public ResponseEntity<String> RequiredFieldsException(RequiredFieldsException requiredFieldsException) {
		return new ResponseEntity<String>(requiredFieldsException.getMessage(), HttpStatus.EXPECTATION_FAILED);
	}

	@ExceptionHandler(value = MissingPathVariableException.class)
	public ResponseEntity<String> CustomeMissingPathVariableException() {
		return new ResponseEntity<String>("Required URI template variable missing", HttpStatus.EXPECTATION_FAILED);
	}

	/* End of Exception Handling */

}
