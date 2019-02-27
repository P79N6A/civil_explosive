package cn.zhiyuan.ces.sys.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import cn.zhiyuan.ces.camera.biz.ICameraConfigBiz;
import cn.zhiyuan.ces.sys.biz.ISysMenuBiz;
import cn.zhiyuan.ces.sys.biz.ISysUserBiz;
import cn.zhiyuan.ces.sys.biz.ITodoTask;
import cn.zhiyuan.ces.sys.entity.SysUser;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.CommonUtils;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.PageBean;
import cn.zhiyuan.frame.filter.SysFilter;
import cn.zhiyuan.frame.web.DeployAction;


@Controller
@RequestMapping("/sys")
public final class SysUserAction extends BaseAction{

	@Autowired
	private ITodoTask itodoTask;
	
	@Autowired
	private ISysUserBiz iSysUserBiz;

	@Autowired
	private ISysMenuBiz iSysMenuBiz;

	@Autowired
	private ICameraConfigBiz sipConfig;
	
	@RequestMapping("/indexJson") 
	public ModelAndView indexJson(HttpSession session,HttpServletRequest request){
		MsgBean mbean = new MsgBean();
		SysUser user = (SysUser)session.getAttribute(SysFilter.USER);
		Map<String,Object> map = new HashMap<>();
		map.put("menuList", iSysMenuBiz.queryMenuByRoleId(user.getUserId()));
		map.put("user", user);
		JSONObject configJson = new JSONObject();
		configJson.put("serverTimeStr", CommonUtils.date2str(new Date(), "yyyy,MM,dd,HH,mm,ss,SSS"));
		String ip = request.getRemoteAddr();
		configJson.put("rtmpRoot", sipConfig.getRtmpUrl(ip.indexOf("192.168.") != -1?true:false));
		map.put("config", configJson);
		mbean.setObj(map);
		//设置用户权限
		//SysBean.getInstance().setNeedLimit(menuList);
		return json(mbean);
	}
	
	//验证有效性
	private boolean checkValid(HttpSession session,MsgBean mbean,String userName,
			String password,String code){
		String oldCode = (String)session
				.getAttribute(DeployAction.SESSION_SECURITY_CODE);
		if(StringUtils.isEmpty(userName)
				|| StringUtils.isEmpty(password)){
			mbean.setRetInfo(-20, "用户帐号或者密码不能为空 !");
			return false;
		}
		if("android_app".equals(code)) return true;
		if(oldCode == null || !oldCode.equals(code)){
			mbean.setRetInfo(-10, "验证码错误 !");
			log.debug("系统:" + code + ",用户:" + oldCode);
			return false;
		}
		return true;
	}
		
	/*
	 * 返回值:
	 * -10 验证码错误
	 * -20 帐号密码或验证码不能为空
	 * -30 密码错误
	 */
	@RequestMapping("/sysuser/login")
	public ModelAndView login(HttpSession session,String userName,String password,String code) {
		MsgBean mbean = new MsgBean();
		if(!checkValid(session,mbean,userName,password,code)) return json(mbean);
		try{
			Object user = iSysUserBiz.login(mbean, userName, password);
			session.setAttribute(SysFilter.USER,user);
			mbean.setObj(user);
		}catch(Exception e){
			e.printStackTrace();
			mbean.setRetInfo(-3, "timeout");
		}
		return json(mbean);
	}
	
	@RequestMapping("/sysuser/loginGBK")
	public ModelAndView loginGBK(ModelAndView mav,HttpSession session,String userName,String password,String code) {
		MsgBean mbean = new MsgBean();
		if(!checkValid(session,mbean,userName,password,code)) return json(mbean);
		try{
			Object user = iSysUserBiz.login(mbean, userName, password);
			session.setAttribute(SysFilter.USER,user);
			mbean.setObj(user);
		}catch(Exception e){
			e.printStackTrace();
			mbean.setRetInfo(-3, "timeout");
		}
		return jsonGBK(mbean);
	}
	
	@RequestMapping("/savepassword")
	public ModelAndView savePwd(HttpSession session,String mm1,String mm2) {
		MsgBean mbean = new MsgBean();
		SysUser user = (SysUser)session.getAttribute(SysFilter.USER);
		String pwd1 = user.getPassWord();
		String pwd2 = CommonUtils.encryptSHA(mm1);
		if(pwd1.equals(pwd2) == false) {
			mbean.setRetInfo(-10, "原密码不正确 !");
		}else {
			SysUser u = new SysUser();
			u.setUserId(user.getUserId());
			u.setPassWord(CommonUtils.encryptSHA(mm2));
			iSysUserBiz.update(u);
		}
		return json(mbean);
	}
	
	@RequestMapping("/sysuser/jsons") 
	public ModelAndView jsons(PageBean<SysUser> pb,SysUser sysUser) {
		iSysUserBiz.getPageList(pb,sysUser,null);
		return json(pb);
	}

	//查询从业单位的账号
	@RequestMapping("/sysuser/jsons4Reg") 
	public ModelAndView jsons4Reg(MsgBean mbean,String loginName,Integer roleType) {
		mbean.setObj(iSysUserBiz.getRegAccounts(loginName,roleType));
		return json(mbean);
	}
	
	//注册账号
	@RequestMapping("/sysuser/regAccount") 
	public ModelAndView regAccount(MsgBean mbean,SysUser sysUser) {
		mbean.setInfo("注册成功! ");
		iSysUserBiz.regAccount(mbean, sysUser);
		return json(mbean);
	}
	
	@RequestMapping("/sysuser/save") 
	public ModelAndView save(MsgBean mbean,SysUser sysUser) {
		mbean.setInfo("保存成功");
		if(sysUser.getUserId() == null) {
			if(iSysUserBiz.checkLoginName(sysUser.getLoginName()) > 0){
				mbean.setRetInfo(-2, SysUser.ERROR_MSG);
				return json(mbean);
			}
			sysUser.setRegTime(new Date());
			sysUser.setPassWord(SysUser.DEFAULT_MM);
		}
		iSysUserBiz.saveUser(sysUser);
		return json(mbean);
	}

	@RequestMapping("/sysuser/stopUser") 
	public ModelAndView stopUser(MsgBean mbean,SysUser sysUser) {
		if(sysUser.getUserId() == null){
			mbean.setRetInfo(-1,  "参数不正确 !");
		}else{
			SysUser user = new SysUser();
			user.setUserId(sysUser.getUserId());
			user.setStatus(0);
			iSysUserBiz.update(user);
			mbean.setRetInfo(0, "操作成功! ");
		}
		return json(mbean);
	}
	
	@RequestMapping("/sysuser/pwdReset") 
	public ModelAndView pwdReset(MsgBean mbean,SysUser sysUser) {
		if(sysUser.getUserId() == null){
			mbean.setRetInfo(-1,  "参数不正确 !");
		}else{
			SysUser user = new SysUser();
			user.setUserId(sysUser.getUserId());
			user.setPassWord(SysUser.DEFAULT_MM);//重置密码
			iSysUserBiz.update(user);
			mbean.setRetInfo(0, "操作成功! ");
		}
		return json(mbean);
	}
	
	@RequestMapping("/sysuser/delete") 
	public ModelAndView delete(MsgBean mbean,SysUser sysUser) {
		mbean.setInfo("删除成功");
		iSysUserBiz.delete(sysUser);
		return json(mbean);
	}

	//待办任务
	@RequestMapping("/sysuser/todoTask") 
	public ModelAndView todoTask(MsgBean mbean) {
		mbean.setInfo("删除成功");
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		itodoTask.todoTask(mbean,user);
		return json(mbean);
	}
	
}
