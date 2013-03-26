package com.ncsu.edu.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.ncsu.edu.entities.Ride;
import com.ncsu.edu.entities.User;
import com.ncsu.edu.entities.UserActivity;
import com.ncsu.edu.utils.RideUtils;
import com.ncsu.edu.utils.UserUtils;

@Path("/l2w/")
public class UserController {

	/*
	 * create user
	 * delete user
	 * 
	 * log user activity 
	 * 
	 * view past user activity for the last week
	 * view past user activity
	 * 
	 * view workout details for the last week
	 * view work out details
	 * 
	 * get personal best ride for the last week
	 * get personal best ride
	 * 
	 * get top 3 performers for the last week
	 * get top 3 persormers
	 * 
	 */

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/createuser") 
	public String  createUser(User user){

		Entity persistedUser = UserUtils.getSingleUser(user.getName());
		if(persistedUser != null) {
			return "Failure: Duplicate user name";
		} else {
			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

			Entity dbUser = new Entity("User", user.getName());
			dbUser.setProperty("id", user.getName());
			ds.put(dbUser);

			return "Success";
		}
	}

	@DELETE
	@Path("/deleteuser/{name}/")
	public String deleteUser(@PathParam("name") String name) {
		
		Entity persistedUser = UserUtils.getSingleUser(name);
		if(persistedUser == null) {
			return "Failure: User does not exist";
		} else {

			//Deleting all the participant entries with this user name
			RideController rc = new RideController();
			List<String> rides = RideUtils.getAllParticipantsByUserName(name);
			for(String ride : rides) {
				rc.removeParticipantFromRide(ride, name);
			}
			
			//TODO: Delete all the user activity logs with this user name

			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			Key userKey = KeyFactory.createKey("User", name);
			ds.delete(userKey);

			return "Success";
		}
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/loguseractivity") 
	public String logUserActivity(UserActivity activity) {
		
		Entity persistedActivity = UserUtils.getSingleUserActivity(activity.getId());
		if(persistedActivity != null) {
			return "Failure: Duplicate user name";
		} else {
			
			Entity persistedUser = UserUtils.getSingleUser(activity.getUserName());
			if(persistedUser != null) {			
				DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

				Entity dbUserActicity = new Entity("UserActivity", activity.getId());
				dbUserActicity.setProperty("id", activity.getId());
				dbUserActicity.setProperty("userName", activity.getUserName());
				dbUserActicity.setProperty("distanceCovered", activity.getDistaceCovered());
				dbUserActicity.setProperty("caloriesBurned", activity.getCaloriesBurned());
				dbUserActicity.setProperty("cadence", activity.getCadence());
				dbUserActicity.setProperty("averageSpeed", activity.getAverageSpeed());
				dbUserActicity.setProperty("activityDate", activity.getActivityDate());
				ds.put(dbUserActicity);

				return "Success";				
			} else {
				return "Failure: User does not exist";	
			}
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/viewpastactivityforlastweek/{name}") 
	public List<UserActivity> viewPastUserActivityForLastWeek(@PathParam("name") String name) {
		List<UserActivity> list = new ArrayList<UserActivity>();
		return list;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/viewpastactivity/{name}") 
	public List<UserActivity> viewPastUserActivity(@PathParam("name") String name) {
		
		List<UserActivity> userActivities = new ArrayList<UserActivity>();
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("UserActivity");
		
		Date today = new Date();
		Filter activityDateFilter = new Query.FilterPredicate("activityDate", FilterOperator.LESS_THAN, today);
		query.setFilter(activityDateFilter);
		
		Filter activityUserFilter = new Query.FilterPredicate("userName", FilterOperator.EQUAL, name);
		query.setFilter(activityUserFilter);
		
		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());
		for(Entity result: results) {
			
			String id = (String) result.getProperty("id");
			String userName = (String) result.getProperty("userName");
			double distanceCovered = (Double) result.getProperty("distanceCovered");
			double caloriesBurned = (Double) result.getProperty("caloriesBurned");
			double cadence = (Double) result.getProperty("cadence");
			double averageSpeed = (Double) result.getProperty("averageSpeed");
			Date activityDate = (Date) result.getProperty("activityDate");
			
			UserActivity ua = new UserActivity(id, userName, distanceCovered, cadence, averageSpeed, caloriesBurned, activityDate);
			userActivities.add(ua);
		}
		return userActivities;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/workoutdetailsforlastweek/{name}") 
	public Map<String, Double>  getWorkoutDetailsForLastWeek(@PathParam("name") String userid) {
		Map<String, Double> workoutDetails = new HashMap<String, Double>();
		//Retrieve the workout details from datastore and put them in the map
		return workoutDetails;
	}

	@Path("/workoutdetails/{name}") 
	public Map<String, Double>  getWorkoutDetails(@PathParam("name") String userid) {
		Map<String, Double> workoutDetails = new HashMap<String, Double>();
		//Retrieve the workout details from datastore and put them in the map
		return workoutDetails;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/mybestrideforlastweek/{name}") 
	public Ride getPersonalBestRideForLastWeek(@PathParam("name") String userid) {
		Ride r = null;
		return r;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/mybestride/{name}") 
	public Ride getPersonalBestRide(@PathParam("name") String userid) {
		Ride r = null;
		return r;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/topPerformersforlastweek") 
	public List<User> getTopPerformersForLastWeek() {
		List<User> list = new ArrayList<User>();
		return list;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/topPerformers") 
	public List<User> getTopPerformers() {
		List<User> list = new ArrayList<User>();
		return list;
	}
}