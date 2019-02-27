package cn.zhiyuan.ces.base.entity;

import javax.persistence.*;

import cn.zhiyuan.frame.CommonUtils;
import cn.zhiyuan.frame.orm.Formula;


@Table(name="working_unit")
public class WorkingUnit extends AuditBean {
	
	/**
	*技术负责人姓名
	*/
	@Formula(name = "tech_leader_name",value="select tt.name from working_person tt "
			+ " where tt.id = t.tech_leader order by tt.id desc ")
	private String techLeaderName;
	
	/**
	*技术负责人电话
	*/
	@Formula(name = "tech_leader_phone",value="select tt.phone from working_person tt "
			+ " where tt.id = t.tech_leader order by tt.id desc ")
	private String techLeaderPhone;
	
	/**
	*保卫负责人姓名
	*/
	@Formula(name = "guard_leader_name",value="select tt.name from working_person tt "
			+ " where tt.id = t.guard_leader order by tt.id desc ")
	private String guardLeaderName;
	
	/**
	*保卫负责人电话
	*/
	@Formula(name = "guard_leader_phone",value="select tt.phone from working_person tt "
			+ " where tt.id = t.guard_leader order by tt.id desc ")
	private String guardLeaderPhone;
	
	/**
	*许可证类型名称
	*/
	@Formula(name = "licence_type_name",value="select tt.code_name from sys_code tt "
			+ " where tt.code_type = '8' and tt.code_value= t.licence_type")
	private String licenceTypeName;
	
	/**
	*资质登记名称
	*/
	@Formula(name = "aptitude_level_name",value="select tt.code_name from sys_code tt "
			+ " where tt.code_type = '4' and tt.code_value= t.aptitude_level")
	private String aptitudeLevelName;
	
	/**
	*单位类型名称
	*/
	@Formula(name = "type_names",value="select group_concat(tt1.code_name) from sys_code tt1,workingunit_type tt2 "
			+ " where  tt1.code_type = '2' and tt1.code_value= tt2.type_code and tt2.unit_id = t.id")
	private String typeNames;
	 
	/**
	*工作范围名称
	*/
	@Formula(name = "working_scope_name",value="select tt.code_name from sys_code tt "
			+ " where tt.code_type = '3' and tt.code_value= t.working_scope")
	private String workingScopeName;
		
	/**
	*单位地址
	*/
	@Column(name = "address")
	private String address;

	/**
	*资质等级
	*/
	@Column(name = "aptitude_level")
	private String aptitudeLevel;

	/**
	*企业信用代码
	*/
	@Column(name = "credit_code")
	private String creditCode;

	/**
	*有效期至
	*/
	@Column(name = "end_start")
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd")
	private java.util.Date endStart;

	/**
	*有效期长期
	*/
	@Column(name = "end_start_ex")
	private Integer endStartEx;
	
	/**
	*法人手机号码
	*/
	@Column(name = "legal_mobile")
	private String legalMobile;

	/**
	*单位名称
	*/
	@Column(name = "name")
	private String name;
	/**
	*单位简称
	*/
	@Column(name = "short_name")
	private String shortName;
	/**
	*
	*/
	@Column(name = "remark")
	private String remark;

	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*工作范围
	*/
	@Column(name = "working_scope")
	private String workingScope;

	/**
	*许可证类型
	*/
	@Column(name = "licence_type")
	private String licenceType;

	/**
	*法人名称
	*/
	@Column(name = "legal_name")
	private String legalName;

	/**
	*法人身份证号码
	*/
	@Column(name = "legal_idcard")
	private String legalIdcard;

	/**
	*经办人
	*/
	@Column(name = "operator")
	private Integer operator;

	/**
	*保卫负责人
	*/
	@Column(name = "guard_leader")
	private Integer guardLeader;

	/**
	*净资产(万)
	*/
	@Column(name = "net_asset")
	private Integer netAsset;

	/**
	*技术负责人
	*/
	@Column(name = "tech_leader")
	private Integer techLeader;

	/**
	*专用设备净资产(万)
	*/
	@Column(name = "dedicated_device_net_asset")
	private Integer dedicatedDeviceNetAsset;

	/**
	*注册资金(万)
	*/
	@Column(name = "registered_capital")
	private Integer registeredCapital;

	/**
	*单位编号
	*/
	@Column(name = "unit_number")
	private String unitNumber;

