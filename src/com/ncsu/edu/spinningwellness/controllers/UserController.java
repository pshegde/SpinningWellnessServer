package com.ncsu.edu.spinningwellness.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.GET;

import javax.ws.rs.Consumes;
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
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.ncsu.edu.spinningwellness.entities.Ride;
import com.ncsu.edu.spinningwellness.entities.User;
import com.ncsu.edu.spinningwellness.entities.UserActivity;
import com.ncsu.edu.spinningwellness.utils.RideUtils;
import com.ncsu.edu.spinningwellness.utils.UserUtils;
import com.ncsu.edu.spinningwellness.utils.Utils;

@Path("/l2wuser/")
public class UserController {

	/**
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

	/**
	 * Creates a user in datastore.
	 * Before creating the user, a check is added to make sure that the user with same name is not present in the datastore already.
	 *
	 * @param  	ride 	the user object which is to be persisted in the datastore.
	 * 
	 * @return			a string stating the status of the operation either success or failure with appropriate message.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/createuser") 
	public String createUser(User user){

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

	/**
	 * Deletes the user from data store.
	 * Before deleting the user from database, the method deletes all the entries from participants table for that user.
	 *
	 * @param  	id  	id of the ride which is to be deleted.
	 * 
	 * @return			a string stating the status of the operation either success or failure with appropriate message.
	 */
	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/deleteuser/{name}/")
	public String deleteUser(@PathParam("name") String name) {

		Entity persistedUser = UserUtils.getSingleUser(name);
		if(persistedUser == null) {
			return "Failure: User does not exist";
		} else {

			//TODO: Delete all the rides for which this user was the creator
			
			//Deleting all the participant entries with this user name
			RideController rc = new RideController();
			List<String> rideIDs = RideUtils.getAllParticipantsByUserName(name);
			for(String rideId : rideIDs) {
				rc.removeParticipantFromRide(rideId, name);
			}

			//TODO: Delete all the user activity logs with this user name

			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			Key userKey = KeyFactory.createKey("User", name);
			ds.delete(userKey);

			return "Success";
		}
	}

