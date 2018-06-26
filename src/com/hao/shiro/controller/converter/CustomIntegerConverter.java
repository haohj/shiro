package com.hao.shiro.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

public class CustomIntegerConverter implements Converter<String, Integer> {

	@Override
	public Integer convert(String source) {
		try {
			return Integer.parseInt(source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
