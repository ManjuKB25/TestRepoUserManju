package com.learn.spring.batch.one.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learn.spring.batch.one.entity.UserData;
import com.learn.spring.batch.one.exceptions.InvalidUserIDException;
import com.learn.spring.batch.one.exceptions.UserNotFoundException;
import com.learn.spring.batch.one.service.UserService;

import io.micrometer.core.annotation.Timed;

@RestController
@RequestMapping("/api/V1.0")
@Timed
public class UserRestController {

	@Autowired
	private UserService service;

	@GetMapping("/users")
	public List<UserData> getAllUsers() {
		return service.getAllUsers();
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<UserData> getUserById(@PathVariable String id) {
		return new ResponseEntity<UserData>(service.getUserById(id), HttpStatus.OK);
	}

	@GetMapping("/users/sortby/{field}")
	public List<UserData> getUsersSortBy(@PathVariable String field) {
		return service.getUsersSortBy(field);
	}

	@GetMapping("/users/filterByName/{name}")
	public List<UserData> getUsersFilterByName(@PathVariable String name) {
		return service.getUsersFilterByName(name);
	}

	@GetMapping("/users/filterByAge/{age}")
	public List<UserData> getUsersFilterByAge(@PathVariable String age) {
		return service.getUsersFilterByAge(Integer.parseInt(age));
	}

	@GetMapping("/users/filterBy")
	public List<UserData> getUsersFilterBy(@RequestParam Map<String, String> dataQuery) {
		return service.getUsersFilterBy(dataQuery);
	}

	@GetMapping("/users/paging/{offset}/{size}")
	public List<UserData> getUsersPaginatedData(@PathVariable int offset, @PathVariable int size) {
		return service.getUsersPaginatedData(offset, size);
	}

	@GetMapping("/users/paging/{offset}/{size}/{field}")
	public List<UserData> getUsersPaginatedDataAndSort(@PathVariable int offset, @PathVariable int size,
			@PathVariable String field) {
		return service.getUsersPaginatedDataAndSort(offset, size, field);
	}
	
	@ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<String> UserNotFoundException(UserNotFoundException userNotFoundException) {
        return new ResponseEntity<String>(userNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

	@ExceptionHandler(value = InvalidUserIDException.class)
    public ResponseEntity<String> InvalidUserIDException(InvalidUserIDException invalidUserIDException) {
        return new ResponseEntity<String>(invalidUserIDException.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
