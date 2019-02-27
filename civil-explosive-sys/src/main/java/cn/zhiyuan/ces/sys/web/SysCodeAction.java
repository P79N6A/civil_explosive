package cn.zhiyuan.ces.sys.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.zhiyuan.ces.sys.biz.ISysCodeBiz;
import cn.zhiyuan.ces.sys.biz.ISysMenuBiz;
import cn.zhiyuan.ces.sys.entity.SysCode;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.PageBean;


@Controller
@RequestMapping("/sys")
public final class SysCodeAction extends BaseAction{

	@Autowired
	private ISysCodeBiz iSysCodeBiz;

	@Autowired
	private ISysMenuBiz iSysMenuBiz;

	@RequestMapping("/syscode/json") 
	public ModelAndView json(PageBean<SysCode> pb,SysCode sysCode) {
		iSysCodeBiz.getPageList(pb,sysCode,"t.code_value asc");
		return json(pb);
	}

	@RequestMapping("/syscode/jsons") 
	public ModelAndView jsons(MsgBean mbean,SysCode sysCode) {
		mbean.setObj(iSysCodeBiz.getList(sysCode,"t.code_value asc"));
		return json(mbean);
	}
	
	@RequestMapping("/syscode/save") 
	public ModelAndView save(MsgBean mbean,SysCode sysCode) {
		mbean.setInfo("保存成功");
		if(sysCode.getCodeId() == null) {
			iSysCodeBiz.add(sysCode);
		}else {
			iSysCodeBiz.updateWithNull(sysCode);
		}
		return json(mbean);
	}

	@RequestMapping("/syscode/delete") 
	public ModelAndView delete(MsgBean mbean,SysCode sysCode) {
		mbean.setInfo("删除成功");
		iSysCodeBiz.delete(sysCode);
		return json(mbean);
	}

	//orm测试
    @RequestMapping("/ormtest")
    public ModelAndView convert(MsgBean mbean,String json) {
        mbean.setRetInfo(0, "导出成功!");
        iSysMenuBiz.ormtest();
        return json(mbean);
    }
    
}
