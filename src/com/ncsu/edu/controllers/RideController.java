package com.ncsu.edu.controllers;


import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ncsu.edu.entities.Ride;

@Path("/l2w/")
public class RideController {

	/*
	 * create ride
	 * update ride
	 * delete ride
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
		return "Success";
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/updateride/{id}") 
	public String updateRide(@PathParam("id") String id, Ride ride){
		return "Success";
	}

	@DELETE
	@Path("/deleteride/{id}/")
	public String deleteCustomer(@PathParam("id") String id) {
		return "Success";
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
		List<Ride> list = new ArrayList<Ride>();
		return list;
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
		List<Ride> list = new ArrayList<Ride>();
		return list;
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