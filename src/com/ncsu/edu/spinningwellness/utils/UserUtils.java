package com.ncsu.edu.spinningwellness.utils;

import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class UserUtils {

	public static Entity getSingleUser(String name) {
		Key key = KeyFactory.createKey("User", name);
		return Utils.findEntity(key);
	}
	
	public static Entity getSingleUserActivity(String id, String name) {

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("UserActivity");
		
		Filter rideIdFilter = new Query.FilterPredicate("rideId", FilterOperator.EQUAL, KeyFactory.createKey("Ride", id));
		Filter userNameFilter = new Query.FilterPredicate("userName", FilterOperator.EQUAL, KeyFactory.createKey("User", name));
		Filter userActivityFilter = CompositeFilterOperator.and(rideIdFilter, userNameFilter);
		query.setFilter(userActivityFilter);

		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());
		if(results.size() == 1) {
			return results.get(0);
		} else {
			return null;
		}
	}
}