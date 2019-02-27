package cn.zhiyuan.frame.filter;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

//系统权限过滤器
public interface IAuthFilter {
	
	//过滤器,返回值:OK通过,go on继续验证,其他退出
	String doFilter(FilterBean fbean,
			FilterChain chain) throws IOException, ServletException;
	
	/*
	 * 销毁时执行
	 */
	public void destroy();
	
	public void init(FilterConfig fConfig);
	
}
