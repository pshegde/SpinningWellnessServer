package com.ncsu.edu.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.ncsu.edu.entities.Ride;

@Path("/l2w/")
public class RideController {

	/*
	 * create ride
	 * update ride
	 * delete ride
	 * view ride
	 * 
	 * view past rides from last week
	 * view past rides
	 * 
	 * view upcoming rides from next week
	 * view upcoming rides
	 * 
	 * add participant to the ride
	 * delete participant to the ride
	 * 
	 */

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/createride") 
	public String createRide(Ride ride){
		
		//TODO: How to check for unique ID ??

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		Entity dbRide = new Entity("Ride", ride.getId());
		dbRide.setProperty("id", ride.getId());
		dbRide.setProperty("name", ride.getName());
		dbRide.setProperty("source", ride.getSource());
		dbRide.setProperty("destination", ride.getDest());
		dbRide.setProperty("creator", ride.getCreator());
		dbRide.setProperty("startTime", ride.getStartTime());

		ds.put(dbRide);

		return "Success";
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/updateride/{id}") 
	public String updateRide(@PathParam("id") String id, Ride ride){

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		Entity dbRide = new Entity("Ride", ride.getId());
		dbRide.setProperty("id", ride.getId());
		dbRide.setProperty("name", ride.getName());
		dbRide.setProperty("source", ride.getSource());
		dbRide.setProperty("destination", ride.getDest());
		dbRide.setProperty("creator", ride.getCreator());
		dbRide.setProperty("startTime", ride.getStartTime());

		ds.put(dbRide);

		return "Success";
	}

	@DELETE
	@Path("/deleteride/{id}/")
	public String deleteRide(@PathParam("id") String id) {

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Key rideKey = KeyFactory.createKey("Ride", id);
		ds.delete(rideKey);

		return "Success";
	}

	@GET
	@Path("/viewride/{id}/")
	public Ride viewRide(@PathParam("id") String id) {

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Key rideKey = KeyFactory.createKey("Ride", id);
		try {
			Entity dbRide = ds.get(rideKey);

			String name = (String) dbRide.getProperty("name");
			String source = (String) dbRide.getProperty("source");
			String dest = (String) dbRide.getProperty("destination");
			String creator = (String) dbRide.getProperty("creator");
			Date startTime = (Date) dbRide.getProperty("startTime");

			Ride ride = new Ride(id, name, source, dest, startTime, creator);
			return ride;
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/viewpastridesfromlastweek") 
	public List<Ride> viewPastRidesFromLastWeek() {
		List<Ride> list = new ArrayList<Ride>();
		return list;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/viewpastrides") 
	public List<Ride> viewPastRides() {

		List<Ride> rides = new ArrayList<Ride>();

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Date today = new Date();

		Query query = new Query("Ride");
		Filter startTimeFilter =
				new Query.FilterPredicate("startTime",
						FilterOperator.LESS_THAN,
						today);

		query.setFilter(startTimeFilter);
		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());

		for(Entity result : results) {

			String id = (String) result.getProperty("id");
			String name = (String) result.getProperty("name");
			String source = (String) result.getProperty("source");
			String dest = (String) result.getProperty("destination");
			String creator = (String) result.getProperty("creator");
			Date startTime = (Date) result.getProperty("startTime");

			Ride r = new Ride(id, name, source, dest, startTime, creator);
			rides.add(r);
		}
		return rides;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/viewupcomingridesfromnextweek") 
	public List<Ride> viewUpcomingRidesFromNextWeek() {
		List<Ride> list = new ArrayList<Ride>();
		return list;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/viewupcomingrides") 
	public List<Ride> viewUpcomingRides() {
		List<Ride> rides = new ArrayList<Ride>();

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Date today = new Date();

		Query query = new Query("Ride");
		Filter startTimeFilter =
				new Query.FilterPredicate("startTime",
						FilterOperator.GREATER_THAN,
						today);

		query.setFilter(startTimeFilter);
		List<Entity> results = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());

		for(Entity result : results) {

			String id = (String) result.getProperty("id");
			String name = (String) result.getProperty("name");
			String source = (String) result.getProperty("source");
			String dest = (String) result.getProperty("destination");
			String creator = (String) result.getProperty("creator");
			Date startTime = (Date) result.getProperty("startTime");

			Ride r = new Ride(id, name, source, dest, startTime, creator);
			rides.add(r);
		}
		return rides;
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/addparticipanttoride/{id}/{name}") 
	public String addParticipantToRide(@PathParam("id") String rideId, @PathParam("name") String name){
		return "Success";
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/removeparticipanttoride/{id}/{name}") 
	public String removeParticipantFromRide(@PathParam("id") String rideId, @PathParam("name") String name){
		return "Success";
	}
}