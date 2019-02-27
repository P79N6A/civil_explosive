package cn.zhiyuan.frame.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;

import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.CommonUtils;
import cn.zhiyuan.frame.MsgBean;


/*
 * 系统过滤器
 * 实现权限拦截
 * 基本日志记录
 */
abstract public class AbstractFilter implements Filter {

	protected  Log log = LogFactory.getLog(this.getClass());
	
	public static final String USER = "SESSION_USER";//用户key
	
	private static ServletContext sc;
	/*
	 * 过滤器状态汇总:
	 * */
	public static final String OK_FILTER = "OK_Filter";//通过当前过滤器，中止继续,并执行请求链
	public static final String GOON_FILTER = "Goon_Filter";//通过并继续下一个过滤器
	public static final String Exp_FILTER = "Exp_Filter";//过滤器中出现异常
	
	//过滤器责任链
	private List<IAuthFilter> filterList = new ArrayList<IAuthFilter>();
	
	//注册过滤器责任链
	protected void regFilter(IAuthFilter authFilter){
		if(authFilter != null)
			filterList.add(authFilter);
	}
	
	/*
	 * 过滤请求
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String ret = OK_FILTER;//默认值
		/*
		 * 初始化FilterBean
		 * */
		MsgBean  mbean = new MsgBean();
		FilterBean fbean = new FilterBean(mbean, request, response);
		long d1 = System.currentTimeMillis();
		log.warn("IN=" + fbean.getUri() + "=" + request.getRemoteAddr() 
				+ ":" + request.getRemotePort() + "="+ fbean.getRequest()
						.getSession().getId());
		try{
			for(IAuthFilter f : filterList){
				ret = f.doFilter(fbean, chain);
				if(OK_FILTER.equals(ret)) {
					chain.doFilter(request, response);
					break;
				}else if(GOON_FILTER.equals(ret)) continue;
				else break;
			}
		}catch(Exception ex){
			ret = "EXCEPTION";
			mbean.setRetInfo(-501, "发生异常 !" + ex.getLocalizedMessage());
			log.error(CommonUtils.log4stack(ex));//异常信息到页面
		}
		/*
		 * 没有通过过滤器验证,更加文档类型返回指定格式
		 * */
		if(!OK_FILTER.equals(ret)){
			if(fbean.isJsp()){
				request.setAttribute("mbean", mbean);
				String forwardurl = "/WEB-INF/error.jsp";
				request.getRequestDispatcher(forwardurl)
						.forward(request, response);
			}else{//json格式
				CommonUtils.responseStream(fbean.getResponse()
						,JSON.toJSONString(mbean).getBytes(),BaseAction.JSON_MIME);
			}
		}
		long d2 = System.currentTimeMillis();
		log.warn("OUT=" + fbean.getUri() +"=" + (d2 - d1) + "ms=" 
				+ ret + "="+ mbean.getInfo());
	}
	
	/*
	 * 初始化时执行
	 */
	@Override
	public void init(FilterConfig fc) throws ServletException {
		sc = fc.getServletContext();
		for(IAuthFilter f : filterList){
			f.init(fc);
		}
	}
	
	/*
	 * 销毁时执行
	 */
	@Override
	public void destroy() {
		for(IAuthFilter f : filterList){
			f.destroy();
		}
	}

	public static ServletContext getSc() {
		return sc;
	}

}
