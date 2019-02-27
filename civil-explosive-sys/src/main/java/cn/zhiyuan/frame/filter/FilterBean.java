package cn.zhiyuan.frame.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

import cn.zhiyuan.frame.MsgBean;
/*
 * 内部类
 * 基本数据封装
 * Msgbean,request,response,url,type
 * */
public final class FilterBean {
	public FilterBean(MsgBean mbean,ServletRequest rq,
			ServletResponse rp) {
		this.mbean = mbean;
		this.request = (HttpServletRequest)rq;
		this.response = (HttpServletResponse)rp;
		this.uri = request.getRequestURI()
				.replace(request.getContextPath(), "");
		this.type = FilterUtils.getAcceptType(request);
	}
	private MsgBean mbean;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String uri;
	private String type;
	private Object params;
	
	
	
	
	public boolean isJsp() {
		return "JSP".equals(type);
	}
	
	public  HttpSession getSession() {
		return request.getSession();
	}
	
	public MsgBean getMbean() {
		return mbean;
	}
	
	public MsgBean getMbean(int id,String info) {
		mbean.setRetInfo(id, info);
		return mbean;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public String getUri() {
		return uri;
	}
	public String getType() {
		return type;
	}

	public void setParams(Object params) {
		this.params = params;
	}
	
	public JSONObject getParamsJSON() {
		return (params instanceof JSONObject)?(JSONObject)params:new JSONObject();
	}
}
