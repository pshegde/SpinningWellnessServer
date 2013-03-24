package com.ncsu.edu.controllers;

import java.util.ArrayList;
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

import com.ncsu.edu.entities.Ride;
import com.ncsu.edu.entities.User;
import com.ncsu.edu.entities.UserActivity;

@Path("/l2w/")
public class UserController {

	/*
	 * create user
	 * delete user
	 * log user activity 
	 * view past user activity for the last week
	 * view past user activity
	 * view workout details for the last week
	 * view work out details
	 * get personal best ride for the last week
	 * get personal best ride
	 * get top 3 performers for the last week
	 * get top 3 persormers
	 * 
	 */

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/createuser") 
	public String  createUser(User user){
		return "Success";
	}

	@DELETE
	@Path("/deleteuser/{name}/")
	public String deleteUser(@PathParam("name") String name) {
		return "Success";
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/loguseractivity/{name}") 
	public String logUserActivity(@PathParam("name") String name, UserActivity activity) {
		return "Success";
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
		List<UserActivity> list = new ArrayList<UserActivity>();
		return list;
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
