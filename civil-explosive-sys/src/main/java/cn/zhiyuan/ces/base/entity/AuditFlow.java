package cn.zhiyuan.ces.base.entity;

import java.util.Date;

import javax.persistence.*;


@Table(name="audit_flow")
public class AuditFlow {
	
	public static final int AUDIT_STATUS_START = 1;//开始
	public static final int AUDIT_STATUS_GOON = 2;//进行中
	public static final int AUDIT_STATUS_END = 3;//结束
	
	/**
	*时间戳
	*/
	@Column(name = "audit_time",nullable=false)
	private Date auditTime;
	
	/**
	*宿主
	*/
	@Column(name = "host_id",nullable=false)
	private Integer hostId;

	/**
	*审核类型
	*/
	@Column(name = "audit_type",nullable=false)
	private String auditType;

	/**
	*审核步骤
	*/
	@Column(name = "audit_step",nullable=false)
	private String auditStep;
	
	/**
	*审核结果
	*/
	@Column(name = "audit_result",nullable=false)
	private Integer auditResult;

	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*
	*/
	@Column(name = "remark")
	private String remark;

	/**
	*审批人
	*/
	@Column(name = "audit_id",nullable=false)
	private Integer auditId;

	public Integer getHostId() {
		return hostId;
	}

	public void setHostId(Integer hostId) {
		this.hostId=hostId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark=remark;
	}

	public Integer getAuditId() {
		return auditId;
	}

	public void setAuditId(Integer auditId) {
		this.auditId=auditId;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public String getAuditStep() {
		return auditStep;
	}

	public void setAuditStep(String auditStep) {
		this.auditStep = auditStep;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Integer getAuditResult() {
		return auditResult;
	}

	public void setAuditResult(Integer auditResult) {
		this.auditResult = auditResult;
	}

}
