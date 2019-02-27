package cn.zhiyuan.ces.base.web;

import cn.zhiyuan.ces.base.biz.IAuditStepBiz;
import cn.zhiyuan.ces.base.entity.AuditStep;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.zhiyuan.frame.*;


@Controller
@RequestMapping("/base")
public final class AuditStepAction extends BaseAction{

	@Autowired
	private IAuditStepBiz iAuditStepBiz;

	@RequestMapping("/auditstep/jsons") 
	public ModelAndView json(PageBean<AuditStep> pb,AuditStep auditStep) {
		iAuditStepBiz.getPageList(pb,auditStep,null);
		return json(pb);
	}

	@RequestMapping("/auditstep/save") 
	public ModelAndView save(MsgBean mbean,AuditStep auditStep) {
		mbean.setInfo("保存成功");
		iAuditStepBiz.save(auditStep);
		return json(mbean);
	}

	@RequestMapping("/auditstep/delete") 
	public ModelAndView delete(MsgBean mbean,AuditStep auditStep) {
		mbean.setInfo("删除成功");
		iAuditStepBiz.delete(auditStep);
		return json(mbean);
	}

}