	/**
	 * Logs user activity information in the database.
	 *
	 * @param  	activity  	UserActivity object which is to be persisted in the database.
	 * 
	 * @return				a string stating the status of the operation either success or failure with appropriate message.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/loguseractivity") 
	public String logUserActivity(UserActivity activity) {

		Entity persistedActivity = UserUtils.getSingleUserActivity(activity.getRideId(), activity.getUserName());
		if(persistedActivity != null) {
			return "Failure: Duplicate activity";
		} else {

			Entity persistedUser = UserUtils.getSingleUser(activity.getUserName());
			if(persistedUser != null) {			

				Entity persistedRide = UserUtils.getSingleUser(activity.getRideId());
				if(persistedRide != null) {

					DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

					Entity dbUserActicity = new Entity("UserActivity", activity.getId());
					dbUserActicity.setProperty("id", activity.getId());
					dbUserActicity.setProperty("rideId", persistedRide.getKey());
					dbUserActicity.setProperty("userName", persistedUser.getKey());
					dbUserActicity.setProperty("distanceCovered", activity.getDistaceCovered());
					dbUserActicity.setProperty("caloriesBurned", activity.getCaloriesBurned());
					dbUserActicity.setProperty("cadence", activity.getCadence());
					dbUserActicity.setProperty("averageSpeed", activity.getAverageSpeed());
					dbUserActicity.setProperty("activityDate", activity.getActivityDate());
					ds.put(dbUserActicity);

					return "Success";
				} else {
					return "Failure: Ride does not exist";
				}
			} else {
				return "Failure: User does not exist";	
			}
		}
	}

	/**
	 * Returns all the activities for a user from past week
	 *
	 * @param  	name  	name of the user for which all the past week activities are requested
	 * 
	 * @return			list of user activities from past week.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/viewpastactivityforlastweek/{name}") 
	public List<UserActivity> viewPastUserActivityForLastWeek(@PathParam("name") String name) {

		List<UserActivity> userActivities = new ArrayList<UserActivity>();

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("UserActivity");

		//filter for start date
		Date today = new Date();	  
		Filter activityDateFilterLessThan = new Query.FilterPredicate("activityDate", FilterOperator.LESS_THAN, today);

		//filter for end date
		Calendar cal = Calendar.getInstance();  
		cal.setTime(today);  
		cal.add(Calendar.DATE, -7);  
		today = cal.getTime();  		
		Filter activityDateFilterGreaterThan = new Query.FilterPredicate("activityDate", FilterOperator.GREATER_THAN, today);

		//Filter for user name
		Filter activityUserFilter = new Query.FilterPredicate("userName", FilterOperator.EQUAL, KeyFactory.createKey("User", name));

		//complete composite filter
		Filter activityDateRangeFilter = CompositeFilterOperator.and(activityDateFilterLessThan, activityDateFilterGreaterThan, activityUserFilter);
		query.setFilter(activityDateRangeFilter);

		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());
		for(Entity result: results) {

			String id = (String) result.getProperty("id");
			String rideId = ((Key) result.getProperty("rideId")).getName();
			String userName = ((Key) result.getProperty("userName")).getName();
			double distanceCovered = (Double) result.getProperty("distanceCovered");
			double caloriesBurned = (Double) result.getProperty("caloriesBurned");
			double cadence = (Double) result.getProperty("cadence");
			double averageSpeed = (Double) result.getProperty("averageSpeed");
			long activityDate = (Long) result.getProperty("activityDate");

			UserActivity ua = new UserActivity(id, rideId, userName, distanceCovered, cadence, averageSpeed, caloriesBurned, activityDate);
			userActivities.add(ua);
		}
		return userActivities;		
	}

	/**
	 * Returns all activities for a user.
	 *
	 * @param  	name  	name of the user for which activities are requested
	 * 
	 * @return			list of user activities from past week.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/viewpastactivity/{name}") 
	public List<UserActivity> viewPastUserActivity(@PathParam("name") String name) {

		List<UserActivity> userActivities = new ArrayList<UserActivity>();

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("UserActivity");

		//Filter for end date
		Date today = new Date();
		Filter activityDateFilter = new Query.FilterPredicate("activityDate", FilterOperator.LESS_THAN, today);

		//Filter for user name
		Filter activityUserFilter = new Query.FilterPredicate("userName", FilterOperator.EQUAL, KeyFactory.createKey("User", name));

		//complete composite filter
		Filter activityFilter = CompositeFilterOperator.and(activityDateFilter, activityUserFilter);
		query.setFilter(activityFilter);

		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());
		for(Entity result: results) {

			String id = (String) result.getProperty("id");
			String rideId = ((Key) result.getProperty("rideId")).getName();
			String userName = ((Key) result.getProperty("userName")).getName();
			double distanceCovered = (Double) result.getProperty("distanceCovered");
			double caloriesBurned = (Double) result.getProperty("caloriesBurned");
			double cadence = (Double) result.getProperty("cadence");
			double averageSpeed = (Double) result.getProperty("averageSpeed");
			long activityDate = (Long) result.getProperty("activityDate");

			UserActivity ua = new UserActivity(id, rideId, userName, distanceCovered, cadence, averageSpeed, caloriesBurned, activityDate);
			userActivities.add(ua);
		}
		return userActivities;
	}

	/**
	 * Returns workout details for a user like totalCaloriesBurned and totalDistanceCovered for the past week
	 *
	 * @param  	name  	name of the user for which workout details are requested
	 * 
	 * @return			a map of workout details and their values
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/workoutdetailsforlastweek/{name}") 
	public Map<String, Double> getWorkoutDetailsForLastWeek(@PathParam("name") String name) {
		Map<String, Double> workoutDetails = new HashMap<String, Double>();

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("UserActivity");

		//Filter for end date
		Date today = new Date();	  
		Filter activityDateFilterLessThan = new Query.FilterPredicate("activityDate", FilterOperator.LESS_THAN, today);

		//Filter for start date
		Calendar cal = Calendar.getInstance();  
		cal.setTime(today);  
		cal.add(Calendar.DATE, -7);  
		today = cal.getTime();  		
		Filter activityDateFilterGreaterThan = new Query.FilterPredicate("activityDate", FilterOperator.GREATER_THAN, today);

		//Filter for user name
		Filter activityUserFilter = new Query.FilterPredicate("userName", FilterOperator.EQUAL, KeyFactory.createKey("User", name));

		//complete composite filter
		Filter activityFilter = CompositeFilterOperator.and(activityDateFilterLessThan, activityDateFilterGreaterThan, activityUserFilter);
		query.setFilter(activityFilter);

		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());
		Double totalDistanceCovered = 0.0, totalCaloriesBurned = 0.0;
		for(Entity result: results) {

			totalDistanceCovered += (Double) result.getProperty("distanceCovered");
			totalCaloriesBurned += (Double) result.getProperty("caloriesBurned");
		}

		workoutDetails.put("TotalDistanceCovered", totalDistanceCovered);
		workoutDetails.put("TotalCaloriesBurned", totalCaloriesBurned);

		return workoutDetails;
	}

	/**
	 * Returns workout details for a user like totalCaloriesBurned and totalDistanceCovered
	 *
	 * @param  	name  	name of the user for which workout details are requested
	 * 
	 * @return			a map of workout details and their values
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/workoutdetails/{name}") 
	public Map<String, Double>  getWorkoutDetails(@PathParam("name") String name) {
		Map<String, Double> workoutDetails = new HashMap<String, Double>();

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("UserActivity");

		//Filter for end date
		Date today = new Date();	  
		Filter activityDateFilterLessThan = new Query.FilterPredicate("activityDate", FilterOperator.LESS_THAN, today);		

		//Filter for user name
		Filter activityUserFilter = new Query.FilterPredicate("userName", FilterOperator.EQUAL, KeyFactory.createKey("User", name));

		//complete composite filter
		Filter activityFilter = CompositeFilterOperator.and(activityDateFilterLessThan, activityUserFilter);
		query.setFilter(activityFilter);

		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());
		Double totalDistanceCovered = 0.0, totalCaloriesBurned = 0.0;
		for(Entity result: results) {

			totalDistanceCovered += (Double) result.getProperty("distanceCovered");
			totalCaloriesBurned += (Double) result.getProperty("caloriesBurned");
		}

		workoutDetails.put("TotalDistanceCovered", totalDistanceCovered);
		workoutDetails.put("TotalCaloriesBurned", totalCaloriesBurned);

		return workoutDetails;
	}

	/**
	 * Returns the ride for a user in from past week in which maximum distance was covered.
	 *
	 * @param  	name  	name of the user for which the best ride is requested
	 * 
	 * @return			the best ride for user from past week.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/mybestrideforlastweek/{name}") 
	public Ride getPersonalBestRideForLastWeek(@PathParam("name") String name) {
		Ride r = null;

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("UserActivity");

		//End date filter
		Date today = new Date();	  
		Filter activityDateFilterLessThan = new Query.FilterPredicate("activityDate", FilterOperator.LESS_THAN, today);

		//Start date filter
		Calendar cal = Calendar.getInstance();  
		cal.setTime(today);  
		cal.add(Calendar.DATE, -7);  
		today = cal.getTime();  		
		Filter activityDateFilterGreaterThan = new Query.FilterPredicate("activityDate", FilterOperator.GREATER_THAN, today);

		//User name filter
		Filter activityUserFilter = new Query.FilterPredicate("userName", FilterOperator.EQUAL, KeyFactory.createKey("User", name));

		//complete composite filter
		Filter activityFilter = CompositeFilterOperator.and(activityDateFilterLessThan, activityDateFilterGreaterThan, activityUserFilter);
		query.setFilter(activityFilter);

		//Setting the soring order
		query.addSort("distanceCovered", SortDirection.DESCENDING);

		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());		
		if(results.size()>0) {			
			String rideId = (String) results.get(0).getProperty("rideId");
			RideController rc = new RideController();
			r = rc.viewRide(rideId);
		}		
		return r;
	}

	/**
	 * Returns the ride for a user in which maximum distance was covered.
	 *
	 * @param  	name  	name of the user for which the best ride is requested.
	 * 
	 * @return			the best ride for user.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/mybestride/{name}") 
	public Ride getPersonalBestRide(@PathParam("name") String name) {
		Ride r = null;

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("UserActivity");

		//End date filter
		Date today = new Date();	  
		Filter activityDateFilterLessThan = new Query.FilterPredicate("activityDate", FilterOperator.LESS_THAN, today);

		//User filter
		Filter activityUserFilter = new Query.FilterPredicate("userName", FilterOperator.EQUAL, KeyFactory.createKey("User", name));

		//complete composite filter
		Filter activityFilter = CompositeFilterOperator.and(activityDateFilterLessThan, activityUserFilter);
		query.setFilter(activityFilter);

		//Setting the sort order
		query.addSort("distanceCovered", SortDirection.DESCENDING);

		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());		
		if(results.size()>0) {			
			String rideId = (String) results.get(0).getProperty("rideId");
			RideController rc = new RideController();
			r = rc.viewRide(rideId);
		}		
		return r;
	}

	/**
	 * Returns all the past rides for a user from last week.
	 *
	 * @param  	name  	name of the user for past rides are requested.
	 * 
	 * @return			list of past rides for a user from past week.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/mypastridesfromlastweek/{name}") 
	public List<Ride> getMyPastRidesFromLastWeek(@PathParam("name") String name) {
		List<Ride> rides = new ArrayList<Ride>();

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("UserActivity");

		//End date filter
		Date today = new Date();	  
		Filter activityDateFilterLessThan = new Query.FilterPredicate("activityDate", FilterOperator.LESS_THAN, today);

		//Start date filter
		Calendar cal = Calendar.getInstance();  
		cal.setTime(today);  
		cal.add(Calendar.DATE, -7);  
		today = cal.getTime();  		
		Filter activityDateFilterGreaterThan = new Query.FilterPredicate("activityDate", FilterOperator.GREATER_THAN, today);

		//user name filter
		Filter activityUserFilter = new Query.FilterPredicate("userName", FilterOperator.EQUAL, KeyFactory.createKey("User", name));

		//complete composite filter
		Filter activityFilter = CompositeFilterOperator.and(activityDateFilterLessThan, activityDateFilterGreaterThan, activityUserFilter);
		query.setFilter(activityFilter);

		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());		
		for(Entity result: results) {	
			String rideId = (String) result.getProperty("rideId");
			RideController rc = new RideController();
			rides.add(rc.viewRide(rideId));
		}		
		return rides;
	}

	/**
	 * Returns all the past rides for a user.
	 *
	 * @param  	name  	name of the user for past rides are requested.
	 * 
	 * @return			list of past rides for a user.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/mypastrides/{name}") 
	public List<Ride> getMyPastRides(@PathParam("name") String name) {
		List<Ride> rides = new ArrayList<Ride>();

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("UserActivity");

		//End date filter
		Date today = new Date();	  
		Filter activityDateFilterLessThan = new Query.FilterPredicate("activityDate", FilterOperator.LESS_THAN, today);		

		//User name filter
		Filter activityUserFilter = new Query.FilterPredicate("userName", FilterOperator.EQUAL, KeyFactory.createKey("User", name));

		//complete composite filter
		Filter activityFilter = CompositeFilterOperator.and(activityDateFilterLessThan, activityUserFilter);
		query.setFilter(activityFilter);

		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());		
		for(Entity result: results) {	
			String rideId = (String) result.getProperty("rideId");
			RideController rc = new RideController();
			rides.add(rc.viewRide(rideId));
		}		
		return rides;
	}

	/**
	 * Returns a list of 3 users who covered maximum distance in past week rides.
	 *
	 * @return			list of users who covered maximum distance in past week rides.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/topPerformersforlastweek") 
	public List<User> getTopPerformersForLastWeek() {
		List<User> users = new ArrayList<User>();

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("UserActivity");

		Date today = new Date();	  
		Filter activityDateFilterLessThan = new Query.FilterPredicate("activityDate", FilterOperator.LESS_THAN, today);

		Calendar cal = Calendar.getInstance();  
		cal.setTime(today);  
		cal.add(Calendar.DATE, -7);  
		today = cal.getTime();  		
		Filter activityDateFilterGreaterThan = new Query.FilterPredicate("activityDate", FilterOperator.GREATER_THAN, today);

		Filter activityDateRangeFilter = CompositeFilterOperator.and(activityDateFilterLessThan, activityDateFilterGreaterThan);
		query.setFilter(activityDateRangeFilter);

		Map<String, Double> userDistanceCovered = new HashMap<String, Double>();
		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());
		for(Entity result: results) {

			String userName = ((Key) result.getProperty("userName")).getName();
			Double distanceCovered = (Double) result.getProperty("distanceCovered");
			userDistanceCovered.put(userName, (Double) ((Double) (userDistanceCovered.get(userName)) + distanceCovered));
		}

		Map<String, Double> sortedMap = Utils.sortMapOnValues(userDistanceCovered);

		List list = new LinkedList(sortedMap.entrySet());		
		for (int i=list.size()-1; i>=list.size()-3; i--) {
			Map.Entry entry = (Entry) list.get(i);
			users.add(new User((String) entry.getKey()));
		}

		return users;		
	}

	/**
	 * Returns a list of 3 users who covered maximum distance overall.
	 *
	 * @return			list of users who covered maximum distance overall.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/topPerformers") 
	public List<User> getTopPerformers() {
		List<User> users = new ArrayList<User>();

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query query = new Query("UserActivity");

		Date today = new Date();	  
		Filter activityDateFilterLessThan = new Query.FilterPredicate("activityDate", FilterOperator.LESS_THAN, today);
		query.setFilter(activityDateFilterLessThan);

		Map<String, Double> userDistanceCovered = new HashMap<String, Double>();
		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());
		for(Entity result: results) {

			String userName = ((Key) result.getProperty("userName")).getName();
			Double distanceCovered = (Double) result.getProperty("distanceCovered");
			userDistanceCovered.put(userName, (Double) ((Double) (userDistanceCovered.get(userName)) + distanceCovered));
		}

		Map<String, Double> sortedMap = Utils.sortMapOnValues(userDistanceCovered);

		List list = new LinkedList(sortedMap.entrySet());		
		for (int i=list.size()-1; i>=list.size()-3; i--) {
			Map.Entry entry = (Entry) list.get(i);
			users.add(new User((String) entry.getKey()));
		}
		
		return users;		
	}	
}