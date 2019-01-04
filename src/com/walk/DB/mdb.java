package com.walk.DB;

import java.sql.Connection;
import java.sql.DriverManager;

public class mdb {
	public static Connection getconntion()
	{
		String dbURL = "jdbc:mysql://172.17.144.10:3306/walk?characterEncoding=utf8";
		String userName = "root";      
		String userPwd = "123456";
		Connection dbConn = null;		
	    try 
		{
		   Class.forName("com.mysql.jdbc.Driver");  
		   dbConn = DriverManager.getConnection(dbURL,userName,userPwd);
		}catch (Exception e){
		      e.printStackTrace();
		}
		return dbConn;
	}
}
