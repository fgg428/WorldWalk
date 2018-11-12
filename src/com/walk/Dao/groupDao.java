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
import com.walk.pojo.EntryApplication;
import com.walk.pojo.Gps;
import com.walk.pojo.Group;
import com.walk.pojo.SystemPush;
import com.walk.pojo.UserInfor;
import com.walk.pojo.jsonMessage;

import net.sf.json.JSONArray;

public class groupDao {
	
	//获取用户的所有群
	public jsonMessage obtainAllGroupIdDao(String userID){
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="select a.groupID,a.groupHostID,a.groupName,a.groupPic,a.status FROM tblgroup a INNER JOIN tblgroupuser b on b.userID='"+userID+"' and a.groupID=b.groupID";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			String[] infor=null;
			List<Group> list=new ArrayList<Group>();
			while(r.next()) {
				Group gp=new Group();
				gp.setGroupID(r.getInt("groupID"));
				gp.setGroupHostID(r.getInt("groupHostID"));
				gp.setGroupName(r.getString("groupName")); 
				gp.setGroupPic(r.getString("groupPic"));
				gp.setStatus(r.getString("status"));
				list.add(gp);
			}
			stmt.close();
			JSONArray jsonarray = JSONArray.fromObject(list);
			infor=JsonToString(jsonarray);
			jm=new jsonMessage("true","获取成功",infor);
		}catch(Exception e) {
			e.printStackTrace();
			jm=new jsonMessage("false","服务器开小差了");
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
	
	//获取群简介
	public jsonMessage obtainGroupInfoDao(String groupID) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="SELECT groupAnnouncement,groupDescribe FROM tblgroup WHERE groupID='"+groupID+"'";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			String[] infor=null;
			List<Group> list=new ArrayList<Group>();
			while(r.next()) {
				Group gp=new Group();
				gp.setGroupAnnouncement(r.getString("groupAnnouncement"));
				gp.setGroupDescribe(r.getString("groupDescribe"));
				list.add(gp);
			}
			JSONArray jsonarray = JSONArray.fromObject(list);
			infor=JsonToString(jsonarray);
			jm=new jsonMessage("true","获取成功",infor);
			stmt.close();
		}catch(Exception e) {
			e.printStackTrace();
			jm=new jsonMessage("false","服务器开小差了");
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
	
	//获取群用户
	public jsonMessage obtainGroupMemberDao(String groupID) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="select a.userID,a.nickName,a.headPath,a.sex FROM tbluserinfor a INNER JOIN tblgroupuser b on b.groupID="+groupID+" and a.userID=b.userID";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			String[] infor=null;
			List<UserInfor> list=new ArrayList<UserInfor>();
			while(r.next()) {
				UserInfor user=new UserInfor();
				user.setUserID(r.getInt("userID"));
				user.setNickName(r.getString("nickName"));
				user.setHeadPath(r.getString("headPath"));
				user.setSex(r.getString("sex"));
				list.add(user);
			}
			JSONArray jsonarray = JSONArray.fromObject(list);
			infor=JsonToString(jsonarray);
			jm=new jsonMessage("true","获取成功",infor);
			stmt.close();
		}catch(Exception e) {
			e.printStackTrace();
			jm=new jsonMessage("false","服务器开小差了");
			e.getStackTrace();
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
	
	//获取群成员资料
	public jsonMessage obtainGroupMemberDetaileDataDao(String userID) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="SELECT headPath,nickName,userName,phone,sex FROM tblUserInfor WHERE userID='"+userID+"'";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			String[] infor=null;
			List<UserInfor> list=new ArrayList<UserInfor>();
			while(r.next()) {
				UserInfor user=new UserInfor();
				user.setUserName(r.getString("userName"));
				user.setPhone(r.getString("phone"));
				user.setNickName(r.getString("nickName"));
				user.setHeadPath(r.getString("headPath"));
				user.setSex(r.getString("sex"));
				list.add(user);				
			}
			JSONArray jsonarray = JSONArray.fromObject(list);
			infor=JsonToString(jsonarray);
			jm=new jsonMessage("true","获取成功",infor);
			stmt.close();
		}catch(Exception e) {
			jm=new jsonMessage("false","获取失败");
			e.printStackTrace();
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
	
	//创建群组
	public jsonMessage createGroupDao(String userID,String path,MultipartFile part,String groupName,String groupDescribe) {
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
			File file=new File(path+groupName+".png");
			if(file.exists()) {
				
			}else {
				file.createNewFile();
			}
			FileOutputStream outStream = new FileOutputStream(file);
			//写入数据
			outStream.write(b);
			//关闭输出流
			outStream.close();
			
			String sqlRegister="insert into tblGroup(groupHostID,groupName,groupPic,groupDescribe,status) values(?,?,?,?,?)";
		    PreparedStatement pstmt=conn.prepareStatement(sqlRegister);
		    pstmt.setString(1, userID); //ֵ
		    pstmt.setString(2, groupName);    //ֵ
		    pstmt.setString(3,path);
		    pstmt.setString(4,groupDescribe);
		    pstmt.setString(5,"0");
		    pstmt.executeUpdate();
            jm=new jsonMessage("true","创建成功");
            pstmt.close();
            conn.close();
		}catch(Exception e) {
			jm=new jsonMessage("false","创建失败");
			e.printStackTrace();
		}
		return jm;
	}
	
