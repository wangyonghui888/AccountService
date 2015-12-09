package com.lawrence.account.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lawrence.account.service.AccountService;
import com.lawrence.account.service.AccountServiceException;

public class CaptchaImageServlet extends HttpServlet {
	
	private static final long serialVersionUID = 7406226087442440993L;
	private AccountService service;
	
	@Override
	public void init() throws ServletException {
		super.init();
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		service = (AccountService) context.getBean("accountService");
	}
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String captchaKey = request.getParameter("key");
		if(captchaKey == null){
			response.sendError(400, "Unable to find captcha key.");
			return;
		}
		
		try {
			byte[] image = service.generateCaptchaImage(captchaKey);
			response.setContentType("image/jpeg");
			OutputStream out = response.getOutputStream();
			out.write(image);
			out.close();
		} catch (AccountServiceException e) {
			response.sendError(404, e.getMessage());
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
