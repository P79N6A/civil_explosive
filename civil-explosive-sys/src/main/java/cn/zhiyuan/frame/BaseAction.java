package cn.zhiyuan.frame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/*
 * 负责与前台交互
 * 封装通用方法
 * */
public abstract class BaseAction {
 
	protected  Log log = LogFactory.getLog(this.getClass());
	
	//mime常量
	final public static String JPEG_MIME = "image/jpeg";
	final public static String GIF_MIME = "image/gif";
	final public static String PNG_MIME = "image/png";
	final public static String JSON_MIME = "application/json; charset=utf-8";
	final public static String XLS_MIME = "application/vnd.ms-excel";
	final public static String WORD_MIME = "application/vnd.ms-word";
	final public static String PDF_MIME = "application/pdf";
	final public static String APK_MIME = "application/apk";
	final public static String HTML_MIME = "text/html; charset=utf-8";
	final public static String JS_MIME = "application/javascript";
	final public static String CSS_MIME = "text/css";
	final public static String WAVE_MIME = "audio/x-wav";
	final public static String FORM_MIME = "application/x-www-form-urlencoded";
	final public static String GZIP_MIME = "application/x-gzip";
	
	/*
	 * 忽略参数中id
	 * */
	@InitBinder("msgBean") 
    public void initBinder(WebDataBinder binder) {    
        binder.setDisallowedFields("id");
    }   
	
	/*
	 * 解析浏览器参数为JSON对象
	 * 数组则逗号分割
	 * */
	public static JSONObject parseParams2Json(WebRequest webRequest) {
		Map<String,String[]> paramMap = webRequest.getParameterMap();
		JSONObject json = new JSONObject();
		for (String key : paramMap.keySet()) {
			String[] values = paramMap.get(key);
			json.put(key,String.join(",",values));
		}
		return json;
	}

	//返回字符串到浏览器
	protected static ModelAndView html(String html){
		try {
			responseStream(html.getBytes("utf-8"),HTML_MIME);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	//返回json格式
	public static ModelAndView json(Object obj){
		try {
			String json_str = JSON.toJSONString(obj,SerializerFeature.WriteMapNullValue);
			responseStream(json_str.getBytes("utf-8"),JSON_MIME);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//返回json格式
	public static ModelAndView jsonGBK(Object obj){
		try {
			String json_str = JSON.toJSONString(obj,SerializerFeature.WriteMapNullValue);
			responseStream(json_str.getBytes("GBK"),JSON_MIME);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	//下载资源
	public static ModelAndView down(byte [] body,String type) {
		responseStream(body,type);
		return null;
	}
	
	/*
	 * 另存为...
	 * 参数:
	 * 	body资源流
	 *  type content-type
	 *  fileName 文件名称
	 * */
	public ModelAndView downFile(File file,String type,String fileName){
		HttpServletResponse response =  getHttpResponse();
		HttpServletRequest request =  getHttpRequest();
		try{
			if(CommonUtils.isFirefox(request)){
				fileName = new String(fileName.getBytes("utf-8"),"iso-8859-1");
			}else {
				fileName = URLEncoder.encode(fileName,"utf-8");
			}
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.setContentType(type);
			response.setContentLength(new Long(file.length()).intValue());
			OutputStream outs = response.getOutputStream();
			FileInputStream fin = new FileInputStream(file);
			byte []body = new byte[4096];
			do {
				int len = fin.read(body);
				if(-1 == len) break;
				outs.write(body, 0, len);
			}while(true);
			fin.close();
			outs.close();
		}catch(IOException ex){
			log.error(CommonUtils.log4stack(ex));
		}
		return null;
	}
	
	/*
	 * 另存为...
	 * 参数:
	 * 	body资源流
	 *  type content-type
	 *  fileName 文件名称
	 * */
	public static ModelAndView downFile(byte[] body,String type,String fileName){
		HttpServletResponse response =  getHttpResponse();
		HttpServletRequest request =  getHttpRequest();
		try{
			if(CommonUtils.isFirefox(request)){
				fileName = new String(fileName.getBytes("utf-8"),"iso-8859-1");
			}else {
				fileName = URLEncoder.encode(fileName,"utf-8");
			}
		}catch(UnsupportedEncodingException ex){}
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		CommonUtils.responseStream(response,body,type);
		return null;
	}
	
	//输出流到客户端
	private static void responseStream(byte [] bpost,String type){
		CommonUtils.responseStream(getHttpResponse(),bpost,type);
	}
	
	protected static HttpServletResponse getHttpResponse() {
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
	}
	
	protected static HttpServletRequest getHttpRequest() {
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	protected static HttpSession getHttpSession() {
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
	}	
	
	protected String getRealPath(String path) {
		HttpServletRequest request = getHttpRequest();
		return request.getServletContext().getRealPath(path);
	}
	
}
