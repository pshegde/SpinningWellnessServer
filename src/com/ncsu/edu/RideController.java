package com.ncsu.edu;


import java.io.IOException;
import org.codehaus.jettison.json.JSONArray;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.owlike.genson.Genson;
import com.owlike.genson.TransformationException;

@Path("/l2w/")
public class RideController {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/rides") 
	public String  getRides() throws IOException, TransformationException {
		List<Ride> list = new ArrayList<Ride>();
		Ride r = new Ride("Ride to Durham","Champion Ct","office");
//		Genson gson = new Genson();
//		String s = gson.serialize(r);
//		 JSONArray myData = new JSONArray();
//
//        for (int x = 0; x < 12; x++) {
//            myData.put("This is a test entry"+x);
//        }

        return "prajakta";
		//System.out.println(s);
		//return s;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/pastride") 
	public JSONArray  getRide() throws IOException, TransformationException {
//		List<Ride> list = new ArrayList<Ride>();
//		Ride r = new Ride("Ride to Durham","Champion Ct","office");
//		Genson gson = new Genson();
//		String s = gson.serialize(r);
		JSONArray myData = new JSONArray();

        for (int x = 0; x < 12; x++) {
            myData.put("This is a test entry"+x);
        }

        return myData;
		//System.out.println(s);
		//return s;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/myride") 
	public Ride  getMyRide() throws IOException, TransformationException {
//		List<Ride> list = new ArrayList<Ride>();
		Ride r = new Ride("Ride to Durham","Champion Ct","office");
//		Genson gson = new Genson();
//		String s = gson.serialize(r);
		
        //return myData;
		//System.out.println(s);
		return r;
	}
}
