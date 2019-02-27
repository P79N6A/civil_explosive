package cn.zhiyuan.ces.contract.entity;

import javax.persistence.*;

import cn.zhiyuan.ces.base.entity.AuditBean;
import cn.zhiyuan.frame.orm.Formula;


@Table(name="explosive_contract_record")
public class ExplosiveContractRecord extends AuditBean {

	//爆破单位项目参加人员
	@Formula(name="item_work_staffs",value="SELECT group_concat(tt.staff_id) "
			+ "from  project_construction_list tt  where tt.unit_type=1 and tt.item_id = t.id")
	private String itemWorkStaffs;
	//安全评估单位项目参加人员
	@Formula(name="item_assess_staffs",value="SELECT group_concat(tt.staff_id) "
			+ "from  project_construction_list tt  where tt.unit_type=2 and tt.item_id = t.id")
	private String itemAssessStaffs;
	//安全监理单位项目参加人员
	@Formula(name="item_supervisor_staffs",value="SELECT group_concat(tt.staff_id) " + 
			"from project_construction_list tt where tt.unit_type = 3 and tt.item_id = t.id")
	private String itemSupervisorStaffs;
	
	/**
	*辖区派出所名称
	*/
	@Formula(name="police_name",value="select tt.short_name from local_police_station tt "
				+ " where tt.id = t.police_id ")
	private String policeName;
	
	/**
	*委托单位名称
	*/
	@Formula(name="delegate_unit_name",value="select tt.name from delegation_unit tt "
				+ " where tt.id = t.delegate_unit_id ")
	private String delegateUnitName;
	
	/**
	*申请单位名称
	*/
	@Formula(name="apply_unit_name",value="select tt.name from working_unit tt "
				+ " where tt.id = t.apply_unit_id ")
	private String applyUnitName;
	
	/**
	*合同状态名称
	*/
	@Formula(name = "contract_status_name",value="select tt.code_name from sys_code tt "
			+ " where tt.code_type = '11' and tt.code_value= t.contract_status")
	private String contractStatusName;
	
	/**
	*合同状态
	*/
	@Column(name = "contract_status")
	private String contractStatus;

	/**
	*合同截止日期
	*/
	@Column(name = "contract_expiration_date")
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd")
	private java.util.Date contractExpirationDate;

	/**
	*是否需要项目审批
	*/
	@Column(name = "contract_check")
	private Byte contractCheck;

	/**
	*申请单位
	*/
	@Column(name = "apply_unit_id")
	private Integer applyUnitId;

	/**
	*合同简要说明
	*/
	@Column(name = "contract_explain")
	private String contractExplain;

	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*合同签定日期
	*/
	@Column(name = "contract_sign_date")
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd")
	private java.util.Date contractSignDate;

	/**
	*合同编号
	*/
	@Column(name = "contract_number")
	private String contractNumber;

	/**
	*爆破作业地点
	*/
	@Column(name = "task_addr")
	private String taskAddr;

	/**
	*合同名称
	*/
	@Column(name = "contract_name")
	private String contractName;

	/**
	*经办人
	*/
	@Column(name = "operator")
	private Integer operator;

	/**
	*委托单位
	*/
	@Column(name = "delegate_unit_id")
	private Integer delegateUnitId;

	/**
	*安全监理单位
	*/
	@Column(name = "item_supervisor_id")
	private Integer itemSupervisorId;

	/**
	*安全评估单位
	*/
	@Column(name = "item_assess_id")
	private Integer itemAssessId;

	/**
	*施工单位
	*/
	@Column(name = "item_work_id")
	private Integer itemWorkId;

	/**
	*辖区派出所
	*/
	@Column(name = "police_id")
	private Integer policeId;
	
	/**
	*项目审批人
	*/
	@Column(name = "item_approve_id")
	private Integer itemApproveId;

	/**
	*项目审核人
	*/
	@Column(name = "item_audit_id")
	private Integer itemAuditId;

	/**
	*项目设计人
	*/
	@Column(name = "item_designer_id")
	private Integer itemDesignerId;