	//搜索群组
	public jsonMessage searchGroupDao(String keyword) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="select * from tblGroup";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			String[] infor=null;
			List<Group> list=new ArrayList<Group>();
			while(r.next()) {
				Group gp=new Group();
				String groupName=r.getString("groupName");
				if(groupName.indexOf(keyword)>=0) {
					gp.setGroupID(r.getInt("groupID"));
					gp.setGroupName(r.getString("groupName"));
					gp.setGroupPic(r.getString("groupPic"));
					gp.setGroupDescribe(r.getString("groupDescribe"));
					gp.setStatus(r.getString("status"));
					list.add(gp);
				}
			}
			if(list.size()==0) {
				jm=new jsonMessage("false","搜索失败,群组不存在");
			}else {
				JSONArray jsonarray = JSONArray.fromObject(list);
				infor=JsonToString(jsonarray);
				jm=new jsonMessage("true","搜索成功",infor);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			jm=new jsonMessage("false","搜索失败");
		}
		return jm;
	}
	
	//获取群成员位置信息
	public jsonMessage obtainGroupPositionInfoDao(String groupID) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="select a.userID,a.phone,a.headPath,a.nickName,c.Longitiude,c.Latitude,c.Direction FROM tbluserinfor a\r\n" + 
					"JOIN tblgroupuser b on a.userID=b.userID and b.groupID='"+groupID+"' JOIN tblgps c on c.userID=b.userID";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			String[] infor=null;
			List<Gps> list=new ArrayList<Gps>();
			while(r.next()) {
				Gps gps=new Gps();
				gps.setUserID(r.getInt("userID"));
				gps.setPhone(r.getString("phone"));
				gps.setHeadPath(r.getString("headPath"));
				gps.setNickName(r.getString("nickName"));
				gps.setLongititude(r.getString("Longitiude"));
				gps.setLatitude(r.getString("Latitude"));
				gps.setDirection(r.getString("Direction"));
				list.add(gps);
			}
			JSONArray jsonarray = JSONArray.fromObject(list);
			infor=JsonToString(jsonarray);
			jm=new jsonMessage("true","获取成功",infor);
		}catch(Exception e) {
			e.printStackTrace();
			jm=new jsonMessage("false","获取失败");
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
	
	//入群申请请求
	public jsonMessage applyAddGroupDao(String userID,String groupID) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="select * from tblEntryApplication where userID='"+userID+"' and groupID='"+groupID+"'";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			if(r.next()) {
				jm=new jsonMessage("false","已经发送入群申请");
			}else {
				String sql1="insert into tblEntryApplication(userID,groupID,status) values(?,?,?)";
				PreparedStatement pstmt=conn.prepareStatement(sql1);
				pstmt.setString(1, userID); //ֵ
				pstmt.setString(2, groupID);    //ֵ
				pstmt.setString(3,"0");
				pstmt.executeQuery();
				jm=new jsonMessage("true","入群申请发送成功");
				pstmt.close();
			}
			
		}catch(Exception e) {
			e.getStackTrace();
			jm=new jsonMessage("false","入群申请发送失败");
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
	
	//审核入群申请
	public jsonMessage auditingAddGroupDao(String userID,String groupID,String status) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="update tblEntryApplication set status=? where userID='"+userID+"' and groupID='"+groupID+"'";
			PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.executeQuery();
            jm=new jsonMessage("true","审核成功");
		}catch(Exception e) {
			e.printStackTrace();
			jm=new jsonMessage("false","审核失败");
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
	
	//移出群成员
	public jsonMessage deleteGroupMemberDao(String userID,String groupID) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="delete from tblgroupuser where userID='"+userID+"' and groupID='"+groupID+"'";
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.executeQuery();
	        jm=new jsonMessage("true","移出成功");
		}catch(Exception e) {
			e.printStackTrace();
			jm=new jsonMessage("false","移出失败");
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
	
	//退出群
	public jsonMessage singOutGroupDao(String userID,String groupID) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="delete from tblgroupuser where userID='"+userID+"' and groupID='"+groupID+"'";
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.executeQuery();
	        jm=new jsonMessage("true","退出成功");
		}catch(Exception e){
			e.printStackTrace();
			jm=new jsonMessage("false","退出失败");
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
	//解散群
	public jsonMessage disbandGroupDao(String groupID) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="delete from tblgroupUser where groupID='"+groupID+"'";
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.executeQuery();
			ps.close();
			String sql1="delete from tblGroup where groupID='"+groupID+"'";
			PreparedStatement ps1=conn.prepareStatement(sql1);
			ps1.executeQuery();
			ps1.close();
			jm=new jsonMessage("true","解散成功");
		}catch(Exception e) {
			e.printStackTrace();
			jm=new jsonMessage("false","解散失败");
		}
		return jm;
	}
	
	//入群结果通知
	public jsonMessage attainEntryResultDao(String userID) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="select b.groupName,a.Date,a.status from tblEntryApplication a INNER join tblgroup b on a.userID='"+userID+"' and a.groupID=b.groupID";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			String[] infor=null;
			List<EntryApplication> list=new ArrayList<EntryApplication>();
			if(r.next()) {
				while(r.next()) {
					EntryApplication ea=new EntryApplication();
					ea.setGroupName(r.getString("groupName"));
					ea.setDate(r.getString("Date"));
					ea.setStatus(r.getString("status"));
					list.add(ea);
				}
				JSONArray jsonarray = JSONArray.fromObject(list);
				infor=JsonToString(jsonarray);
				jm=new jsonMessage("true","查询成功",infor);
			}else {
				jm=new jsonMessage("false","没有数据");
			}
		}catch(Exception e) {
			e.printStackTrace();
			jm=new jsonMessage("false","查询失败");
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
	//群组获取入群申请的通知
	public jsonMessage attainEntryApplicationDao(String userID) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="SELECT b.groupName,a.userID,c.nickName,a.Date from tblEntryApplication a JOIN tblgroup b on b.groupID=a.groupID AND b.groupHostID='"+userID+"' join tbluserinfor c on c.userID=a.userID";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			String[] infor=null;
			List<EntryApplication> list=new ArrayList<EntryApplication>();
			if(r.next()) {
				while(r.next()) {
					EntryApplication ea=new EntryApplication();
					ea.setGroupName(r.getString("groupName"));
					ea.setUserID(r.getInt("userID"));
					ea.setNickName(r.getString("nickName"));
					ea.setDate(r.getString("Date"));
					ea.setStatus(r.getString("status"));
					list.add(ea);
				}
				JSONArray jsonarray = JSONArray.fromObject(list);
				infor=JsonToString(jsonarray);
				jm=new jsonMessage("true","查询成功",infor);
			}
		}catch(Exception e) {
			e.printStackTrace();
			jm=new jsonMessage("false","查询失败");
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
	
	//获取用户获取系统公告
	public jsonMessage attainSystemDao() {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="select * from tblSystemPush";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			String[] infor;
			List<SystemPush> list=new ArrayList<SystemPush>();
			if(r.next()) {
				while(r.next()) {
					SystemPush sp=new SystemPush();
					sp.setContent(r.getString("content"));
					sp.setDate(r.getString("date"));
					sp.setTheme(r.getString("theme"));
					list.add(sp);
				}
				JSONArray jsonarray = JSONArray.fromObject(list);
				infor=JsonToString(jsonarray);
				jm=new jsonMessage("true","获取成功",infor);
			}else {
				jm=new jsonMessage("false","没有数据");
			}
		}catch(Exception e) {
			jm=new jsonMessage("false","服务器出问题了");
			e.printStackTrace();
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
	
	public jsonMessage attainMessageLastDao(String userIdOne,String userIdTwo,String message,String date) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql1="select * from tblMessage where userIdOne='"+userIdOne+"' and userIdTwo='"+userIdTwo+"'";
			java.sql.Statement stmt1=conn.createStatement();
			ResultSet r1=stmt1.executeQuery(sql1);
			stmt1.close();
			if(r1.next()) {
				String sql2="upate tblMessage set message=?,date=? where userIdOne='"+userIdOne+"' and userIdTwo='"+userIdTwo+"'";
				PreparedStatement ps=conn.prepareStatement(sql2);
				ps.setString(1, message);
				ps.setString(2, date);
				ps.executeUpdate();  
				ps.close();
				
				String sql3="upate tblMessage set message=?,date=? where userIdOne='"+userIdTwo+"' and userIdTwo='"+userIdOne+"'";
				PreparedStatement ps1=conn.prepareStatement(sql3);
				ps1.setString(1, message);
				ps1.setString(2, date);
				ps1.executeUpdate();  
				ps1.close();
			}else {
				String sql4="insert into tblMessage(userIdOne,userIdTwo,message,date) values(?,?,?,?)";
				PreparedStatement pstmt=conn.prepareStatement(sql4);
			    pstmt.setString(1, userIdOne); //ֵ
			    pstmt.setString(2, userIdTwo);    //ֵ
			    pstmt.setString(3,message);
			    pstmt.setString(4,date);
			    pstmt.executeQuery();
			    pstmt.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
			jm=new jsonMessage("false","服务器出问题了");
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
