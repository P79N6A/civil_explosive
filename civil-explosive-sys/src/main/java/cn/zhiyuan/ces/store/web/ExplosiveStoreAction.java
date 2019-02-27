package cn.zhiyuan.ces.store.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.zhiyuan.ces.base.entity.AuditFlow;
import cn.zhiyuan.ces.store.biz.IExplosiveStoreBiz;
import cn.zhiyuan.ces.store.entity.ExplosiveStore;
import cn.zhiyuan.ces.sys.entity.SysUser;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.PageBean;
import cn.zhiyuan.frame.filter.SysFilter;


@Controller
@RequestMapping("/store")
public final class ExplosiveStoreAction extends BaseAction{

	public final static String AUDIT_TYPE = "3";//炸药仓库
		
	@Autowired
	private IExplosiveStoreBiz iExplosiveStoreBiz;

	@RequestMapping("/explosivestore/jsons") 
	public ModelAndView json(PageBean<ExplosiveStore> pb,ExplosiveStore explosiveStore) {
		iExplosiveStoreBiz.getPageList(pb,explosiveStore,null);
		return json(pb);
	}

	/*
	 * 备案
	 * */
	@RequestMapping("/explosivestore/save1") 
	public ModelAndView save1(MsgBean mbean,ExplosiveStore explosiveStore,AuditFlow auditFlow) {
		mbean.setInfo("保存成功");
		explosiveStore.setAuditStatus(AuditFlow.AUDIT_STATUS_START);
		//初始化审核数据
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		auditFlow.setAuditType(AUDIT_TYPE);//审核类型
		auditFlow.setAuditStep("1");
		auditFlow.setAuditResult(1);
		iExplosiveStoreBiz.save(explosiveStore, auditFlow);
		return json(mbean);
	}

	/*
	 * 受理
	 * */
	@RequestMapping("/explosivestore/save2") 
	public ModelAndView save2(MsgBean mbean,ExplosiveStore explosiveStore,AuditFlow auditFlow) {
		mbean.setInfo("保存成功");
		explosiveStore.setAuditStatus(AuditFlow.AUDIT_STATUS_GOON);
		//初始化审核数据
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		auditFlow.setAuditType(AUDIT_TYPE);//审核类型
		auditFlow.setAuditStep("2");
		iExplosiveStoreBiz.auditSave(explosiveStore, auditFlow);
		return json(mbean);
	}
	
	/*
	 * 审核
	 * */
	@RequestMapping("/explosivestore/save3") 
	public ModelAndView save3(MsgBean mbean,ExplosiveStore explosiveStore,AuditFlow auditFlow) {
		mbean.setInfo("保存成功");
		explosiveStore.setAuditStatus(AuditFlow.AUDIT_STATUS_END);
		//初始化审核数据
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		auditFlow.setAuditType(AUDIT_TYPE);//审核类型
		auditFlow.setAuditStep("3");
		iExplosiveStoreBiz.auditSave(explosiveStore, auditFlow);
		return json(mbean);
	}

}
