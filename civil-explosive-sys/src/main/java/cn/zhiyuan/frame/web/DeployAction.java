package cn.zhiyuan.frame.web;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.CommonUtils;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.filter.AbstractFilter;
import cn.zhiyuan.frame.filter.ResourcePack;

/*
 * 项目自动部署
 * 资源请求请求
 * */
@Controller  
@RequestMapping("/")  
public class DeployAction extends BaseAction{
	
	public static final String SESSION_SECURITY_CODE = "SESSION_SECURITY_CODE";//验证码

	@Autowired
	private OrmBiz ormbiz;
	
	@RequestMapping("/index")
	public ModelAndView index(ModelAndView mav,HttpSession session) {
		mav.setViewName("shell");
		//服务器时间戳
		Date date = new Date();
		mav.addObject("serverTime", date.getTime());
		mav.addObject("serverTimeStr", CommonUtils.date2str(date, "yyyy,MM,dd,HH,mm,ss,SSS"));
		Object user = session.getAttribute(AbstractFilter.USER);
		String indexJs = null,indexJsp = null;
		if(user == null) {
			indexJs = "js/sys/login.js";
			indexJsp = "sys/login.jsp";
		}else {
			indexJs = "js/sys/index.js";
			indexJsp = "sys/index.jsp";
		}
		mav.addObject("indexJs", indexJs);
		mav.addObject("indexJsp", indexJsp);
		return mav;
	}

	// 获取验证码
	@RequestMapping("/logincode")
	public ModelAndView loginCode(HttpSession session) {
		Object[] results = CommonUtils.drawImg();
		String code = results[0].toString();
		log.debug("--======验证码========" + code);
		session.setAttribute(SESSION_SECURITY_CODE, code);
		byte[] picbyte = (byte[])results[1];
		return down(picbyte, JPEG_MIME);
	}

	@RequestMapping("/test")
	public ModelAndView test(MsgBean mbean, HttpServletRequest request
			, HttpServletResponse response) throws ServletException, IOException {
		mbean.setInfo("注销登录");
		log.debug(request);
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("a.gif");
		log.debug(requestDispatcher);
		return null;
	}

	@RequestMapping("/logout")
	public ModelAndView logout(MsgBean mbean, HttpSession session) {
		mbean.setInfo("注销登录");
		session.invalidate();//设置无效
		return json(mbean);
	}

	/*
	 * 跳转指定模块
	 * */
	@RequestMapping("/loadui")
	public ModelAndView loadUI(ModelAndView mav,String name) {
		mav.setViewName("ui");
		Date date = new Date();
		mav.addObject("serverTimeStr", CommonUtils.date2str(date, "yyyy,MM,dd,HH,mm,ss,SSS"));
		mav.addObject("indexJs", name);
		return mav;
	}

	/*
	 * 加载 打包css文件
	 * */
	@RequestMapping("css//loadCss")
	public ModelAndView loadCss() {
		ResourcePack packRes = new ResourcePack(super.getHttpRequest().getServletContext());
        return down(packRes.loadCss(),CSS_MIME);
	}
	
	/*
	 * 加载 打包javascript文件
	 * */
	@RequestMapping("/loadJs")
	public ModelAndView loadJs() {
		//log.debug(super.getHttpRequest() + "," + super.getHttpResponse());
		ResourcePack packRes = new ResourcePack(super.getHttpRequest().getServletContext());
        return down(packRes.loadJs(),JS_MIME);
	}
	
	/*
	 * 动态加载jsp页面
	 * */
	@RequestMapping("/loadJsp")
	public ModelAndView loadJsp(ModelAndView mav,String name) {
		if(StringUtils.isEmpty(name)) name = "sys/404";
		name = name.replace(".jsp","");
		mav.setViewName(name);
		return mav;
	}

	/*
	 * 上传war文件，并重启服务
	 * */
	@RequestMapping("/deploy")
	public ModelAndView uploadFile(MsgBean mbean,String servName,String appName
			,@RequestParam MultipartFile warfile) {
		String warpath = ormbiz.writeWarFile(mbean,warfile);
		if(warpath == null) return json(mbean);
		ormbiz.restart(warpath,appName,servName,super.getRealPath("/WEB-INF/war/win.js"),
				super.getRealPath("/WEB-INF/war/linux.sh"));//启动服务
		return html(JSON.toJSONString(mbean));
	}
	
    @RequestMapping("/ormjsons")
    public ModelAndView jsons(MsgBean mbean,String json) {
        mbean.setObj(ormbiz.getTables());
        return json(mbean);
    }

    //转换保存到磁盘指定路径
    @RequestMapping("/convert")
    public ModelAndView convert(MsgBean mbean,String json) {
        mbean.setRetInfo(0, "导出成功!");
        List<ConvertInfo> infolist = JSON.parseArray(json, ConvertInfo.class);
        ormbiz.convert(infolist);
        return json(mbean);
    }
    
}
