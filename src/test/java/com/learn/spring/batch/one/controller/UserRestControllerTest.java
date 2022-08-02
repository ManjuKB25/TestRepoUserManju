package com.learn.spring.batch.one.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.learn.spring.batch.one.entity.UserData;
import com.learn.spring.batch.one.service.UserService;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class UserRestControllerTest {
	@InjectMocks
	UserRestController userRestController;
	@Mock
	UserService mockUserService;

	@Test
	public void testGetAllUsers() {
		List<UserData> users = getTestUsers(5);
		when(mockUserService.getAllUsers()).thenReturn(users);
		List<UserData> usersList = userRestController.getAllUsers();
		assertEquals(5, usersList.size());

		users = getTestUsers(0);
		when(mockUserService.getAllUsers()).thenReturn(users);
		usersList = userRestController.getAllUsers();
		assertEquals(0, usersList.size());

	}

	@Test
	public void testGetUserById() {
		List<UserData> users = getTestUsers(5);
		when(mockUserService.getUserById(any(String.class))).thenReturn(users.get(0));
//		UserData usersResult = userRestController.getUserById("100");
//		assertEquals(100, usersResult.getId());
//		assertEquals("User0", usersResult.getName());
//		assertEquals("User 0 Address 1", usersResult.getAddressone());
//		assertEquals("User 0 Address 2", usersResult.getAddresstwo());
	}

	private List<UserData> getTestUsers(int usersCount) {
		List<UserData> users = new ArrayList<UserData>();
		for (int i = 0; i < usersCount; i++) {
			UserData user1 = new UserData();
			user1.setId(Integer.parseInt("10" + i));
			user1.setName("User" + i);
			user1.setAge(new Random().nextInt(15, 70));
			user1.setAddressone("User " + i + " Address 1");
			user1.setAddresstwo("User " + i + " Address 2");
			users.add(user1);
		}
		return users;
	}
}
