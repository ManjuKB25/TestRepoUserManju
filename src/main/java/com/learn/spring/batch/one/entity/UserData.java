package com.learn.spring.batch.one.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER_DATA")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO,generator="native")
	@GenericGenerator(name = "native",strategy = "native")
	@Column(name = "id")
	private long id;
	@Column(name = "name")
	private String name;
	@Column(name = "age")
	private int age;
	@Column(name = "addressone")
	private String addressone;
	@Column(name = "addresstwo")
	private String addresstwo;
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public int getAge() {
		return age;
	}
	public String getAddressone() {
		return addressone;
	}
	public String getAddresstwo() {
		return addresstwo;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public void setAddressone(String addressone) {
		this.addressone = addressone;
	}
	public void setAddresstwo(String addresstwo) {
		this.addresstwo = addresstwo;
	}
}
