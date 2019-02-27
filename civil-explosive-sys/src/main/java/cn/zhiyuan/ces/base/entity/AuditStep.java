package cn.zhiyuan.ces.base.entity;

import javax.persistence.*;

import cn.zhiyuan.frame.orm.Formula;


@Table(name="audit_step")
public class AuditStep {
	
	//审核类型名称
	@Formula(name="audit_step_name",value="select tt.code_name from sys_code tt "
			  + " where tt.code_value=t.audit_type and tt.code_type= 'a' ")
	private String auditTypeName;
		
	/**
	*审核步骤名称
	*/
	@Column(name = "audit_name",nullable=false)
	private String auditName;

	/**
	*审核通过
	*/
	@Column(name = "audit_yes",nullable=false)
	private String auditYes;
	
	/**
	*审核拒绝
	*/
	@Column(name = "audit_no",nullable=false)
	private String auditNo;
	
	/**
	*审核步骤编码
	*/
	@Column(name = "audit_code",nullable=false)
	private String auditCode;
	
	/**
	*审核类型
	*/
	@Column(name = "audit_type",nullable=false)
	private String auditType;

	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName=auditName;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType=auditType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public String getAuditTypeName() {
		return auditTypeName;
	}

	public void setAuditTypeName(String auditTypeName) {
		this.auditTypeName = auditTypeName;
	}

	public String getAuditCode() {
		return auditCode;
	}

	public void setAuditCode(String auditCode) {
		this.auditCode = auditCode;
	}

	public String getAuditYes() {
		return auditYes;
	}

	public void setAuditYes(String auditYes) {
		this.auditYes = auditYes;
	}

	public String getAuditNo() {
		return auditNo;
	}

	public void setAuditNo(String auditNo) {
		this.auditNo = auditNo;
	}

}
