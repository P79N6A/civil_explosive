package cn.zhiyuan.ces.contract.web;

import cn.zhiyuan.ces.contract.biz.IProjectTransportLineBiz;
import cn.zhiyuan.ces.contract.entity.ProjectTransportLine;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.zhiyuan.frame.*;


@Controller
@RequestMapping("/contract")
public final class ProjectTransportLineAction extends BaseAction{

	@Autowired
	private IProjectTransportLineBiz iProjectTransportLineBiz;

	@RequestMapping("/projecttransportline/json") 
	public ModelAndView json(PageBean<ProjectTransportLine> pb,ProjectTransportLine projectTransportLine) {
		iProjectTransportLineBiz.getPageList(pb,projectTransportLine,null);
		return json(pb);
	}

	@RequestMapping("/projecttransportline/save") 
	public ModelAndView save(MsgBean mbean,ProjectTransportLine projectTransportLine) {
		mbean.setInfo("保存成功");
		iProjectTransportLineBiz.save(projectTransportLine);
		return json(mbean);
	}

	@RequestMapping("/projecttransportline/delete") 
	public ModelAndView delete(MsgBean mbean,ProjectTransportLine projectTransportLine) {
		mbean.setInfo("删除成功");
		iProjectTransportLineBiz.delete(projectTransportLine);
		return json(mbean);
	}

}
