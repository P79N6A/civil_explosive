package cn.zhiyuan.ces.sys.web;

import cn.zhiyuan.ces.sys.biz.IUserRoleBiz;
import cn.zhiyuan.ces.sys.entity.UserRole;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.zhiyuan.frame.*;


@Controller
@RequestMapping("/sys")
public final class UserRoleAction extends BaseAction{

	@Autowired
	private IUserRoleBiz iUserRoleBiz;

	@RequestMapping("/userrole/json") 
	public ModelAndView json(PageBean<UserRole> pb,UserRole userRole) {
		iUserRoleBiz.getPageList(pb,userRole,null);
		return json(pb);
	}

	@RequestMapping("/userrole/save") 
	public ModelAndView save(MsgBean mbean,UserRole userRole) {
		mbean.setInfo("保存成功");
		iUserRoleBiz.save(userRole);
		return json(mbean);
	}

	@RequestMapping("/userrole/delete") 
	public ModelAndView delete(MsgBean mbean,UserRole userRole) {
		mbean.setInfo("删除成功");
		iUserRoleBiz.delete(userRole);
		return json(mbean);
	}
	
	public static void main(String[] args) {
		//System.out.println("abcd:" + CommonUtils.checkUDPPort(53531));
		String rtmpPath = "2018/08/08/1_1533732846344";
		String prefix = CommonUtils.regx(rtmpPath, "(\\d{4}\\/).+");
		System.out.println(prefix);
	}
}
