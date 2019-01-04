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
import com.walk.pojo.main;

import net.sf.json.JSONArray;

public class groupDao {
	
	//获取用户的所有群
	public jsonMessage obtainAllGroupIdDao(String userID){
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="select a.groupID,a.groupHostID,a.groupHXID,a.groupName,a.groupPic,a.status FROM tblgroup a INNER JOIN tblgroupuser b on b.userID='"+userID+"' and a.groupID=b.groupID";
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
				gp.setGroupHXID(r.getString("groupHXID"));
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
			String sql="select a.userID,a.userName,a.nickName,a.headPath,a.sex FROM tbluserinfor a INNER JOIN tblgroupuser b on b.groupID="+groupID+" and a.userID=b.userID";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			String[] infor=null;
			List<UserInfor> list=new ArrayList<UserInfor>();
			while(r.next()) {
				UserInfor user=new UserInfor();
				user.setUserID(r.getInt("userID"));
				user.setUserName(r.getString("userName"));
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
	public jsonMessage createGroupDao(String userID,MultipartFile part,String groupName,String groupDescribe,String groupHXID) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="select * from tblGroup where groupName='"+groupName+"'";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			if(!r.next()) {
				InputStream is=part.getInputStream();
				ByteArrayOutputStream baos=new ByteArrayOutputStream();
				byte[] b=new byte[1024];
				while(is.read(b)>0) {
					baos.write(b);
				}
				b=baos.toByteArray();
				File file=new File(main.groupImagePath+groupName+".png");
				if(file.exists()) {
					
				}else {
					file.createNewFile();
				}
				FileOutputStream outStream = new FileOutputStream(file);
				//写入数据
				outStream.write(b);
				//关闭输出流
				outStream.close();
				
				String sqlRegister="insert into tblGroup(groupHostID,groupName,groupPic,groupDescribe,status,groupHXID) values(?,?,?,?,?,?)";
			    PreparedStatement pstmt=conn.prepareStatement(sqlRegister);
			    pstmt.setString(1, userID); //ֵ
			    pstmt.setString(2, groupName);    //ֵ
			    pstmt.setString(3,main.groupImage+groupName+".png");
			    pstmt.setString(4,groupDescribe);
			    pstmt.setString(5,"0");
			    pstmt.setString(6, groupHXID);
			    pstmt.executeUpdate();
	            pstmt.close();
	            String sql1="insert into tblGroupUser(userID,groupID) select groupHostID,groupID from tblGroup where groupName='"+groupName+"'";
	            PreparedStatement p1=conn.prepareStatement(sql1);
	            p1.execute(sql1);
	            p1.close();
	            conn.close();
	             jm=new jsonMessage("true","创建成功");
			}else {
				jm=new jsonMessage("false","群昵称已存在");
			}	
		}catch(Exception e) {
			jm=new jsonMessage("false","创建失败");
			e.printStackTrace();
		}
		return jm;
	}
	