	public String getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus=contractStatus;
	}

	public java.util.Date getContractExpirationDate() {
		return contractExpirationDate;
	}

	public void setContractExpirationDate(java.util.Date contractExpirationDate) {
		this.contractExpirationDate=contractExpirationDate;
	}

	public Byte getContractCheck() {
		return contractCheck;
	}

	public String getContractCheckName() {
		if(null == contractCheck) return null;
		else return contractCheck == 1?"是":"否";
	}
	
	public void setContractCheck(Byte contractCheck) {
		this.contractCheck=contractCheck;
	}

	public Integer getApplyUnitId() {
		return applyUnitId;
	}

	public void setApplyUnitId(Integer applyUnitId) {
		this.applyUnitId=applyUnitId;
	}

	public String getContractExplain() {
		return contractExplain;
	}

	public void setContractExplain(String contractExplain) {
		this.contractExplain=contractExplain;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public java.util.Date getContractSignDate() {
		return contractSignDate;
	}

	public void setContractSignDate(java.util.Date contractSignDate) {
		this.contractSignDate=contractSignDate;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber=contractNumber;
	}

	public String getTaskAddr() {
		return taskAddr;
	}

	public void setTaskAddr(String taskAddr) {
		this.taskAddr=taskAddr;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName=contractName;
	}

	public Integer getOperator() {
		return operator;
	}

	public void setOperator(Integer operator) {
		this.operator=operator;
	}

	public Integer getDelegateUnitId() {
		return delegateUnitId;
	}

	public void setDelegateUnitId(Integer delegateUnitId) {
		this.delegateUnitId=delegateUnitId;
	}

	public String getDelegateUnitName() {
		return delegateUnitName;
	}

	public void setDelegateUnitName(String delegateUnitName) {
		this.delegateUnitName = delegateUnitName;
	}

	public String getApplyUnitName() {
		return applyUnitName;
	}

	public void setApplyUnitName(String applyUnitName) {
		this.applyUnitName = applyUnitName;
	}

	public String getContractStatusName() {
		return contractStatusName;
	}

	public void setContractStatusName(String contractStatusName) {
		this.contractStatusName = contractStatusName;
	}

	public Integer getItemSupervisorId() {
		return itemSupervisorId;
	}

	public void setItemSupervisorId(Integer itemSupervisorId) {
		this.itemSupervisorId = itemSupervisorId;
	}

	public Integer getItemAssessId() {
		return itemAssessId;
	}

	public void setItemAssessId(Integer itemAssessId) {
		this.itemAssessId = itemAssessId;
	}

	public Integer getItemWorkId() {
		return itemWorkId;
	}

	public void setItemWorkId(Integer itemWorkId) {
		this.itemWorkId = itemWorkId;
	}

	public Integer getItemApproveId() {
		return itemApproveId;
	}

	public void setItemApproveId(Integer itemApproveId) {
		this.itemApproveId = itemApproveId;
	}

	public Integer getItemAuditId() {
		return itemAuditId;
	}

	public void setItemAuditId(Integer itemAuditId) {
		this.itemAuditId = itemAuditId;
	}

	public Integer getItemDesignerId() {
		return itemDesignerId;
	}

	public void setItemDesignerId(Integer itemDesignerId) {
		this.itemDesignerId = itemDesignerId;
	}

	public String getItemWorkStaffs() {
		if(itemWorkStaffs == null) return "";
		return itemWorkStaffs;
	}

	public void setItemWorkStaffs(String itemWorkStaffs) {
		this.itemWorkStaffs = itemWorkStaffs;
	}

	public String getItemAssessStaffs() {
		if(itemAssessStaffs == null) return "";
		return itemAssessStaffs;
	}

	public void setItemAssessStaffs(String itemAssessStaffs) {
		this.itemAssessStaffs = itemAssessStaffs;
	}

	public String getItemSupervisorStaffs() {
		if(itemSupervisorStaffs == null) return "";
		return itemSupervisorStaffs;
	}

	public void setItemSupervisorStaffs(String itemSupervisorStaffs) {
		this.itemSupervisorStaffs = itemSupervisorStaffs;
	}

	public String getPoliceName() {
		return policeName;
	}

	public void setPoliceName(String policeName) {
		this.policeName = policeName;
	}

	public Integer getPoliceId() {
		return policeId;
	}

	public void setPoliceId(Integer policeId) {
		this.policeId = policeId;
	}
}
