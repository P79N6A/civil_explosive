package cn.zhiyuan.ces.base.web;

import cn.zhiyuan.ces.base.biz.IWorkinguintTypeBiz;
import cn.zhiyuan.ces.base.entity.WorkinguintType;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.zhiyuan.frame.*;


@Controller
@RequestMapping("/base")
public final class WorkinguintTypeAction extends BaseAction{

	@Autowired
	private IWorkinguintTypeBiz iWorkinguintTypeBiz;

	@RequestMapping("/workinguinttype/json") 
	public ModelAndView json(PageBean<WorkinguintType> pb,WorkinguintType workinguintType) {
		iWorkinguintTypeBiz.getPageList(pb,workinguintType,null);
		return json(pb);
	}

	@RequestMapping("/workinguinttype/save") 
	public ModelAndView save(MsgBean mbean,WorkinguintType workinguintType) {
		mbean.setInfo("保存成功");
		iWorkinguintTypeBiz.save(workinguintType);
		return json(mbean);
	}

	@RequestMapping("/workinguinttype/delete") 
	public ModelAndView delete(MsgBean mbean,WorkinguintType workinguintType) {
		mbean.setInfo("删除成功");
		iWorkinguintTypeBiz.delete(workinguintType);
		return json(mbean);
	}

}
