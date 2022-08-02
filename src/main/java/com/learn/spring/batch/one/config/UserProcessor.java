package com.learn.spring.batch.one.config;

import org.springframework.batch.item.ItemProcessor;

import com.learn.spring.batch.one.entity.UserData;

public class UserProcessor implements ItemProcessor<UserData, UserData>{

	@Override
	public UserData process(UserData item) throws Exception {
		return item;
	}

}
