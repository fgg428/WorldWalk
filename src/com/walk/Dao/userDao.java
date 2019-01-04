package com.walk.Dao;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.walk.DB.mdb;
import com.walk.pojo.UserInfor;
import com.walk.pojo.jsonMessage;
import com.walk.pojo.main;

import net.sf.json.JSONArray;

public class userDao {
	//登陆
	public jsonMessage loginDao(String userName,String password) throws Exception{
		Connection conn=mdb.getconntion();//连接数据库
		String sql="select * from tblUserInfor where userName = '"+userName+"' and password='"+password+"'";
		java.sql.Statement stmt=conn.createStatement();
		ResultSet r=stmt.executeQuery(sql);
		jsonMessage jm=null;
		if(r.next()) {
			String[] infor=null;
			List<UserInfor> list=new ArrayList<UserInfor>();
			r.beforeFirst();
			while(r.next()) {
				UserInfor user=new UserInfor();
				user.setUserID(r.getInt("userID"));
				user.setUserName(r.getString("userName"));
				user.setPhone(r.getString("phone"));
				user.setNickName(r.getString("nickName"));
				user.setPassword(r.getString("password"));
				user.setHeadPath(r.getString("headPath"));
				user.setSex(r.getString("sex"));
				user.setStatus(r.getString("status"));
				list.add(user);
			}
			JSONArray jsonarray = JSONArray.fromObject(list);
			infor=JsonToString(jsonarray);
			jm=new jsonMessage("true","登陆成功",infor);
		}else {
			jm=new jsonMessage("false","登陆失败,用户名或密码错误");
		}
		stmt.close();
		conn.close();
		return jm;
	}
	
	//环信通过userName拿到个人信息
		public jsonMessage attainUserInforByHXDao(String userName) throws Exception{
			Connection conn=mdb.getconntion();//连接数据库
			String sql="select * from tblUserInfor where userName = '"+userName+"'";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			jsonMessage jm=null;
			if(r.next()) {
				String[] infor=null;
				List<UserInfor> list=new ArrayList<UserInfor>();
				r.beforeFirst();
				while(r.next()) {
					UserInfor user=new UserInfor();
					user.setNickName(r.getString("nickName"));
					user.setHeadPath(r.getString("headPath"));
					user.setStatus(r.getString("status"));
					list.add(user);
				}
				JSONArray jsonarray = JSONArray.fromObject(list);
				infor=JsonToString(jsonarray);
				jm=new jsonMessage("true","登陆成功",infor);
			}else {
				jm=new jsonMessage("false","登陆失败,用户名或密码错误");
			}
			stmt.close();
			conn.close();
			return jm;
		}
	
	//注册
	public jsonMessage registerDao(String userName,String password,String phone) throws Exception {
		Connection conn=mdb.getconntion();//连接数据库
		String result="success";//判断用户名是否存在
		jsonMessage jm=null;
		String sql="select * from tblUserInfor where userName='"+userName+"'";
		java.sql.Statement stmt=conn.createStatement();
		ResultSet r=stmt.executeQuery(sql);
		while(r.next()) {
			String s=r.getString("userName");
			if(s.equals(userName)) {
				result="fail";	
				break;
			}
		}
		if(result.equals("success")) {
			String sqlRegister="insert into tblUserInfor(userName,phone,nickName,password,headPath,sex,status) values(?,?,?,?,?,?,?)";
		    PreparedStatement pstmt=conn.prepareStatement(sqlRegister);
		    pstmt.setString(1, userName); //ֵ
		    pstmt.setString(2, phone);    //ֵ
		    pstmt.setString(3,userName);
		    pstmt.setString(4,password);
		    pstmt.setString(5, main.ipPath+"WorldWalk/image/tx.png");
		    pstmt.setString(6, "男");
		    pstmt.setString(7, "0");
		    pstmt.executeUpdate();  			
		    jm=new jsonMessage("true","注册成功");
		}else {
			jm=new jsonMessage("false","注册失败，用户名已经存在");
		}
		stmt.close();
		conn.close();
		return jm;
	}
	//检查注册时手机号是否存在
	public jsonMessage checkPhoneDao(String phone) throws Exception{
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		String sql="select * from tblUserInfor where phone='"+phone+"'";
		java.sql.Statement stmt=conn.createStatement();
		ResultSet r=stmt.executeQuery(sql);
		String result="success";
		while(r.next()) {
			String s=r.getString("phone");
			if(s.equals(phone)) {
				result="fail";
				break;
			}
		}
		if(result.equals("success")){
			jm=new jsonMessage("true","");
		}else {
			jm=new jsonMessage("false","该手机号码已经存在");
		}
		stmt.close();
		conn.close();
		return jm;
	}
	//检查忘记密码时手机号是否存在
	public jsonMessage forgetPasswordCheckPhoneDao(String phone) throws Exception{
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		String sql="select * from tblUserInfor where phone='"+phone+"'";
		java.sql.Statement stmt=conn.createStatement();
		ResultSet r=stmt.executeQuery(sql);
		String result="fail";
		while(r.next()) {
			String s=r.getString("phone");
			if(s.equals(phone)) {
				result="success";
				break;
			}
		}
		if(result.equals("success")){
			jm=new jsonMessage("true","");
		}else {
			jm=new jsonMessage("false","该手机号码已经存在");
		}
		stmt.close();
		conn.close();
		return jm;
	}
	
