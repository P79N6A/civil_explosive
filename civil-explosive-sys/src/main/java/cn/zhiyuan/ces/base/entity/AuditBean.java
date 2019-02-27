package cn.zhiyuan.ces.base.entity;

import javax.persistence.Column;

import cn.zhiyuan.frame.orm.Formula;

/*
 * 审核实体类
 * */
public class AuditBean {
	//最新的审核进度
	@Formula(name="audit_step",value="SELECT tt1.audit_step from  audit_flow tt1 " + 
			" where tt1.id = t.audit_flow_id")
	private String auditStep;
		
	//最新的审核结果
	@Formula(name="audit_result",value="SELECT tt1.audit_result from  audit_flow tt1 " + 
			" where tt1.id = t.audit_flow_id")
	private Boolean auditResult;
	
	//审核状态名称,最新的
	@Formula(name="audit_result_name",value="SELECT  case tt1.audit_result WHEN 1 then tt2.audit_yes "
			+ "WHEN 0 then tt2.audit_no  end  as audit_result_name from  audit_flow tt1,audit_step tt2 " + 
			" where tt1.audit_type = tt2.audit_type and  tt1.audit_step = tt2.audit_code "
			+ "and tt1.id = t.audit_flow_id")
	private String auditResultName;

	/**
	*审核流程ID
	*/
	@Column(name = "audit_flow_id")
	private Integer auditFlowId;
	
	/**
	*时间戳
	*/
	@Column(name = "create_date")
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createDate;
	
	//审核状态:1开始2进行3结束
	@Column(name = "audit_status")
	private Integer auditStatus;
		
	public String getAuditStep() {
		return auditStep;
	}

	public void setAuditStep(String auditStep) {
		this.auditStep = auditStep;
	}

	public Boolean getAuditResult() {
		return auditResult;
	}

	public void setAuditResult(Boolean auditResult) {
		this.auditResult = auditResult;
	}

	public String getAuditResultName() {
		return auditResultName;
	}

	public void setAuditResultName(String auditResultName) {
		this.auditResultName = auditResultName;
	}

	public Integer getAuditFlowId() {
		return auditFlowId;
	}

	public void setAuditFlowId(Integer auditFlowId) {
		this.auditFlowId = auditFlowId;
	}

	public java.util.Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

}
