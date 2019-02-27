package cn.zhiyuan.frame.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

/*
 * 继承servlet filter
 */
final public class SysFilter extends AbstractFilter {
	
	
	public static final String MENUS = "MENUS";//用户菜单key
	
	public void init(FilterConfig fc) throws ServletException {
		super.init(fc);
	}
	
	public SysFilter(){
		//上传附件过滤器
		//regFilter(new UpLoadFilterImpl());
		//访问权限过滤器
		super.regFilter(new LimitFilterImpl());
		
		super.regFilter(new ResourceFilterImpl());
	}
}
