package com.learn.spring.batch.one.entity.validations;

import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.learn.spring.batch.one.entity.UserData;

public class CustomValidate {

	private static int maxAgeLimit = 200;

	public static boolean isValidField(String field) {
		return BeanUtils.getPropertyDescriptor(UserData.class, field) != null;
	}

	public static boolean isValidFields(Map<String, String> dataQuery) {
		if (dataQuery.size() == 0)
			return false;
		if (dataQuery.containsKey("sortby")) {
			String sortBy = dataQuery.get("sortby");
			if (BeanUtils.getPropertyDescriptor(UserData.class, sortBy) == null) {
				return false;
			}
		}
		for (String ele : dataQuery.keySet()) {
			if (!(ele.equals("sortby")) && !(ele.equals("sortType"))) {
				if (BeanUtils.getPropertyDescriptor(UserData.class, ele) == null) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean isValidField(Map<String, String> dataQuery) {
		if (dataQuery.size() == 0)
			return true;
		if (dataQuery.containsKey("sortby")) {
			String sortBy = dataQuery.get("sortby");
			if (BeanUtils.getPropertyDescriptor(UserData.class, sortBy) == null) {
				return false;
			}
		}
		for (String ele : dataQuery.keySet()) {
			if (!(ele.equals("sortby")) && !(ele.equals("sortType"))) {
				if (BeanUtils.getPropertyDescriptor(UserData.class, ele) == null) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean isValidUserId(String id) {
		try {
			Long.parseUnsignedLong(id);
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}

	public static boolean isVaildPagingOptions(Map<String, String> pathVarsMap) {
		if (pathVarsMap.size() < 2) {
			return false;
		}
		try {
			Integer.parseUnsignedInt(pathVarsMap.get("offset"));
			Integer.parseUnsignedInt(pathVarsMap.get("size"));
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}

	public static boolean isValidAge(String age) {
		int ageField = 0;
		try {
			ageField = Integer.parseUnsignedInt(age);
		} catch (NumberFormatException ex) {
			return false;
		}
		if (ageField > 0 && ageField < maxAgeLimit)
			return true;
		else
			return false;
	}

}
