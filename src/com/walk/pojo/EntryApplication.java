package com.walk.pojo;

public class EntryApplication extends Group{
//	entryID int PRIMARY key AUTO_INCREMENT,
//	Date TIMESTAMP,
//	groupID int not null,
//	userID int not null,
//	status char(2),	
	int userID;
	int groupID;
	String Date;
	String status;
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getGroupID() {
		return groupID;
	}
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
