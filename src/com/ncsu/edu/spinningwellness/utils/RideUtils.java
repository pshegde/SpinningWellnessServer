package com.ncsu.edu.spinningwellness.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class RideUtils {

	public static Entity getSingleRide(String id) {
		Key key = KeyFactory.createKey("Ride", id);
		return Utils.findEntity(key);
	}
	
	public static Key getSingleParticipant(String id, String name) {

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("Participant");
		Filter rideIdFilter = new Query.FilterPredicate("rideId", FilterOperator.EQUAL, id);
		Filter userNameFilter = new Query.FilterPredicate("userName", FilterOperator.EQUAL, name);
		query.setFilter(rideIdFilter);
		query.setFilter(userNameFilter);
		
		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());

		if(results.size() == 1) {
			return results.get(0).getKey();
		} else {
			return null;
		}
	}
	
	public static List<String> getAllParticipantsByRideId(String id) {

		List<String> users = new ArrayList<String>();

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("Participant");
		Filter rideIdFilter = new Query.FilterPredicate("rideId", FilterOperator.EQUAL, id);
		query.setFilter(rideIdFilter);
		
		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());
		for(Entity result: results) {
			users.add((String) result.getProperty("userName"));
		}
		return users;
	}
	
	public static List<String> getAllParticipantsByUserName(String name) {

		List<String> users = new ArrayList<String>();

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("Participant");
		Filter rideIdFilter = new Query.FilterPredicate("userName", FilterOperator.EQUAL, name);
		query.setFilter(rideIdFilter);
		
		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());
		for(Entity result: results) {
			users.add((String) result.getProperty("rideId"));
		}
		return users;
	}
}