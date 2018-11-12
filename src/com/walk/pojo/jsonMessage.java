package com.walk.pojo;

public class jsonMessage {
	String success;
	String message;
	String[] data=null;	
	public jsonMessage() {
		
	}
	public jsonMessage(String success,String message) {
		this.success=success;
		this.message=message;
	}
	public jsonMessage(String success,String message,String[] data) {
		this.success=success;
		this.message=message;
		this.data=data;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String[] getData() {
		return data;
	}
	public void setData(String[] data) {
		this.data = data;
	}
}
