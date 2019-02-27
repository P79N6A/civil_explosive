package cn.zhiyuan.ces.base.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.zhiyuan.ces.base.biz.IWorkingUnitBiz;
import cn.zhiyuan.ces.base.entity.AuditFlow;
import cn.zhiyuan.ces.base.entity.WorkingUnit;
import cn.zhiyuan.ces.sys.entity.SysUser;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.PageBean;
import cn.zhiyuan.frame.filter.SysFilter;

@Controller
@RequestMapping("/base")
public final class WorkingUnitAction extends BaseAction{

	//审核类型
	public final static String AUDIT_TYPE = "1";//三级审核
		
	@Autowired
	private IWorkingUnitBiz iWorkingUnitBiz;

	/*
	 * 加载单位列表用于审核
	 * */
	@RequestMapping("/workingunit/json4check") 
	public ModelAndView json(PageBean<WorkingUnit> pb,WorkingUnit workingUnit) {
		iWorkingUnitBiz.getPageList(pb,workingUnit,null);
		return json(pb);
	}
	
	/*
	 * 加载当前登录用户所属单位资料
	 * */
	@RequestMapping("/workingunit/jsons") 
	public ModelAndView json(MsgBean mbean,WorkingUnit workingUnit) {
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		Integer unitId = user.getUnitId();
		workingUnit.setId(unitId);
		mbean.setObj(iWorkingUnitBiz.getList(workingUnit,null));
		return json(mbean);
	}

	/*
	 * 审核通过
	 * */
	@RequestMapping("/workingunit/boxJson") 
	public ModelAndView boxJson(MsgBean mbean,WorkingUnit workingUnit) {
		mbean.setObj(iWorkingUnitBiz.getList4Box(3));
		return json(mbean);
	}
	
	/*
	 * 获取所有记录
	 * */
	@RequestMapping("/workingunit/boxJsonEx") 
	public ModelAndView boxJsonEx(MsgBean mbean) {
		mbean.setObj(iWorkingUnitBiz.getList4Box(null));
		return json(mbean);
	}
	
	//治安大队初始化单位,注册单位
	@RequestMapping("/workingunit/regUnit") 
	public ModelAndView regUnit(MsgBean mbean,WorkingUnit workingUnit) {
		mbean.setInfo("保存成功");
		workingUnit.setAuditStatus(1);
		iWorkingUnitBiz.add(workingUnit);
		return json(mbean);
	}
	
	//备案
	@RequestMapping("/workingunit/save1") 
	public ModelAndView save1(MsgBean mbean,WorkingUnit workingUnit,
			String uploadIds,AuditFlow auditFlow) {
		mbean.setInfo("保存成功");
		workingUnit.setAuditStatus(AuditFlow.AUDIT_STATUS_START);
		//初始化审核数据
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		auditFlow.setAuditType(AUDIT_TYPE);//审核类型
		auditFlow.setAuditStep("1");
		auditFlow.setAuditResult(1);
		iWorkingUnitBiz.save(workingUnit, uploadIds, auditFlow);
		return json(mbean);
	}

	//受理
	@RequestMapping("/workingunit/save2") 
	public ModelAndView save2(MsgBean mbean,WorkingUnit workingUnit,AuditFlow auditFlow) {
		mbean.setInfo("保存成功");
		workingUnit.setAuditStatus(AuditFlow.AUDIT_STATUS_GOON);
		//初始化审核数据
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		auditFlow.setAuditType(AUDIT_TYPE);//审核类型
		auditFlow.setAuditStep("2");
		iWorkingUnitBiz.auditSave(workingUnit, auditFlow);
		return json(mbean);
	}

	//审核
	@RequestMapping("/workingunit/save3") 
	public ModelAndView save3(MsgBean mbean,WorkingUnit workingUnit,AuditFlow auditFlow) {
		mbean.setInfo("保存成功");
		workingUnit.setAuditStatus(AuditFlow.AUDIT_STATUS_END);
		//初始化审核数据
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		auditFlow.setAuditType(AUDIT_TYPE);//审核类型
		auditFlow.setAuditStep("3");
		iWorkingUnitBiz.auditSave(workingUnit, auditFlow);
		return json(mbean);
	}
	
}
