package cn.zhiyuan.frame.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResourceFilterImpl implements IAuthFilter {

	private static Log log = LogFactory.getLog(ResourceFilterImpl.class);   
	
	public static final String DEPLOY_MODE = "DEPLOY.MODE";
	
	@Override
	public String doFilter(FilterBean fbean, FilterChain chain) 
			throws IOException, ServletException {
		return AbstractFilter.GOON_FILTER;
	}

	@Override
	public void destroy() {}

	@Override
	public void init(FilterConfig fConfig) {
		ServletContext sc = fConfig.getServletContext();
		String deployMode = System.getProperty(DEPLOY_MODE);
		//根据开发模式打包js ,css
		if("1".equals(deployMode)) {//发布模式
			ResourcePack packResource = new ResourcePack(sc);
			packResource.packJsCss();
			log.debug("当前为发布模式!");
		}else{//开发者模块
			log.debug("当前为开发者模式!");
			sc.setAttribute("PACK_JS","loadJs.do");
			sc.setAttribute("PACK_CSS","css/loadCss.do");
		}
	}

}
