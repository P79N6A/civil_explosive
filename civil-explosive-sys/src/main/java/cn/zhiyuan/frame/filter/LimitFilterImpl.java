package cn.zhiyuan.frame.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.zhiyuan.frame.MsgBean;

public class LimitFilterImpl implements IAuthFilter {

	private static  Log log = LogFactory.getLog(LimitFilterImpl.class);
	
	//免登录权限列表
	static protected List<Pattern> nologin = new Vector<Pattern>();
	//仅登录权限列表
	static protected List<Pattern> nolimit = new Vector<Pattern>();
		
	@Override
	public void destroy() {
		nologin.clear();
		nolimit.clear();
	}

	/*
	 * 过滤url
	 * */
	private String filterURL(String url) {
		url = url.trim();
		return (!"/".equals(url)
					&& !url.endsWith(".do")) ? url + "\\.do":url;
	}
	
	@Override
	public void init(FilterConfig fConfig) {
		String tmp = fConfig.getInitParameter("nologin");
		if(tmp == null) tmp = "";
		for(String t :tmp.split(",")){
			nologin.add(Pattern.compile(filterURL(t)));
		}
		tmp = fConfig.getInitParameter("nolimit");
		if(tmp == null) tmp = "";
		for(String t :tmp.split(",")){
			nolimit.add(Pattern.compile(filterURL(t)));
		}
		//log.debug("nologin:" + nologin);
		//log.debug("nolimit:" + nolimit);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doFilter(FilterBean fbean, 
			FilterChain chain) throws IOException, ServletException {
		String ret = SysFilter.OK_FILTER;
		MsgBean mbean = fbean.getMbean();
		String ctrluri = fbean.getUri();
		HttpSession hseesion = fbean.getSession();
		Object obj = hseesion.getAttribute(SysFilter.USER);
		//获取当前session的权限集合
		List<Pattern> needlimit = (List<Pattern>)hseesion.getAttribute(SysFilter.MENUS);
		if(null == needlimit){
			//如果为空
			needlimit = new ArrayList<Pattern>();
			hseesion.setAttribute(SysFilter.MENUS,needlimit);
		}
		if(!FilterUtils.checkURI(nologin, ctrluri)){
			if(obj == null)	{//验证是否登录
				ret = "nologin"; 
				mbean.setRetInfo(-404, "请先登录系统 !");
			} else{
				if(!FilterUtils.checkURI(nolimit, ctrluri)){//登录即可的
					if(!FilterUtils.checkURI(needlimit, ctrluri)){//正式验证权限
						ret = "NOLIMIT";
						mbean.setRetInfo(-505, "没有访问权限 !");
					}
				}
			}
		}
		return ret;
	}
	
	//设置权限
	@SuppressWarnings("unchecked")
	public static void  setNeedLimit(HttpSession session,List<String> menuList){
		List<Pattern> needlimit = (List<Pattern>)session.getAttribute(SysFilter.MENUS);
		if(menuList == null) return;
		needlimit.clear();//清空
		for(String url :menuList){
			if(StringUtils.isNotEmpty(url))
				needlimit.add(Pattern.compile(url));
		}
	}

}
