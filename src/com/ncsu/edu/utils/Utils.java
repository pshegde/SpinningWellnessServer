package com.ncsu.edu.utils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public class Utils {
	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	public static Entity findEntity(Key key) {
		try {
			return datastore.get(key);
		} catch (EntityNotFoundException e) {
			return null;
		}
	}
}
