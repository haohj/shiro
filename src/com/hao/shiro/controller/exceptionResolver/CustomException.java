package com.hao.shiro.controller.exceptionResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <p>Title: CustomException</p>
 * <p>Description:系统自定义异常 </p>
 * <p>Company: www.itcast.com</p> 
 * @author	传智.燕青
 * @date	2015-3-7上午10:59:26
 * @version 1.0
 */
public class CustomException extends Exception {

	/** serialVersionUID*/
	private static final long serialVersionUID = -5212079010855161498L;
	
	public CustomException(String message){
		super(message);
		this.message = message;
	}

	//异常信息
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
