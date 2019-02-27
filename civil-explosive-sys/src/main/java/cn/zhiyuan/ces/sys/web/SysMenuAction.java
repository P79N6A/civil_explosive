package cn.zhiyuan.ces.sys.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.zhiyuan.ces.sys.biz.ISysMenuBiz;
import cn.zhiyuan.ces.sys.entity.SysMenu;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.PageBean;


@Controller
@RequestMapping("/sys")
public final class SysMenuAction extends BaseAction{

	@Autowired
	private ISysMenuBiz iSysMenuBiz;

	@RequestMapping("/sysmenu/json") 
	public ModelAndView json(PageBean<SysMenu> pb,SysMenu sysMenu) {
		iSysMenuBiz.getPageList(pb,sysMenu,null);
		return json(pb);
	}
	
	@RequestMapping("/sysmenu/jsons") 
	public ModelAndView json(MsgBean mbean,SysMenu sysMenu) {
		mbean.setObj(iSysMenuBiz.getList(sysMenu,"order_num asc"));
		return json(mbean);
	}

	//调整顺序
	@RequestMapping("/sysmenu/order") 
	public ModelAndView order(MsgBean mbean,Integer id1,Integer order1,Integer id2,Integer order2) {
		mbean.setRetInfo(0, "设置成功 !");
		if(id1 == null  || order1 == null 
				|| id2 == null || order2 == null){
			mbean.setRetInfo(-1,"参数不正确 !");
		}else{
			iSysMenuBiz.orderSysMenu(mbean,new SysMenu(id1,order1),new SysMenu(id2,order2));
		}
		return json(mbean);
	}
		
	@RequestMapping("/sysmenu/save") 
	public ModelAndView save(MsgBean mbean,SysMenu sysMenu) {
		mbean.setInfo("保存成功");
		iSysMenuBiz.save(sysMenu);
		return json(mbean);
	}

	@RequestMapping("/sysmenu/delete") 
	public ModelAndView delete(MsgBean mbean,SysMenu sysMenu) {
		mbean.setInfo("删除成功");
		iSysMenuBiz.delete(sysMenu);
		return json(mbean);
	}

}