	public jsonMessage modifyGroupInformDao(String groupID,String groupAnnouncement) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="update tblGroup set groupAnnouncement='"+groupAnnouncement+"' where groupId='"+groupID+"'";
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.executeUpdate();
			conn.close();
			jm=new jsonMessage("true","修改成功");
		}catch(Exception e ) {
			e.printStackTrace();
		}
		return jm;
	}
	
	public jsonMessage modifyGroupDescribeDao(String groupID,String groupDescribe) {
		System.out.println(groupDescribe);
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="update tblGroup set groupDescribe='"+groupDescribe+"' where groupId='"+groupID+"'";
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.executeUpdate();
			conn.close();
			jm=new jsonMessage("true","修改成功");
		}catch(Exception e ) {
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
				conn.close();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			jm=new jsonMessage("false","搜索失败");
		}
		return jm;
	}
	
	//获取搜索群群信息
	public jsonMessage obtainSearchGroupInfoDao(String groupID) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="select count(*)woman from tblgroupUser a inner join tblUserInfor b where a.groupID='"+groupID+"' and a.userID=b.userID and b.sex='女'";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			String woman="0";
			String []infor = new String[4];
			if(r.next()) {				
				woman=String.valueOf(r.getInt("woman"));
			}
			stmt.close();
			r.close();
			String sql1="select count(*)man from tblgroupUser a inner join tblUserInfor b where a.groupID='"+groupID+"' and a.userID=b.userID and b.sex='男'";
			java.sql.Statement stmt1=conn.createStatement();
			ResultSet r1=stmt1.executeQuery(sql1);
			String man="0";
			if(r1.next()) {
				man=String.valueOf(r1.getInt("man"));
			}
			stmt1.close();
			r1.close();
			String sql2="select a.groupDescribe,b.nickName from tblGroup a inner join tblUserInfor b where a.groupID='"+groupID+"' and a.groupHostID=b.userID";
			java.sql.Statement stmt2=conn.createStatement();
			ResultSet r2=stmt2.executeQuery(sql2);
			String groupDescribe="";
			String nickName="";
			if(r2.next()) {
				groupDescribe=r2.getString("groupDescribe");
				nickName=r2.getString("nickName");
			}
			stmt2.close();
			r2.close();
			infor[0]=woman;
			infor[1]=man;
			infor[2]=groupDescribe;
			infor[3]=nickName;
			System.out.println(infor);
			jm=new jsonMessage("true","搜索成功",infor);
			conn.close();
		}catch(Exception e) {
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
	//群主邀请群成员申请请求
	public jsonMessage addUserDao(String userID,String groupID) {
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
				pstmt.setString(3,"3");
				 pstmt.executeUpdate(); 
				jm=new jsonMessage("true","入群申请发送成功");
				pstmt.close();
			}
					
		}catch(Exception e) {
			e.printStackTrace();
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
	
	//入群申请请求
	public jsonMessage applyAddGroupDao(String userID,String groupID) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="select * from tblEntryApplication where userID='"+userID+"' and groupID='"+groupID+"'";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			if(r.next()) {
				jm=new jsonMessage("false","已经加入群或已有入群申请");
			}else {
				String sql1="insert into tblEntryApplication(userID,groupID,status) values(?,?,?)";
				PreparedStatement pstmt=conn.prepareStatement(sql1);
				pstmt.setString(1, userID); //ֵ
				pstmt.setString(2, groupID);    //ֵ
				pstmt.setString(3,"0");
				 pstmt.executeUpdate(); 
				jm=new jsonMessage("true","入群申请发送成功");
				pstmt.close();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
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
		System.out.println("groupID:"+groupID);
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="update tblEntryApplication set status=? where userID='"+userID+"' and groupID='"+groupID+"'";
			PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.executeUpdate();
            if(status.equals("1")) {
            	String sql2="insert into tblgroupUser(userID,groupID) values(?,?)";
	            PreparedStatement ps1=conn.prepareStatement(sql2);
	            ps1.setString(1, userID);
	            ps1.setString(2, groupID);
	            ps1.executeUpdate();
            }
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
	
	//用户审核入群申请
	public jsonMessage userAuditingAddGroupDao(String userID,String groupID,String status) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="update tblEntryApplication set status=? where userID='"+userID+"' and groupID='"+groupID+"'";
			PreparedStatement ps=conn.prepareStatement(sql);
	        ps.setString(1, status);
	        ps.executeUpdate();
	        if(status.equals("4")) {
            	String sql2="insert into tblgroupUser(userID,groupID) values(?,?)";
	            PreparedStatement ps1=conn.prepareStatement(sql2);
	            ps1.setString(1, userID);
	            ps1.setString(2, groupID);
	            ps1.executeUpdate();
            }
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
			ps.execute();
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
			ps.execute(sql);
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
			ps.execute(sql);
			ps.close();
			
			String sql1="delete from tblEntryApplication where groupID='"+groupID+"'";
			PreparedStatement ps1=conn.prepareStatement(sql1);
			ps1.execute(sql1);
			ps1.close();
			
			String sql2="delete from tblGroup where groupID='"+groupID+"'";
			PreparedStatement ps2=conn.prepareStatement(sql2);
			ps2.execute(sql2);
			ps2.close();
			jm=new jsonMessage("true","解散成功");
		}catch(Exception e) {
			e.printStackTrace();
			jm=new jsonMessage("false","解散失败");
		}
		return jm;
	}
	
	//通过环信得到群成员的信息
	public jsonMessage attainGroupUserMessageByHxIdDao(String groupHXID) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="select a.userName,a.nickName,a.headPath FROM tbluserinfor a INNER JOIN tblgroupuser b on b.groupHXID="+groupHXID+" and a.userID=b.userID";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			String[] infor=null;
			List<UserInfor> list=new ArrayList<UserInfor>();
			while(r.next()) {
				UserInfor user=new UserInfor();
				user.setUserName(r.getString("userName"));
				user.setNickName(r.getString("nickName"));
				user.setHeadPath(r.getString("headPath"));
				list.add(user);
			}
			JSONArray jsonarray = JSONArray.fromObject(list);
			infor=JsonToString(jsonarray);
			jm=new jsonMessage("true","获取成功",infor);
			stmt.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return jm;
	}
	
	//获取各类最新通知的条数
	public jsonMessage attainMessageNumDao(String userID) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			//系统推送
			java.sql.Statement stmt=conn.createStatement();
			String sql="select count(*)SystMessage from tblSystemPush where status='0' and userID='"+userID+"'";
			ResultSet r=stmt.executeQuery(sql);
			String SystMessage="0";
			String []infor = new String[4];
			if(r.next()) {				
				SystMessage=String.valueOf(r.getInt("SystMessage"));
			}
			r.close();
			
			//入群申请的
			String sql1="select count(*)Inform1 from tblEntryApplication a inner join tblGroup b where a.status='0' and b.groupHostID=+'"+userID+"' and a.groupID=b.groupID";
			ResultSet r1=stmt.executeQuery(sql1);
			String Inform1="0";
			if(r1.next()) {
				Inform1=String.valueOf(r1.getInt("Inform1"));
			}
			r1.close();
			
			//群主邀请入群的
			String sql2="select count(*)Inform2 from tblEntryApplication where status='3' and userID='"+userID+"'";
			ResultSet r2=stmt.executeQuery(sql2);
			String Inform2="0";
			if(r2.next()) {
				Inform2=String.valueOf(r2.getInt("Inform2"));
			}
			r2.close();
			
			//入群结果通知
			String sql3="select count(*)Inform3 from tblEntryApplication where userID='"+userID+"' and(status='1' or status='2' or status='4' or status='5')";
			ResultSet r3=stmt.executeQuery(sql3);
			String Inform3="0";
			if(r3.next()) {
				Inform3=String.valueOf(r3.getInt("Inform3"));
			}
			r3.close();
			stmt.close();
			infor[0]=SystMessage;
			infor[1]=Inform1;
			infor[2]=Inform2;
			infor[3]=Inform3;
			jm=new jsonMessage("true","搜索成功",infor);
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return jm;
	}
	
	//入群结果通知
	public jsonMessage attainEntryResultDao(String userID) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {			
			String sql="select b.groupName,a.Date,a.status from tblEntryApplication a INNER join tblgroup b on a.userID='"+userID+"' and a.groupID=b.groupID and (a.status='1' or a.status='2' or a.status='4' or a.status='5' or a.status='6' or a.status='7' or a.status='8' or a.status='9')";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			String[] infor=null;
			List<EntryApplication> list=new ArrayList<EntryApplication>();
			if(r.next()) {
				String sql1="update tblEntryApplication set status=? where status='1'";
				PreparedStatement ps1=conn.prepareStatement(sql1);
		        ps1.setString(1,"6" );
		        ps1.executeUpdate();
		        ps1.close();
		        
		        String sql2="update tblEntryApplication set status=? where status='2'";
				PreparedStatement ps2=conn.prepareStatement(sql2);
		        ps2.setString(1,"7" );
		        ps2.executeUpdate();
		        ps2.close();
		        
		        String sql3="update tblEntryApplication set status=? where status='4'";
				PreparedStatement ps3=conn.prepareStatement(sql3);
		        ps3.setString(1,"8" );
		        ps3.executeUpdate();
		        ps3.close();
		        
		        String sql4="update tblEntryApplication set status=? where status='5'";
				PreparedStatement ps4=conn.prepareStatement(sql4);
		        ps4.setString(1,"9" );
		        ps4.executeUpdate();
		        ps4.close();
		        r.beforeFirst();
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
			stmt.close();
			conn.close();
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
	//群主获取入群申请的通知
	public jsonMessage attainEntryApplicationDao(String userID) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="SELECT b.groupName,b.groupHXID,b.groupID,a.userID,c.nickName,c.userName,a.Date,a.status from tblEntryApplication a JOIN tblgroup b on b.groupID=a.groupID AND b.groupHostID='"+userID+"' join tbluserinfor c on c.userID=a.userID";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			String[] infor=null;
			List<EntryApplication> list=new ArrayList<EntryApplication>();
			if(r.next()) {
				String sql1="update tblEntryApplication set status=? where status='0'";
				PreparedStatement ps1=conn.prepareStatement(sql1);
		        ps1.setString(1,"10" );
		        ps1.executeUpdate();
		        ps1.close();
		        r.beforeFirst();
				while(r.next()) {
					if(r.getString("status").equals("0") || r.getString("status").equals("1") || r.getString("status").equals("2") || r.getString("status").equals("6") || r.getString("status").equals("7") || r.getString("status").equals("10")) {
						EntryApplication ea=new EntryApplication();
						ea.setGroupName(r.getString("groupName"));
						ea.setGroupID(r.getInt("groupID"));
						ea.setGroupHXID(r.getString("groupHXID"));
						ea.setUserID(r.getInt("userID"));
						ea.setNickName(r.getString("nickName"));
						ea.setUserName(r.getString("userName"));
						ea.setDate(r.getString("Date"));
						ea.setStatus(r.getString("status"));
						list.add(ea);
					}
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
	
	//群主邀请入群通知
	public jsonMessage attainAddUserDao(String userID) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="select b.groupID,b.groupName,b.groupHXID,a.Date,a.status from tblEntryApplication a INNER join tblgroup b on a.userID='"+userID+"' and a.groupID=b.groupID";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			String[] infor=null;
			List<EntryApplication> list=new ArrayList<EntryApplication>();
			if(r.next()) {
				String sql1="update tblEntryApplication set status=? where status='3'";
				PreparedStatement ps1=conn.prepareStatement(sql1);
		        ps1.setString(1,"11" );
		        ps1.executeUpdate();
		        ps1.close();
		        r.beforeFirst();
				while(r.next()) {
					if(r.getString("status").equals("3") || r.getString("status").equals("4") || r.getString("status").equals("5") || r.getString("status").equals("8") || r.getString("status").equals("9") || r.getString("status").equals("11")) {
						EntryApplication ea=new EntryApplication();
						ea.setGroupID(r.getInt("groupID"));
						ea.setGroupName(r.getString("groupName"));
						ea.setGroupHXID(r.getString("groupHXID"));
						ea.setDate(r.getString("Date"));
						ea.setStatus(r.getString("status"));
						list.add(ea);
					}				
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
	//获取特定群信息
	public jsonMessage obtainOneGroupIdDao(String userID,String groupHXID){
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="select a.groupID,a.groupHostID,a.groupHXID,a.groupName,a.groupPic,a.status FROM tblgroup a INNER JOIN tblgroupuser b on b.userID='"+userID+"' and a.groupHXID='"+groupHXID+"' and a.groupID=b.groupID";
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
				gp.setGroupHXID(r.getString("groupHXID"));
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
	
	//获取用户获取系统公告
	public jsonMessage attainSystemDao(String userID) {
		Connection conn=mdb.getconntion();//连接数据库
		jsonMessage jm=null;
		try {
			String sql="select * from tblSystemPush where userID='"+userID+"'";
			java.sql.Statement stmt=conn.createStatement();
			ResultSet r=stmt.executeQuery(sql);
			String[] infor;
			List<SystemPush> list=new ArrayList<SystemPush>();
			if(r.next()) {
				String sql1="update tblSystemPush set status=? where status='0' and userID='"+userID+"'";
				PreparedStatement ps1=conn.prepareStatement(sql1);
		        ps1.setString(1,"1" );
		        ps1.executeUpdate();
		        ps1.close();
		        r.beforeFirst();
				while(r.next()) {
					SystemPush sp=new SystemPush();
					sp.setContent(r.getString("content"));
					sp.setDate(r.getString("date"));
					sp.setTheme(r.getString("theme"));
					sp.setStatus(r.getString("status"));
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
			    pstmt.executeUpdate();
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
