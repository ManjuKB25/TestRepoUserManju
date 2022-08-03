package com.learn.spring.batch.one.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learn.spring.batch.one.entity.UserData;
import com.learn.spring.batch.one.exceptions.EmptyUserRepoException;
import com.learn.spring.batch.one.exceptions.InvalidNumericValueException;
import com.learn.spring.batch.one.exceptions.RequiredFieldsException;
import com.learn.spring.batch.one.exceptions.UserNotFoundException;
import com.learn.spring.batch.one.service.UserService;

/*
 * Version 2 of User data with single mapping having filters and sort along with sort type as request params.
 *
 */
@RestController
@RequestMapping("/api/V2.0")
public class UserRestControllerV2 {
	@Autowired
	private UserService service;

	/* 
	 * supproted queries:
	 * /users -- List top 100 users sorted on id, desc
	 * /users?name=<name> -- List top 100 users filtered by name, sorted on id, desc
	 * /users?age=<age> -- List top 100 users filtered by age, sorted on id, desc
	 * /users?name=<name>&age=<age> -- List top 100 users filtered by name and age, sorted on id, desc
	 * /users?sortby=<field>&sortType=asc -- List top 100 users sorted on filed, asc
	 * etc.
	 * */
	@GetMapping("/users")
	public List<UserData> getUsersFilterBy(@RequestParam(required = true) Map<String, String> dataQuery) {
		return service.getUsersFilterBy(dataQuery);
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
