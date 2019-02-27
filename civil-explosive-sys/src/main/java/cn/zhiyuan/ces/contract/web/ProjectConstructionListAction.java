package cn.zhiyuan.ces.contract.web;

import cn.zhiyuan.ces.contract.biz.IProjectConstructionListBiz;
import cn.zhiyuan.ces.contract.entity.ProjectConstructionList;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.zhiyuan.frame.*;


@Controller
@RequestMapping("/contract")
public final class ProjectConstructionListAction extends BaseAction{

	@Autowired
	private IProjectConstructionListBiz iProjectConstructionListBiz;

	@RequestMapping("/projectconstructionlist/json") 
	public ModelAndView json(PageBean<ProjectConstructionList> pb,ProjectConstructionList projectConstructionList) {
		iProjectConstructionListBiz.getPageList(pb,projectConstructionList,null);
		return json(pb);
	}

	@RequestMapping("/projectconstructionlist/save") 
	public ModelAndView save(MsgBean mbean,ProjectConstructionList projectConstructionList) {
		mbean.setInfo("保存成功");
		iProjectConstructionListBiz.save(projectConstructionList);
		return json(mbean);
	}

	@RequestMapping("/projectconstructionlist/delete") 
	public ModelAndView delete(MsgBean mbean,ProjectConstructionList projectConstructionList) {
		mbean.setInfo("删除成功");
		iProjectConstructionListBiz.delete(projectConstructionList);
		return json(mbean);
	}

}
