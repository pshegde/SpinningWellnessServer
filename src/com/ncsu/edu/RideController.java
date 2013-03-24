package com.ncsu.edu;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.owlike.genson.TransformationException;

@Path("/l2w/")
public class RideController {

	/*- create user

- Create a ride
- update ride
- delete ride
- View past rides
- View upcoming rides

- Add participant
- delete participant

- Log userActivity
- View your past activity
- View personal best for the week
- Total calories burned and total distance covered in last week
- FInd top 3 performers of the week
*/
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/createride") 
	public String  createRide(Ride ride){
		
		return "Success";
	}
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/updateride/{id}") 
	public String  updateRide(@PathParam("id") String id, Ride ride){
		System.out.println("***id" + id);
		return "Success";
	}
	
	@DELETE
    @Path("/deleteride/{id}/")
    public String deleteCustomer(@PathParam("id") String id) {
        return "Success";
    }
		
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/allrides") 
	public List<Ride>  getRides() throws IOException, TransformationException {
		List<Ride> list = new ArrayList<Ride>();
		list.add(new Ride("Ride to Durham","Champion Ct","office"));
        return list;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/pastrides") 
	public List<Ride>  getPastRides() throws IOException, TransformationException {
		List<Ride> list = new ArrayList<Ride>();
		list.add(new Ride("Ride to Durham","Champion Ct","office"));
		list.add(new Ride("Ride to Pune","Kothrud","nalstop"));
        return list;
		//System.out.println(s);
		//return s;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/upcomingrides") 
	public List<Ride>  getUpcomingRides() throws IOException, TransformationException {
		List<Ride> list = new ArrayList<Ride>();
		list.add(new Ride("Ride to Durham","Champion Ct","office"));
		list.add(new Ride("Ride to Pune","Kothrud","nalstop"));
        return list;
	}

	//Add participant
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/addusertoride/{userid}/{rideid}") 
	public String  addParticipantToRide(@PathParam("userid") String userid, @PathParam("rideid") String rideid){
		System.out.println("***rideid" + rideid );
		System.out.println("***userid" + userid );
		return "Success";
	}
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/removeuserfromride/{userid}/{rideid}") 
	public String  removeParticipantFromRide(@PathParam("userid") String userid, @PathParam("rideid") String rideid){
		System.out.println("***rideid" + rideid );
		System.out.println("***userid" + userid );
		return "Success";
	}
	
	//Log userActivity
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/adduseractivity/{userid}") 
	public String  removeParticipantFromRide(@PathParam("userid") String userid,UserActivity act){
		System.out.println("***userid" + userid );
		return "Success";
	}
	
	//View your past activity
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/userpastactivity/{userid}") 
	public List<UserActivity>  getPastUserActivity(@PathParam("userid") String userid) throws IOException, TransformationException {
		List<UserActivity> list = new ArrayList<UserActivity>();
		list.add(new UserActivity());
		list.add(new UserActivity());
        return list;
	}
	
	//View personal best ride for the week
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/mybestride/{userid}") 
	public Ride  getPersonalBestRide(@PathParam("userid") String userid) throws IOException, TransformationException {
		Ride r = new Ride("Ride to Durham","Champion Ct","office");
        return r;
	}

	//Total calories burned and total distance covered in last week
	//FInd top 3 performers of the week
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/topPerformers") 
	public List<User>  getTopPerformers() throws IOException, TransformationException {
		List<User> list = new ArrayList<User>();
		list.add(new User());
		list.add(new User());
        return list;
	}
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/adduser") 
	public String createUser(User user){
		
		return "Success";
	}
	
}
