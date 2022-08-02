package com.learn.spring.batch.one.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.learn.spring.batch.one.entity.UserData;

@Repository
public interface UserRepo extends JpaRepository<UserData, Long> {
	public List<UserData> findByAge(Integer age);

	@Query("SELECT u FROM UserData u WHERE "
			+ " u.name LIKE %?1%")
	public List<UserData> findByName(String name);

	@Query("SELECT u FROM UserData u WHERE "
			+ " u.name LIKE %?1%"
			+ " AND u.age = ?2")
	public List<UserData> findByNameAndAge(String name, Integer age);
}
