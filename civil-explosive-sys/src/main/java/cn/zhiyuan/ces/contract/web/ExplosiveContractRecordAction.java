package cn.zhiyuan.ces.contract.web;

import cn.zhiyuan.ces.base.entity.AuditFlow;
import cn.zhiyuan.ces.contract.biz.IExplosiveContractRecordBiz;
import cn.zhiyuan.ces.contract.entity.ExplosiveContractRecord;
import cn.zhiyuan.ces.sys.entity.SysUser;

import org.springframework.stereotype.Controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.zhiyuan.frame.*;
import cn.zhiyuan.frame.filter.SysFilter;
import cn.zhiyuan.frame.orm.builder.IWhereSql;


@Controller
@RequestMapping("/contract")
public final class ExplosiveContractRecordAction extends BaseAction{

	public final static String AUDIT_TYPE = "4";//合同备案
	public final static String ITEM_AUDIT_TYPE = "5";//合同审批
	
	@Autowired
	private IExplosiveContractRecordBiz iExplosiveContractRecordBiz;

	@RequestMapping("/explosivecontractrecord/jsons") 
	public ModelAndView jsons(PageBean<ExplosiveContractRecord> pb
			,ExplosiveContractRecord explosiveContractRecord,String q) {
		iExplosiveContractRecordBiz.getPageList(pb,explosiveContractRecord,"t.id desc",new IWhereSql() {
			
			@Override
			public void where(StringBuilder sb, Map<String, Object> params) {
				if(StringUtils.isNotEmpty(q)) {
					sb.append(" and t.contract_name like :q ");
					params.put("q", "%" + q + "%");
				}
			}
		});
		return json(pb);
	}
	
	/*
	 * app端爆破公司查询，待受理的
	 * */
	@RequestMapping("/explosivecontractrecord/jsonsApp") 
	public ModelAndView jsonsApp(PageBean<ExplosiveContractRecord> pb,
			ExplosiveContractRecord explosiveContractRecord) {
		iExplosiveContractRecordBiz.getPageList(pb,explosiveContractRecord,null);
		return json(pb);
	}
	

	@RequestMapping("/explosivecontractrecord/boxJsons") 
	public ModelAndView boxJsons(MsgBean mbean,ExplosiveContractRecord explosiveContractRecord) {
		Integer applyUnitId = explosiveContractRecord.getApplyUnitId();
		if(applyUnitId== null) {
			SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
			applyUnitId = user.getUnitId();
		}
		//审核完成
		mbean.setObj(iExplosiveContractRecordBiz.getList4Status(applyUnitId,AuditFlow.AUDIT_STATUS_END));
		return json(mbean);
	}
	/*
	 * app端获取未受理合同
	 * */
	@RequestMapping("/explosivecontractrecord/appJsons") 
	public ModelAndView appJsons(MsgBean mbean,Integer status) {
		SysUser  user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		log.debug(user.getUnitId() + "," + status);
		mbean.setObj(iExplosiveContractRecordBiz.getList4Status(user.getUnitId(),status));
		return json(mbean);
	}
	
	@RequestMapping("/explosivecontractrecord/save1") 
	public ModelAndView save1(MsgBean mbean,ExplosiveContractRecord explosiveContractRecord
			,String uploadIds,AuditFlow auditFlow,String itemJson) {
		mbean.setInfo("备案成功");
		explosiveContractRecord.setAuditStatus(AuditFlow.AUDIT_STATUS_START);
		//初始化审核数据
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		if(explosiveContractRecord.getContractCheck() == 1)
			auditFlow.setAuditType(ITEM_AUDIT_TYPE);//项目
		else
			auditFlow.setAuditType(AUDIT_TYPE);//合同备案
		auditFlow.setAuditStep("1");
		auditFlow.setAuditResult(1);
		iExplosiveContractRecordBiz.save(explosiveContractRecord, uploadIds, auditFlow);
		return json(mbean);
	}
	
	@RequestMapping("/explosivecontractrecord/save2") 
	public ModelAndView save2(MsgBean mbean,ExplosiveContractRecord explosiveContractRecord
			,AuditFlow auditFlow,String itemJson) {
		mbean.setInfo("受理成功");
		explosiveContractRecord.setAuditStatus(AuditFlow.AUDIT_STATUS_GOON);
		//初始化审核数据
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		if(explosiveContractRecord.getContractCheck() == 1)
			auditFlow.setAuditType(ITEM_AUDIT_TYPE);//项目
		else
			auditFlow.setAuditType(AUDIT_TYPE);//合同备案
		auditFlow.setAuditStep("2");
		iExplosiveContractRecordBiz.auditSave(explosiveContractRecord, auditFlow);
		return json(mbean);
	}
	
	@RequestMapping("/explosivecontractrecord/save3") 
	public ModelAndView save3(MsgBean mbean,ExplosiveContractRecord explosiveContractRecord
			,AuditFlow auditFlow,String itemJson) {
		mbean.setInfo("审核成功");
		
		//初始化审核数据
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		if(explosiveContractRecord.getContractCheck() == 1) {
			auditFlow.setAuditType(ITEM_AUDIT_TYPE);//项目
			//项目结束
			explosiveContractRecord.setAuditStatus(AuditFlow.AUDIT_STATUS_END);
		}else {
			auditFlow.setAuditType(AUDIT_TYPE);//合同备案
			//继续
			explosiveContractRecord.setAuditStatus(AuditFlow.AUDIT_STATUS_GOON);
		}
		auditFlow.setAuditStep("3");
		iExplosiveContractRecordBiz.auditSave(explosiveContractRecord, auditFlow);
		return json(mbean);
	}
	
	@RequestMapping("/explosivecontractrecord/save4") 
	public ModelAndView save4(MsgBean mbean,ExplosiveContractRecord explosiveContractRecord
			,AuditFlow auditFlow,String itemJson) {
		mbean.setInfo("审核成功");
		explosiveContractRecord.setAuditStatus(AuditFlow.AUDIT_STATUS_GOON);
		//初始化审核数据
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		auditFlow.setAuditType(AUDIT_TYPE);//审核类型
		auditFlow.setAuditStep("4");
		iExplosiveContractRecordBiz.auditSave(explosiveContractRecord, auditFlow);
		return json(mbean);
	}
	
	//分局领导审批
	@RequestMapping("/explosivecontractrecord/save5") 
	public ModelAndView save5(MsgBean mbean,ExplosiveContractRecord explosiveContractRecord
			,AuditFlow auditFlow,String itemJson) {
		mbean.setInfo("审批成功");
		explosiveContractRecord.setAuditStatus(AuditFlow.AUDIT_STATUS_END);
		//初始化审核数据
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		auditFlow.setAuditType(AUDIT_TYPE);//审核类型
		auditFlow.setAuditStep("5");
		iExplosiveContractRecordBiz.auditSave(explosiveContractRecord, auditFlow);
		return json(mbean);
	}
	
}