	//重置密码
	public jsonMessage forgetPasswordResetPasswordDao(String phone,String newPassword){
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		String sql="update tblUserInfor set password=? where phone='"+phone+"'";
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setString(1, newPassword);
			ps.executeUpdate();  
			ps.close();
			conn.close();
			jm=new jsonMessage("true","修改成功");
		}catch(Exception e) {
			jm=new jsonMessage("false","修改失败");
		}	
		return jm;
	}
	
	//获取个人信息
	public jsonMessage obtainUserDataDao(String userName) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="SELECT * from tblUserInfor where userName='"+userName+"'";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			String[] infor=null;
			List<UserInfor> list=new ArrayList<UserInfor>();
			while(r.next()) {
				UserInfor user=new UserInfor();
				user.setUserID(r.getInt("userID"));
				user.setUserName(r.getString("userName"));
				user.setPhone(r.getString("phone"));
				user.setNickName(r.getString("nickName"));
				user.setPassword(r.getString("password"));
				user.setHeadPath(r.getString("headPath"));
				user.setSex(r.getString("sex"));
				user.setStatus(r.getString("status"));
				list.add(user);
			}
			JSONArray jsonarray = JSONArray.fromObject(list);
			infor=JsonToString(jsonarray);
			jm=new jsonMessage("true","获取成功",infor);
			stmt.close();
			conn.close();
		}catch(Exception e) {
			jm=new jsonMessage("false","获取失败,后台数据库出问题了");
		}		
		return jm;
	}
	
	//修改个人信息
	public jsonMessage modifyUserDataDao(String userID,MultipartFile part,String nickName,String phone,String sex) {
		System.out.println(userID+" "+nickName);
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			InputStream is=part.getInputStream();
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			byte[] b=new byte[1024];
			while(is.read(b)>0) {
				baos.write(b);
			}
			b=baos.toByteArray();
			File file=new File(main.userImagePath+userID+".png");
			if(file.exists()) {
				
			}else {
				file.createNewFile();
			}
			FileOutputStream outStream = new FileOutputStream(file);
			//写入数据
			outStream.write(b);
			//关闭输出流
			outStream.close();
			
			String sql="update tblUserInfor set nickName=?,phone=?,sex=?,headPath=? where userID='"+userID+"'";
			PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1, nickName);
            ps.setString(2, phone);
            ps.setString(3, sex);
            ps.setString(4, main.userImage+userID+".png");
            ps.executeUpdate();  
            jm=new jsonMessage("true","修改成功");
            ps.close();
		}catch(Exception e) {
			jm=new jsonMessage("false","修改失败");
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			}
		}
		return jm;
	}
	//搜索用户
	public jsonMessage searchUserDao(String key) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String[] infor=null;
			List<UserInfor> list=new ArrayList<UserInfor>();
			
			String sql1="select * from tbluserinfor where userName ='"+key+"'";
			java.sql.Statement stmt1=conn.createStatement();
			ResultSet r1=stmt1.executeQuery(sql1);	
			while(r1.next()) {
				UserInfor user=new UserInfor();
				user.setUserID(r1.getInt("userID"));
				user.setUserName(r1.getString("userName"));
				user.setPhone(r1.getString("phone"));
				user.setNickName(r1.getString("nickName"));
				user.setHeadPath(r1.getString("headPath"));
				user.setSex(r1.getString("sex"));
				user.setStatus(r1.getString("status"));
				list.add(user);
				System.out.println("lalalaa");
			}
			System.out.println(list.size());
			r1.close();
			stmt1.close();
			
			String sql2="SELECT * from tblUserInfor where phone ='"+key+"'";
			java.sql.Statement stmt2=conn.createStatement();
			ResultSet r2=stmt2.executeQuery(sql2);	
			while(r2.next()) {
				UserInfor user=new UserInfor();
				user.setUserID(r2.getInt("userID"));
				user.setUserName(r2.getString("userName"));
				user.setPhone(r2.getString("phone"));
				user.setNickName(r2.getString("nickName"));
				user.setHeadPath(r2.getString("headPath"));
				user.setSex(r2.getString("sex"));
				user.setStatus(r2.getString("status"));
				list.add(user);
			}
			r2.close();
			stmt2.close();
			
			String sql3="SELECT * from tblUserInfor where nickName like '%"+key+"%'";
			java.sql.Statement stmt3=conn.createStatement();
			ResultSet r3=stmt3.executeQuery(sql3);		
			while(r3.next()) {
				UserInfor user=new UserInfor();
				user.setUserID(r3.getInt("userID"));
				user.setUserName(r3.getString("userName"));
				user.setPhone(r3.getString("phone"));
				user.setNickName(r3.getString("nickName"));
				user.setHeadPath(r3.getString("headPath"));
				user.setSex(r3.getString("sex"));
				user.setStatus(r3.getString("status"));
				list.add(user);
			}
			r3.close();
			stmt3.close();
			System.out.println(key);
			System.out.println("aaa"+list.size());
			
			JSONArray jsonarray = JSONArray.fromObject(list);
			infor=JsonToString(jsonarray);
			jm=new jsonMessage("true","获取成功",infor);
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
			jm=new jsonMessage("false","获取失败,后台数据库出问题了");
		}		
		return jm;
	}
	
	//更新或者保存用户位置信息
	public jsonMessage updateUserStateDao(String userID,String Longititude,String Latitude,String Direction) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		System.out.println("userID:"+userID+"Loong:"+Longititude+"lati:"+Latitude+"Direction:"+Direction);
		try {
			String sql1="select * from tblGPS where userID='"+userID+"'"; //查询是否否存在
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql1);
			if(r.next()) {
				String sql2="update tblGps set userID=?,Longitiude=?,Latitude=?,Direction=? where userID='"+userID+"'";
				PreparedStatement ps=conn.prepareStatement(sql2);
				ps.setString(1, userID);
	            ps.setString(2, Longititude);
	            ps.setString(3, Latitude);
	            ps.setString(4, Direction);
	            ps.executeUpdate();  
	            jm=new jsonMessage("true","更新位置信息成功");
	            ps.close();
			}else {
				String sql3="insert into tblGps(userID,Longitiude,Latitude,Direction) values(?,?,?,?)";
				PreparedStatement pstmt=conn.prepareStatement(sql3);
			    pstmt.setString(1, userID); //ֵ
			    pstmt.setString(2, Longititude);    //ֵ
			    pstmt.setString(3,Latitude);
			    pstmt.setString(4,Direction);
			    pstmt.executeUpdate();
			    pstmt.close();
			    jm=new jsonMessage("true","保存位置信息成功");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			 jm=new jsonMessage("false","失败");
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jm;
	}
	public static String[] JsonToString(JSONArray jsonObj){	
		Object[] os = jsonObj.toArray();
		String[] shopNoArrinfo = new String[os.length]; 
		for(int i=0; i<os.length; i++) { 
			shopNoArrinfo[i] = os[i].toString(); 
		}
		return shopNoArrinfo;
	}
}
