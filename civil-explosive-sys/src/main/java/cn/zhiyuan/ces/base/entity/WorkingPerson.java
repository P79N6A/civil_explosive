package cn.zhiyuan.ces.base.entity;

import javax.persistence.*;

import cn.zhiyuan.frame.orm.Formula;


@Table(name="working_person")
public class WorkingPerson extends AuditBean{
	
	/**
	*所属单位名称
	*/
	@Formula(name = "unit_name",value="select tt.short_name from working_unit tt "
			+ " where tt.id= t.unit_id")
	private String unitName;
	
	/**
	*技术等级名称
	*/
	@Formula(name = "tech_level_name",value="select tt.code_name from sys_code tt "
			+ " where tt.code_type = '6' and tt.code_value= t.tech_level")
	private String techLevelName;
	
	/**
	*许可证类型名称
	*/
	@Formula(name = "licence_type_name",value="select tt.code_name from sys_code tt "
			+ " where tt.code_type = '5' and tt.code_value= t.licence_type")
	private String licenceTypeName;
	
	/**
	*在职状态名称
	*/
	@Formula(name = "status_name",value="select tt.code_name from sys_code tt "
			+ " where tt.code_type = '7' and tt.code_value= t.status")
	private String statusName;
	
	
	/**
	*劳动合同有效期至
	*/
	@Column(name = "labour_end_date")
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd")
	private java.util.Date labourEndDate;

	/**
	*状态(1在职2离职)
	*/
	@Column(name = "status")
	private String status;

	/**
	*所属单位
	*/
	@Column(name = "unit_id",nullable=false)
	private Integer unitId;
	
	/**
	*技术等级
	*/
	@Column(name = "tech_level")
	private String techLevel;

	/**
	*许可证有效期至
	*/
	@Column(name = "licence_end_date")
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd")
	private java.util.Date licenceEndDate;

	/**
	*人员卡编号
	*/
	@Column(name = "card_num")
	private String cardNum;

	/**
	* 联系电话
	*/
	@Column(name = "phone")
	private String phone;
	
	/**
	*身份证号码
	*/
	@Column(name = "idcard")
	private String idcard;

	/**
	*养老保险有效期至
	*/
	@Column(name = "endowment_end_date")
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd")
	private java.util.Date endowmentEndDate;

	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*姓名
	*/
	@Column(name = "name",nullable=false)
	private String name;

	/**
	*许可证名称
	*/
	@Column(name = "licence_name")
	private String licenceName;

	/**
	*许可证编号
	*/
	@Column(name = "licence_code")
	private String licenceCode;

	/**
	*许可证类型
	*/
	@Column(name = "licence_type")
	private String licenceType;



	public java.util.Date getLabourEndDate() {
		return labourEndDate;
	}

	public void setLabourEndDate(java.util.Date labourEndDate) {
		this.labourEndDate=labourEndDate;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId=unitId;
	}

	public String getTechLevel() {
		return techLevel;
	}

	public void setTechLevel(String techLevel) {
		this.techLevel=techLevel;
	}

	public java.util.Date getLicenceEndDate() {
		return licenceEndDate;
	}

	public void setLicenceEndDate(java.util.Date licenceEndDate) {
		this.licenceEndDate=licenceEndDate;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum=cardNum;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard=idcard;
	}

	public java.util.Date getEndowmentEndDate() {
		return endowmentEndDate;
	}

	public void setEndowmentEndDate(java.util.Date endowmentEndDate) {
		this.endowmentEndDate=endowmentEndDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public String getName() {
		return name;
	}

	public String getNameAll() {
		return licenceTypeName + " : " +name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public String getLicenceCode() {
		return licenceCode;
	}

	public void setLicenceCode(String licenceCode) {
		this.licenceCode=licenceCode;
	}

	public String getLicenceType() {
		return licenceType;
	}

	public void setLicenceType(String licenceType) {
		this.licenceType=licenceType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTechLevelName() {
		return techLevelName;
	}

	public void setTechLevelName(String techLevelName) {
		this.techLevelName = techLevelName;
	}

	public String getLicenceTypeName() {
		return licenceTypeName;
	}

	public void setLicenceTypeName(String licenceTypeName) {
		this.licenceTypeName = licenceTypeName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLicenceName() {
		return licenceName;
	}

	public void setLicenceName(String licenceName) {
		this.licenceName = licenceName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

}
