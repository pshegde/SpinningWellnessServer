package com.ncsu.edu.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserActivity {
	
	String name;
	String rideId;
	double distaceCovered;
	double cadence;
	double averageSpeed;
	double caloriesBurned;
	
	public UserActivity() {}

	public UserActivity(String name, String rideId, double distaceCovered,
			double cadence, double averageSpeed, double caloriesBurned) {
		super();
		this.name = name;
		this.rideId = rideId;
		this.distaceCovered = distaceCovered;
		this.cadence = cadence;
		this.averageSpeed = averageSpeed;
		this.caloriesBurned = caloriesBurned;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRideId() {
		return rideId;
	}

	public void setRideId(String rideId) {
		this.rideId = rideId;
	}

	public double getDistaceCovered() {
		return distaceCovered;
	}

	public void setDistaceCovered(double distaceCovered) {
		this.distaceCovered = distaceCovered;
	}

	public double getCadence() {
		return cadence;
	}

	public void setCadence(double cadence) {
		this.cadence = cadence;
	}

	public double getAverageSpeed() {
		return averageSpeed;
	}

	public void setAverageSpeed(double averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	public double getCaloriesBurned() {
		return caloriesBurned;
	}

	public void setCaloriesBurned(double caloriesBurned) {
		this.caloriesBurned = caloriesBurned;
	}
}