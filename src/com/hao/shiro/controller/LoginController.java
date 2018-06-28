package com.hao.shiro.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hao.shiro.controller.exceptionResolver.CustomException;
import com.hao.shiro.po.ActiveUser;
import com.hao.shiro.service.SysService;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 
 * <p>Title: LoginController</p>
 * <p>Description: 用户登陆</p>
 * <p>Company: www.itcast.com</p> 
 * @author	传智.燕青
 * @date	2015-2-9下午5:42:03
 * @version 1.0
 */
@Controller
public class LoginController {
	
	@Autowired
	private SysService sysService;

	//用户登陆提交
	@RequestMapping("/login")
	public String login( HttpServletRequest request) throws Exception{
		// shiro在认证过程中出现错误后将异常类路径通过request返回
		String exceptionClassName = (String) request
				.getAttribute("shiroLoginFailure");
		if(exceptionClassName!=null){
			if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
				throw new CustomException("账号不存在");
			} else if (IncorrectCredentialsException.class.getName().equals(
					exceptionClassName)) {
				throw new CustomException("用户名/密码错误");
			} else if("randomCodeError".equals(exceptionClassName)){
				throw new CustomException("验证码错误");
			} else{
				throw new Exception();//最终在异常处理器生成未知错误
			}
		}
		return "login";
	}
	//退出
	@RequestMapping("/logout")
	public String logout(HttpSession httpSession) throws Exception{
		//清空session
		httpSession.invalidate();
		
		return "redirect:first.action";
	}
}
