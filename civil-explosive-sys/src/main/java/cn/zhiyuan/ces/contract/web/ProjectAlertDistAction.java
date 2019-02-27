package cn.zhiyuan.ces.contract.web;

import cn.zhiyuan.ces.contract.biz.IProjectAlertDistBiz;
import cn.zhiyuan.ces.contract.entity.ProjectAlertDist;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.zhiyuan.frame.*;


@Controller
@RequestMapping("/contract")
public final class ProjectAlertDistAction extends BaseAction{

	@Autowired
	private IProjectAlertDistBiz iProjectAlertDistBiz;

	@RequestMapping("/projectalertdist/json") 
	public ModelAndView json(PageBean<ProjectAlertDist> pb,ProjectAlertDist projectAlertDist) {
		iProjectAlertDistBiz.getPageList(pb,projectAlertDist,null);
		return json(pb);
	}

	@RequestMapping("/projectalertdist/save") 
	public ModelAndView save(MsgBean mbean,ProjectAlertDist projectAlertDist) {
		mbean.setInfo("保存成功");
		iProjectAlertDistBiz.save(projectAlertDist);
		return json(mbean);
	}

	@RequestMapping("/projectalertdist/delete") 
	public ModelAndView delete(MsgBean mbean,ProjectAlertDist projectAlertDist) {
		mbean.setInfo("删除成功");
		iProjectAlertDistBiz.delete(projectAlertDist);
		return json(mbean);
	}

}
