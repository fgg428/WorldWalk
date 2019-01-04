package com.walk.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.walk.Dao.groupDao;
import com.walk.pojo.jsonMessage;

import net.sf.json.JSONObject;

@Controller
public class groupController {
	JSONObject jsonOb = null;
	groupDao dao=new groupDao();
	@RequestMapping("/obtainAllGroupId") //获取用户的所有群
    public void obtainAllGroupId(HttpServletRequest req,HttpServletResponse rep) throws Exception
    {
    	String userID=req.getParameter("userID");
		jsonMessage jm=dao.obtainAllGroupIdDao(userID);   	
		deal(jm,rep);
    }
	
	@RequestMapping("/obtainGroupInfo") //获取用户的群的群简介
    public void obtainGroupInfo(HttpServletRequest req,HttpServletResponse rep) throws Exception
    {
		String groupID=req.getParameter("groupID");
		jsonMessage jm=dao.obtainGroupInfoDao(groupID);   	
		deal(jm,rep);
    }
	
	@RequestMapping("/obtainGroupMember") //获取用户的群的群成员
    public void obtainGroupMember(HttpServletRequest req,HttpServletResponse rep) throws Exception
    {
		String groupID=req.getParameter("groupID");
		jsonMessage jm=dao.obtainGroupMemberDao(groupID);   	
		deal(jm,rep);
    }
	
	@RequestMapping("/obtainGroupMemberDetaileData") //获取用户的群的群成员的详细信息
    public void obtainGroupMemberDetaileData(HttpServletRequest req,HttpServletResponse rep) throws Exception
    {
		String userID=req.getParameter("userID");
		jsonMessage jm=dao.obtainGroupMemberDetaileDataDao(userID);
		deal(jm,rep);
    }
	
	
	@RequestMapping("/createGroup") //创建群组
    public void createGroup(HttpServletRequest req,HttpServletResponse rep,@RequestParam("image") MultipartFile file) throws Exception
    {
		req.setCharacterEncoding("utf-8");
		String userID=req.getParameter("userID");
		String groupName=req.getParameter("groupName");
		if((groupName==null)|| (groupName.equalsIgnoreCase(""))){			
		}else {
			groupName = new String(groupName.getBytes("ISO8859-1"), "UTF-8"); 
		}
		String groupDescribe=req.getParameter("groupDescribe");
		if((groupDescribe==null)|| (groupDescribe.equalsIgnoreCase(""))){			
		}else {
			groupDescribe = new String(groupDescribe.getBytes("ISO8859-1"), "UTF-8"); 
		}
		String groupHXID=req.getParameter("groupHXID");
		jsonMessage jm=dao.createGroupDao(userID,file,groupName,groupDescribe,groupHXID);
		deal(jm,rep);
    }
	
	@RequestMapping("/modifyGroupInform") //修该群公告
    public void modifyGroupInform(HttpServletRequest req,HttpServletResponse rep) throws Exception
    {
		String groupID=req.getParameter("groupID");
		String groupAnnouncement=req.getParameter("groupAnnouncement");
		if((groupAnnouncement==null)|| (groupAnnouncement.equalsIgnoreCase(""))){			
		}else {
			groupAnnouncement = new String(groupAnnouncement.getBytes("ISO8859-1"), "UTF-8"); 
		}
		jsonMessage jm=dao.modifyGroupInformDao(groupID,groupAnnouncement);
		deal(jm,rep);
    }
	
	@RequestMapping("/modifyGroupDescribe") //修改群描述
    public void modifyGroupDescript(HttpServletRequest req,HttpServletResponse rep) throws Exception
    {
		String groupID=req.getParameter("groupID");
		String groupDescribe=req.getParameter("groupDescribe");
		if((groupDescribe==null)|| (groupDescribe.equalsIgnoreCase(""))){			
		}else {
			groupDescribe = new String(groupDescribe.getBytes("ISO8859-1"), "UTF-8"); 
		}
		jsonMessage jm=dao.modifyGroupDescribeDao(groupID,groupDescribe);
		deal(jm,rep);
    }
	
	@RequestMapping("/searchGroup") //搜索群组
	public void searchGroup(HttpServletRequest req,HttpServletResponse rep)throws Exception{
		String keyword=req.getParameter("keyword");
		jsonMessage jm=dao.searchGroupDao(keyword);
		deal(jm,rep);
	}
	
	@RequestMapping("/obtainSearchGroupInfo") //获取搜索群组的信息
	public void obtainSearchGroupInfo(HttpServletRequest req,HttpServletResponse rep)throws Exception{
		String groupID=req.getParameter("groupID");
		jsonMessage jm=dao.obtainSearchGroupInfoDao(groupID);
		deal(jm,rep);
	}
	
	@RequestMapping("/obtainGroupPositionInfo") //获取群用户位置信息
	public void obtainGroupPositionInfo(HttpServletRequest req,HttpServletResponse rep)throws Exception{
		String groupID=req.getParameter("groupID");
		jsonMessage jm=dao.obtainGroupPositionInfoDao(groupID);
		deal(jm,rep);
	}

	@RequestMapping("/applyAddGroup") //添加加入群请求
	public void applyAddGroup(HttpServletRequest req,HttpServletResponse rep)throws Exception{
		String userID=req.getParameter("userID");
		String groupID=req.getParameter("groupID");
		jsonMessage jm=dao.applyAddGroupDao(userID,groupID);
		deal(jm,rep);
	}
	
