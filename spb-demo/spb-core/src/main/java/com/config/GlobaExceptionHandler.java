package com.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常捕捉
 * @author Administrator
 *
 */
@ControllerAdvice
public class GlobaExceptionHandler {
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String,String>> globaException(HttpServletRequest request,Exception exception){
		Map<String,String> map = new HashMap<>();
		map.put("code","500");
		map.put("result","error");
		map.put("message","网络高峰期，等待红绿灯！");		
		return new ResponseEntity<Map<String,String>>(map, HttpStatus.OK);
	}
}
