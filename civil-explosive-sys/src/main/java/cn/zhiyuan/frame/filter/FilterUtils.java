package cn.zhiyuan.frame.filter;

import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

//工具类
public class FilterUtils {
	
	/*
	 * 获取request请求类型 jsp xml or json 取消xml格式
	 */
	public static String getAcceptType(HttpServletRequest request){
		String type = request.getHeader("accept");
		if(type == null) return "json";
		type = type.toLowerCase();
		if(type.indexOf("json") != -1) return "JSON";
		if(type.indexOf("xml") != -1) return "JSP";
		return "JSON";
	}
	
	//获取资源大小
	public static int getContentLength(HttpServletRequest httprequest){
		String len = httprequest.getHeader("Content-Length");
		if(len == null) return 0;
		else return Integer.parseInt(len);
	}
		
	///匹配验证
	public static boolean checkURI(List<Pattern> ptnlist,String name){
		for(Pattern p : ptnlist){
			if(p.matcher(name).matches()) 
				return true;
		}
		return false;
	}
	
}
