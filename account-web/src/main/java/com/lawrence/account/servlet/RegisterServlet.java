package com.lawrence.account.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lawrence.account.service.AccountService;
import com.lawrence.account.service.AccountServiceException;
import com.lawrence.account.service.SignUpRequest;

public class RegisterServlet extends HttpServlet{
	
	private static final long serialVersionUID = -2899925245928589004L;
	private AccountService service;
	
	@Override
	public void init() throws ServletException {
		super.init();
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		service = (AccountService) context.getBean("accountService");
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		
		String captchaKey = request.getParameter("key");
		String captchaValue = request.getParameter("captcha");//user input
		
		String uid = request.getParameter("uid");
		String uname = request.getParameter("uname");
		String email = request.getParameter("email");
		String pwd = request.getParameter("pwd");
		String pwdcon = request.getParameter("pwdConfirm");
		
		SignUpRequest signup = new SignUpRequest();
		signup.setId(uid);
		signup.setName(uname);
		signup.setEmail(email);
		signup.setPassword(pwd);
		signup.setPasswordConfirm(pwdcon);
		signup.setCaptchaKey(captchaKey);
		signup.setCaptchaValue(captchaValue);
		
		try {
			boolean isSucceeded = service.register(signup);
			if(isSucceeded){
				request.getRequestDispatcher("/display.jsp").forward(request, response);
			}else{
				response.sendError(400, "Unable to register.");
			}
		} catch (AccountServiceException e) {
			response.sendError(400, "Exception occurs!");
		}
	}

}
