package cn.zhiyuan.ces.contract.web;

import cn.zhiyuan.ces.contract.biz.IProjectConstructionSchemeBiz;
import cn.zhiyuan.ces.contract.entity.ProjectConstructionScheme;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.zhiyuan.frame.*;


@Controller
@RequestMapping("/contract")
public final class ProjectConstructionSchemeAction extends BaseAction{

	@Autowired
	private IProjectConstructionSchemeBiz iProjectConstructionSchemeBiz;

	@RequestMapping("/projectconstructionscheme/json") 
	public ModelAndView json(PageBean<ProjectConstructionScheme> pb,ProjectConstructionScheme projectConstructionScheme) {
		iProjectConstructionSchemeBiz.getPageList(pb,projectConstructionScheme,null);
		return json(pb);
	}

	@RequestMapping("/projectconstructionscheme/save") 
	public ModelAndView save(MsgBean mbean,ProjectConstructionScheme projectConstructionScheme) {
		mbean.setInfo("保存成功");
		iProjectConstructionSchemeBiz.save(projectConstructionScheme);
		return json(mbean);
	}

	@RequestMapping("/projectconstructionscheme/delete") 
	public ModelAndView delete(MsgBean mbean,ProjectConstructionScheme projectConstructionScheme) {
		mbean.setInfo("删除成功");
		iProjectConstructionSchemeBiz.delete(projectConstructionScheme);
		return json(mbean);
	}

}
