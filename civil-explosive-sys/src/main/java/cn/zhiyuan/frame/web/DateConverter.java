package cn.zhiyuan.frame.web;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;

import cn.zhiyuan.frame.CommonUtils;


public class DateConverter implements Converter<String, Date> {

	protected static Log log = LogFactory.getLog(DateConverter.class);
	
	@Override
	public Date convert(String arg0) {
		//log.debug("转换器:" + arg0);
		if(StringUtils.isEmpty(arg0)) return null;
		int len =arg0.length();
		String pattern = null;
		/*
		 * yyyy-MM-dd 10
		 * yyyy-MM-dd HH:mm 16
		 * yyyy-MM-dd HH:mm:ss 19
		 */
		if(len == 10) pattern = "yyyy-MM-dd";
		else if(len == 16) pattern = "yyyy-MM-dd HH:mm";
		else if(len == 19) pattern = "yyyy-MM-dd HH:mm:ss";
		else if(len == 5 && arg0.indexOf(":") != -1) pattern = "HH:mm";
		else if(len == 21) pattern = "yyyy-MM-dd HH:mm:ss.S";
		if(pattern != null){
			return CommonUtils.str2date(arg0, pattern);
		}
		return null;
	}

}
