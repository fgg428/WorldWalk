package com.walk.pojo;

public class Group extends UserInfor{
//	groupID int PRIMARY key AUTO_INCREMENT,
//	groupHostID int not null,
//	groupName varchar(20) not null,
//	groupPic VARCHAR(100),
//	groupAnnouncement VARCHAR(200),
//	groupDescribe VARCHAR(200),
//	status char(2) not null,
	int groupID;
	int groupHostID;
	String groupName;
	String groupPic;
	String groupAnnouncement;
	String groupDescribe;
	String status;
	public int getGroupID() {
		return groupID;
	}
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
	public int getGroupHostID() {
		return groupHostID;
	}
	public void setGroupHostID(int groupHostID) {
		this.groupHostID = groupHostID;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupPic() {
		return groupPic;
	}
	public void setGroupPic(String groupPic) {
		this.groupPic = groupPic;
	}
	public String getGroupAnnouncement() {
		return groupAnnouncement;
	}
	public void setGroupAnnouncement(String groupAnnouncement) {
		this.groupAnnouncement = groupAnnouncement;
	}
	public String getGroupDescribe() {
		return groupDescribe;
	}
	public void setGroupDescribe(String groupDescribe) {
		this.groupDescribe = groupDescribe;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
