package cn.zhiyuan.ces.base.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.zhiyuan.ces.base.biz.IWorkingPersonBiz;
import cn.zhiyuan.ces.base.entity.AuditFlow;
import cn.zhiyuan.ces.base.entity.WorkingPerson;
import cn.zhiyuan.ces.sys.entity.SysUser;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.PageBean;
import cn.zhiyuan.frame.filter.SysFilter;

@Controller
@RequestMapping("/base")
public final class WorkingPersonAction extends BaseAction{

	public final static String AUDIT_TYPE = "2";//从业人员
		
	@Autowired
	private IWorkingPersonBiz iWorkingPersonBiz;
	
	/*
	 * 单位的从业人员
	 * */
	@RequestMapping("/workingperson/jsons") 
	public ModelAndView json(PageBean<WorkingPerson> pb,WorkingPerson workingPerson,String q) {
		iWorkingPersonBiz.getWorkingPersonList(pb,workingPerson,q);
		return json(pb);
	}
	
	/*
	 * 类型分组统计
	 * */
	@RequestMapping("/workingperson/typeCount") 
	public ModelAndView typeCount(MsgBean mbean,int unitId) {
		mbean.setObj(iWorkingPersonBiz.typeCount(unitId));
		return json(mbean);
	}
	
	/*
	 * 备案
	 * */
	@RequestMapping("/workingperson/save1") 
	public ModelAndView save1(MsgBean mbean,WorkingPerson workingPerson,String uploadIds
			,AuditFlow auditFlow) {
		mbean.setInfo("保存成功");
		workingPerson.setAuditStatus(AuditFlow.AUDIT_STATUS_START);
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		auditFlow.setAuditType(AUDIT_TYPE);//审核类型
		auditFlow.setAuditStep("1");
		auditFlow.setAuditResult(1);
		iWorkingPersonBiz.save(workingPerson,uploadIds,auditFlow);
		return json(mbean);
	}
	/*
	 * 受理
	 * */
	@RequestMapping("/workingperson/save2") 
	public ModelAndView save2(MsgBean mbean,WorkingPerson workingPerson,AuditFlow auditFlow) {
		mbean.setInfo("保存成功");
		workingPerson.setAuditStatus(AuditFlow.AUDIT_STATUS_GOON);
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		auditFlow.setAuditType(AUDIT_TYPE);//审核类型
		auditFlow.setAuditStep("2");
		iWorkingPersonBiz.auditSave(workingPerson,auditFlow);
		return json(mbean);
	}
	
	/*
	 * 审核
	 * */
	@RequestMapping("/workingperson/save3") 
	public ModelAndView save3(MsgBean mbean,WorkingPerson workingPerson,AuditFlow auditFlow) {
		mbean.setInfo("保存成功");
		//审核结果同步
		workingPerson.setAuditStatus(AuditFlow.AUDIT_STATUS_END);
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		auditFlow.setAuditType(AUDIT_TYPE);//审核类型
		auditFlow.setAuditStep("3");
		iWorkingPersonBiz.auditSave(workingPerson,auditFlow);
		return json(mbean);
	}
	
	/*
	 * 查询当前用户所在的单位的从业人员
	 * */
	@RequestMapping("/workingperson/boxJsons") 
	public ModelAndView boxJsons(MsgBean mbean,WorkingPerson workingPerson) {
		if(workingPerson.getUnitId() == null) {
			SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
			workingPerson.setUnitId(user.getUnitId());
		}
		workingPerson.setAuditStatus(3);
		mbean.setObj(iWorkingPersonBiz.getList(workingPerson,"t.id desc"));
		return json(mbean);
	}
	
}