	@RequestMapping("/addUser") //群主添加用户加入群请求
	public void addUser(HttpServletRequest req,HttpServletResponse rep)throws Exception{
		String userID=req.getParameter("userID");
		String groupID=req.getParameter("groupID");
		jsonMessage jm=dao.addUserDao(userID, groupID);
		deal(jm,rep);
	}
	
	@RequestMapping("/auditingAddGroup") //审核入群申请
	public void auditingAddGroup(HttpServletRequest req,HttpServletResponse rep)throws Exception{
		String userID=req.getParameter("userID");
		String groupID=req.getParameter("groupID");
		String status=req.getParameter("status");
		jsonMessage jm=dao.auditingAddGroupDao(userID,groupID,status);
		deal(jm,rep);
	}
	
	@RequestMapping("/userAuditingAddGroup") //用户审核入群申请
	public void userAuditingAddGroup(HttpServletRequest req,HttpServletResponse rep)throws Exception{
		String userID=req.getParameter("userID");
		String groupID=req.getParameter("groupID");
		String status=req.getParameter("status");
		jsonMessage jm=dao.userAuditingAddGroupDao(userID,groupID,status);
		deal(jm,rep);
	}
	
	@RequestMapping("/deleteGroupMember") //移出群成员
	public void deleteGroupMember(HttpServletRequest req,HttpServletResponse rep)throws Exception{
		String userID=req.getParameter("userID");
		String groupID=req.getParameter("groupID");
		jsonMessage jm=dao.deleteGroupMemberDao(userID,groupID);
		deal(jm,rep);
	}
	
	
	@RequestMapping("/singOutGroup") //用户退出群
	public void singOutGroup(HttpServletRequest req,HttpServletResponse rep)throws Exception{
		String userID=req.getParameter("userID");
		String groupID=req.getParameter("groupID");
		jsonMessage jm=dao.singOutGroupDao(userID,groupID);
		deal(jm,rep);
	}
	
	@RequestMapping("/disbandGroup") //解散群
	public void disbandGroup(HttpServletRequest req,HttpServletResponse rep)throws Exception{
		String groupID=req.getParameter("groupID");
		jsonMessage jm=dao.disbandGroupDao(groupID);
		deal(jm,rep);
	}
	
	@RequestMapping("/attainGroupUserMessageByHxID") //通过环信id获取群用户基本信息
    public void attainGroupUserMessageByHxID(HttpServletRequest req,HttpServletResponse rep) throws Exception
    {
		String groupHXID=req.getParameter("groupHXID");
		jsonMessage jm=dao.attainGroupUserMessageByHxIdDao(groupHXID);   	
		deal(jm,rep);
    }
	
	@RequestMapping("/attainMessageNum") //查询带查看的各消息的总条数
	public void attainMessageNum(HttpServletRequest req,HttpServletResponse rep)throws Exception{
		String userID=req.getParameter("userID");
		jsonMessage jm=dao.attainMessageNumDao(userID);
		deal(jm,rep);
	}
	
	@RequestMapping("/attainEntryResult") //查询入群结果通知
	public void attainEntryResult(HttpServletRequest req,HttpServletResponse rep)throws Exception{
		String userID=req.getParameter("userID");
		jsonMessage jm=dao.attainEntryResultDao(userID);
		deal(jm,rep);
	}
	
	@RequestMapping("/attainEntryApplication") //群主查询入群申请通知
	public void attainEntryApplication(HttpServletRequest req,HttpServletResponse rep)throws Exception{
		String userID=req.getParameter("userID");
		jsonMessage jm=dao.attainEntryApplicationDao(userID);
		deal(jm,rep);
	}
	
	@RequestMapping("/attainAddUser") //获取群主邀请入群请求
	public void attainAddUser(HttpServletRequest req,HttpServletResponse rep)throws Exception{
		String userID=req.getParameter("userID");
		jsonMessage jm=dao.attainAddUserDao(userID);
		deal(jm,rep);
	}
	
	//获取特定群信息
	@RequestMapping("/obtainOneGroupId")
    public void obtainOneGroupId(HttpServletRequest req,HttpServletResponse rep) throws Exception
    {
    	String userID=req.getParameter("userID");
    	String groupHXID=req.getParameter("groupHXID");
		jsonMessage jm=dao.obtainOneGroupIdDao(userID,groupHXID);   	
		deal(jm,rep);
    }
	
	@RequestMapping("/attainSystem") //获取系统公告
	public void attainSystem(HttpServletRequest req,HttpServletResponse rep)throws Exception{
		String userID=req.getParameter("userID");
		jsonMessage jm=dao.attainSystemDao(userID);
		deal(jm,rep);
	}
	
	@RequestMapping("/attainMessageLast") //保存通讯最后一条讯息
	public void attainMessageLast(HttpServletRequest req,HttpServletResponse rep)throws Exception{
		String userIdOne=req.getParameter("userIdOne");
		String userIdTwo=req.getParameter("userIdTwo");
		String message =req.getParameter("message");
		String date=req.getParameter("date");
		jsonMessage jm=dao.attainMessageLastDao(userIdOne,userIdTwo,message,date);
		deal(jm,rep);
	}
	
	public void deal(jsonMessage jm,HttpServletResponse rep) throws Exception{
    	jsonOb=JSONObject.fromObject(jm); 
    	//System.out.println(jsonOb);
    	rep.setCharacterEncoding("UTF-8");
		rep.setContentType("application/json");	
    	PrintWriter writer = rep.getWriter();
		writer.println(jsonOb);
	    writer.flush();
	    writer.close();
    }
}
