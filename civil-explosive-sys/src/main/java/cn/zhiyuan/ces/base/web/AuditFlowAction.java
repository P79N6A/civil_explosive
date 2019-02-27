package cn.zhiyuan.ces.base.web;

import cn.zhiyuan.ces.base.biz.IAuditFlowBiz;
import cn.zhiyuan.ces.base.entity.AuditFlow;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.zhiyuan.frame.*;


@Controller
@RequestMapping("/base")
public final class AuditFlowAction extends BaseAction{

	@Autowired
	private IAuditFlowBiz iAuditFlowBiz;

	@RequestMapping("/auditflow/json") 
	public ModelAndView json(PageBean<AuditFlow> pb,AuditFlow auditFlow) {
		iAuditFlowBiz.getPageList(pb,auditFlow,null);
		return json(pb);
	}

	@RequestMapping("/auditflow/save") 
	public ModelAndView save(MsgBean mbean,AuditFlow auditFlow) {
		mbean.setInfo("保存成功");
		iAuditFlowBiz.save(auditFlow);
		return json(mbean);
	}

	@RequestMapping("/auditflow/delete") 
	public ModelAndView delete(MsgBean mbean,AuditFlow auditFlow) {
		mbean.setInfo("删除成功");
		iAuditFlowBiz.delete(auditFlow);
		return json(mbean);
	}

}
