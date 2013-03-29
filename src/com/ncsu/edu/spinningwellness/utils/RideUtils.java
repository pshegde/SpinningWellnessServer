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
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class RideUtils {

	public static Entity getSingleRide(String id) {
		Key key = KeyFactory.createKey("Ride", id);
		return Utils.findEntity(key);
	}
	
	public static Entity getSingleParticipant(String id, String name) {

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("Participant");
		
		Filter rideIdFilter = new Query.FilterPredicate("rideId", FilterOperator.EQUAL, KeyFactory.createKey("Ride", id));
		Filter userNameFilter = new Query.FilterPredicate("userName", FilterOperator.EQUAL, KeyFactory.createKey("User", name));
		Filter participantFilter = CompositeFilterOperator.and(rideIdFilter, userNameFilter);
		query.setFilter(participantFilter);

		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());
		if(results.size() == 1) {
			return results.get(0);
		} else {
			return null;
		}
	}
	
	public static List<String> getAllParticipantsByRideId(String id) {

		List<String> users = new ArrayList<String>();

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("Participant");
		Filter rideIdFilter = new Query.FilterPredicate("rideId", FilterOperator.EQUAL, KeyFactory.createKey("Ride", id));
		query.setFilter(rideIdFilter);
		
		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());
		for(Entity result: results) {
			users.add(((Key) result.getProperty("userName")).getName());
		}
		return users;
	}
	
	public static List<String> getAllParticipantsByUserName(String name) {

		List<String> rideIDs = new ArrayList<String>();

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("Participant");
		Filter rideIdFilter = new Query.FilterPredicate("userName", FilterOperator.EQUAL, KeyFactory.createKey("User", name));
		query.setFilter(rideIdFilter);
		
		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());
		for(Entity result: results) {
			rideIDs.add(((Key) result.getProperty("rideId")).getName());
		}
		return rideIDs;
	}
}