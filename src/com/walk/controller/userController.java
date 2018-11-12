package com.walk.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.walk.Dao.userDao;
import com.walk.pojo.jsonMessage;

import net.sf.json.JSONObject;

@Controller
public class userController {
	JSONObject jsonOb = null;
	userDao dao=new userDao();
    @RequestMapping("/login") //登陆
    public void login(HttpServletRequest req,HttpServletResponse rep) throws Exception
    {
    	String userName=req.getParameter("userName");
    	if((userName==null)|| (userName.equalsIgnoreCase(""))){
    	}else {
    		userName = new String(userName.getBytes("ISO8859-1"), "UTF-8"); 
    	}
		String password=req.getParameter("password");
		jsonMessage jm=dao.loginDao(userName,password);   	
		deal(jm,rep);
    }
    
    @RequestMapping("/register") //注册
    public void register(HttpServletRequest req,HttpServletResponse rep) throws Exception {
    	String userName=req.getParameter("userName");
		if((userName==null)|| (userName.equalsIgnoreCase(""))){
			
		}else {
			userName = new String(userName.getBytes("ISO8859-1"), "UTF-8"); 
			System.out.println(userName);
		}
		String password=req.getParameter("password");		
		String phone=req.getParameter("phone");
		jsonMessage jm=dao.registerDao(userName,password,phone);
		deal(jm,rep);
    }
    
    @RequestMapping("/checkPhone") //注册时查看手机是否被注册过
    public void checkPhone(HttpServletRequest req,HttpServletResponse rep) throws Exception {	
		String phone=req.getParameter("phone");
		jsonMessage jm=dao.checkPhoneDao(phone);
		System.out.println("checkPhone");
		deal(jm,rep);
    }
    
    @RequestMapping("/forgetPasswordCheckPhone") //忘记密码时查看手机是否被注册过
    public void forgetPasswordCheckPhone(HttpServletRequest req,HttpServletResponse rep) throws Exception{
    	String phone=req.getParameter("phone");
		jsonMessage jm=dao.forgetPasswordCheckPhoneDao(phone);
		deal(jm,rep);
    }
    
    @RequestMapping("/forgetPasswordResetPassword") //密码重置
    public void forgetPasswordResetPassword(HttpServletRequest req,HttpServletResponse rep) throws Exception{
    	String phone=req.getParameter("phone");
    	String newPassword=req.getParameter("password");
		jsonMessage jm=dao.forgetPasswordResetPasswordDao(phone,newPassword);
		deal(jm,rep);
    }
    
    @RequestMapping("/obtainUserData") //获取个人信息
    public void obtainUserData(HttpServletRequest req,HttpServletResponse rep) throws Exception{
    	String userName=req.getParameter("userName");
		jsonMessage jm=dao.obtainUserDataDao(userName);
		deal(jm,rep);
    }
    
//    @RequestMapping("/modifyUserData") //修改个人信息
//    public void modifyUserData(HttpServletRequest req,HttpServletResponse rep) throws Exception{
//    	String userID=req.getParameter("userID"); 
//		String nickName=req.getParameter("nickName");
//		if((nickName==null)|| (nickName.equalsIgnoreCase(""))){			
//		}else {
//			nickName = new String(nickName.getBytes("ISO8859-1"), "UTF-8"); 
//			System.out.println(nickName);
//		}
//		String phone=req.getParameter("phone");	
//		String sex=req.getParameter("sex");
//		jsonMessage jm=dao.modifyUserDataDao(userID,nickName,phone,sex);
//		deal(jm,rep);
//    }
    
    @RequestMapping("/modifyUserData") //修改个人信息
    public void modifyHeadimg(HttpServletRequest req,HttpServletResponse rep,@RequestParam("image") MultipartFile file) throws Exception{
    	req.setCharacterEncoding("utf-8");
    	String userID=req.getParameter("userID");
    	String nickName=req.getParameter("nickName");
		if((nickName==null)|| (nickName.equalsIgnoreCase(""))){			
		}else {
			nickName = new String(nickName.getBytes("ISO8859-1"), "UTF-8"); 
		}
		String phone=req.getParameter("phone");	
		String sex=req.getParameter("sex");
		if((sex==null)|| (sex.equalsIgnoreCase(""))){			
		}else {
			sex = new String(sex.getBytes("ISO8859-1"), "UTF-8"); 
		}
    	String path="E:/eclipse workface/WorldWalk/WebContent/image/";
    	jsonMessage jm=dao.modifyUserDataDao(userID,path,file,nickName,phone,sex);
    	deal(jm,rep);
    }
    
    @RequestMapping("/updateUserState") //更新或添加个人位置信息
    public void updateUserState(HttpServletRequest req,HttpServletResponse rep) throws Exception{
    	String userID=req.getParameter("userID");
    	String Longititude=req.getParameter("Longititude");
    	String Latitude=req.getParameter("Latitude");
    	String Direction=req.getParameter("Dircetion");
    	jsonMessage jm=dao.updateUserStateDao(userID,Longititude,Latitude,Direction);
    	deal(jm,rep);
    }
    
    public void deal(jsonMessage jm,HttpServletResponse rep) throws Exception{
    	jsonOb=JSONObject.fromObject(jm); 
    	System.out.println(jsonOb);
    	rep.setCharacterEncoding("UTF-8");
		rep.setContentType("application/json");	
    	PrintWriter writer = rep.getWriter();
		writer.println(jsonOb);
	    writer.flush();
	    writer.close();
    }
}
