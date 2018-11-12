package com.walk.pojo;

public class Gps extends UserInfor{
	int userID;
	String Longititude;
	String Latitude;
	String Direction;
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getLongititude() {
		return Longititude;
	}
	public void setLongititude(String longititude) {
		Longititude = longititude;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getDirection() {
		return Direction;
	}
	public void setDirection(String direction) {
		Direction = direction;
	}
	
}
