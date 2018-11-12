package com.walk.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String userID=request.getParameter("userID");
		FileInputStream fis=new FileInputStream(this.getServletContext().getRealPath("")+"image/"+userID+".png");
		byte[] b=new byte[fis.available()];
		fis.read(b);
		fis.close();
		response.setContentType("image/png");
		ServletOutputStream op=response.getOutputStream();
		op.write(b);
		op.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		if(ServletFileUpload.isMultipartContent(request)) {
			Part part=request.getPart("image");
			InputStream is=part.getInputStream();
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			byte[] b=new byte[1024];
			while(is.read(b)>0) {
				baos.write(b);
			}
			b=baos.toByteArray();
			FileOutputStream fos=new FileOutputStream(this.getServletContext().getRealPath("")+"image/"+"phone"+".png");
			System.out.println(this.getServletContext().getRealPath("")+"image/"+"phone"+".png");
			fos.write(b);
			fos.close();
		}
		
		doGet(request, response);
	}

}
