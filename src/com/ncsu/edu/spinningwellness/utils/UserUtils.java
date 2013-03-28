package com.ncsu.edu.spinningwellness.utils;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class UserUtils {

	public static Entity getSingleUser(String name) {
		Key key = KeyFactory.createKey("User", name);
		return Utils.findEntity(key);
	}
	
	public static Entity getSingleUserActivity(String id) {
		Key key = KeyFactory.createKey("UserActivity", id);
		return Utils.findEntity(key);
	}
}