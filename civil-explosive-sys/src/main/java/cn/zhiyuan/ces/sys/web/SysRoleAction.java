package cn.zhiyuan.ces.sys.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.zhiyuan.ces.sys.biz.ISysRoleBiz;
import cn.zhiyuan.ces.sys.entity.SysRole;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.CommonUtils;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.PageBean;


@Controller
@RequestMapping("/sys")
public final class SysRoleAction extends BaseAction{

	@Autowired
	private ISysRoleBiz iSysRoleBiz;
	
	@RequestMapping("/sysrole/json") 
	public ModelAndView json(PageBean<SysRole> pb,SysRole sysRole) {
		iSysRoleBiz.getPageList(pb,sysRole,"t.order_num asc");
		return json(pb);
	}

	@RequestMapping("/sysrole/jsons") 
	public ModelAndView json(MsgBean mbean,SysRole sysRole) {
		mbean.setObj(iSysRoleBiz.getList(sysRole,"order_num asc"));
		return json(mbean);
	}

	//注册账号所属角色
	@RequestMapping("/sysrole/jsons4Reg") 
	public ModelAndView jsons4Reg(MsgBean mbean,Integer roleType) {
		mbean.setObj(iSysRoleBiz.role4Reg(roleType));
		return json(mbean);
	}
	
	//角色权限列表json
	@RequestMapping("/sysrole/saveRoleAuth") 
	public ModelAndView saveRoleAuth(MsgBean mbean,Integer roleId,String menuIds) {
		mbean.setRetInfo(0, "角色授权成功 !");
		iSysRoleBiz.saveSysRoleAuth(roleId, CommonUtils.str2IntList(menuIds));
		return json(mbean);
	}
		
	@RequestMapping("/sysrole/save") 
	public ModelAndView save(MsgBean mbean,SysRole sysRole) {
		mbean.setInfo("保存成功");
		iSysRoleBiz.save(sysRole);
		return json(mbean);
	}

	@RequestMapping("/sysrole/delete") 
	public ModelAndView delete(MsgBean mbean,SysRole sysRole) {
		mbean.setInfo("删除成功");
		iSysRoleBiz.delete(sysRole);
		return json(mbean);
	}

}
