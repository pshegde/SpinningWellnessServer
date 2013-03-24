package com.ncsu.edu.entities;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Ride {
	
	String name;
	String source;
	String dest;
	
	public Ride(){
		
	}
	
	public Ride(String name, String source, String dest) {
		super();
		this.name = name;
		this.source = source;
		this.dest = dest;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getDest() {
		return dest;
	}
	
	public void setDest(String dest) {
		this.dest = dest;
	}
}