	/**
	*银行帐号
	*/
	@Column(name = "bank_account")
	private String bankAccount;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address=address;
	}

	public String getAptitudeLevel() {
		return aptitudeLevel;
	}

	public void setAptitudeLevel(String aptitudeLevel) {
		this.aptitudeLevel=aptitudeLevel;
	}

	public String getCreditCode() {
		return creditCode;
	}

	public void setCreditCode(String creditCode) {
		this.creditCode=creditCode;
	}

	public java.util.Date getEndStart() {
		return endStart;
	}

	public String getEndStartStr() {
		if(endStart == null || endStartEx == null) return null;
		if(1 == endStartEx) return "长期";
		return CommonUtils.date2str(endStart, "yyyy-MM-dd HH:mm:ss");
	}
	
	public void setEndStart(java.util.Date endStart) {
		this.endStart=endStart;
	}

	public String getLegalMobile() {
		return legalMobile;
	}

	public void setLegalMobile(String legalMobile) {
		this.legalMobile=legalMobile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark=remark;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public String getWorkingScope() {
		return workingScope;
	}

	public void setWorkingScope(String workingScope) {
		this.workingScope=workingScope;
	}

	public String getLicenceType() {
		return licenceType;
	}

	public void setLicenceType(String licenceType) {
		this.licenceType=licenceType;
	}

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName=legalName;
	}

	public String getLegalIdcard() {
		return legalIdcard;
	}

	public void setLegalIdcard(String legalIdcard) {
		this.legalIdcard=legalIdcard;
	}

	public Integer getOperator() {
		return operator;
	}

	public void setOperator(Integer operator) {
		this.operator=operator;
	}

	public Integer getGuardLeader() {
		return guardLeader;
	}

	public void setGuardLeader(Integer guardLeader) {
		this.guardLeader=guardLeader;
	}

	public Integer getNetAsset() {
		return netAsset;
	}

	public void setNetAsset(Integer netAsset) {
		this.netAsset=netAsset;
	}

	public Integer getTechLeader() {
		return techLeader;
	}

	public void setTechLeader(Integer techLeader) {
		this.techLeader=techLeader;
	}

	public Integer getDedicatedDeviceNetAsset() {
		return dedicatedDeviceNetAsset;
	}

	public void setDedicatedDeviceNetAsset(Integer dedicatedDeviceNetAsset) {
		this.dedicatedDeviceNetAsset=dedicatedDeviceNetAsset;
	}
	
	public Integer getRegisteredCapital() {
		return registeredCapital;
	}

	public void setRegisteredCapital(Integer registeredCapital) {
		this.registeredCapital=registeredCapital;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber=unitNumber;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount=bankAccount;
	}

	public String getTechLeaderName() {
		return techLeaderName;
	}

	public void setTechLeaderName(String techLeaderName) {
		this.techLeaderName = techLeaderName;
	}

	public String getGuardLeaderName() {
		return guardLeaderName;
	}

	public void setGuardLeaderName(String guardLeaderName) {
		this.guardLeaderName = guardLeaderName;
	}

	public String getAptitudeLevelName() {
		return aptitudeLevelName;
	}

	public void setAptitudeLevelName(String aptitudeLevelName) {
		this.aptitudeLevelName = aptitudeLevelName;
	}

	public String getWorkingScopeName() {
		return workingScopeName;
	}

	public void setWorkingScopeName(String workingScopeName) {
		this.workingScopeName = workingScopeName;
	}

	public String getLicenceTypeName() {
		return licenceTypeName;
	}

	public void setLicenceTypeName(String licenceTypeName) {
		this.licenceTypeName = licenceTypeName;
	}

	public String getTechLeaderPhone() {
		return techLeaderPhone;
	}

	public void setTechLeaderPhone(String techLeaderPhone) {
		this.techLeaderPhone = techLeaderPhone;
	}

	public String getGuardLeaderPhone() {
		return guardLeaderPhone;
	}

	public void setGuardLeaderPhone(String guardLeaderPhone) {
		this.guardLeaderPhone = guardLeaderPhone;
	}

	public String getTypeNames() {
		return typeNames;
	}

	public void setTypeNames(String typeNames) {
		this.typeNames = typeNames;
	}

	public Integer getEndStartEx() {
		return endStartEx;
	}

	public void setEndStartEx(Integer endStartEx) {
		this.endStartEx = endStartEx;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}
