package cn.zhiyuan.ces.sys.entity;

import javax.persistence.*;

import cn.zhiyuan.frame.orm.Formula;


@Table(name="sys_code")
public class SysCode {
	
	//字段类别名称
	@Formula(name="code_type_name",value="select tt.code_name from sys_code tt "
			+ " where tt.code_value=t.code_type and tt.code_type= '1' ")
	private String codeTypeName;
		
	/**
	*字典描述一
	*/
	@Column(name = "code_desc2")
	private String codeDesc2;

	/**
	*字典名称
	*/
	@Column(name = "code_name",nullable=false)
	private String codeName;

	/**
	*字段值
	*/
	@Column(name = "code_value",nullable=false)
	private String codeValue;

	/**
	*字典描述一
	*/
	@Column(name = "code_desc4")
	private String codeDesc4;

	/**
	*字典类别
	*/
	@Column(name = "code_type",nullable=false)
	private String codeType;

	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "code_id",nullable=false)
	private Integer codeId;

	/**
	*字典描述一
	*/
	@Column(name = "code_desc3")
	private String codeDesc3;

	/**
	*字典描述一
	*/
	@Column(name = "code_desc1")
	private String codeDesc1;

	/**
	*备注
	*/
	@Column(name = "remark")
	private String remark;



	public String getCodeDesc2() {
		return codeDesc2;
	}

	public void setCodeDesc2(String codeDesc2) {
		this.codeDesc2=codeDesc2;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName=codeName;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue=codeValue;
	}

	public String getCodeDesc4() {
		return codeDesc4;
	}

	public void setCodeDesc4(String codeDesc4) {
		this.codeDesc4=codeDesc4;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType=codeType;
	}

	public Integer getCodeId() {
		return codeId;
	}

	public void setCodeId(Integer codeId) {
		this.codeId=codeId;
	}

	public String getCodeDesc3() {
		return codeDesc3;
	}

	public void setCodeDesc3(String codeDesc3) {
		this.codeDesc3=codeDesc3;
	}

	public String getCodeDesc1() {
		return codeDesc1;
	}

	public void setCodeDesc1(String codeDesc1) {
		this.codeDesc1=codeDesc1;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark=remark;
	}

	public String getCodeTypeName() {
		return codeTypeName;
	}

	public void setCodeTypeName(String codeTypeName) {
		this.codeTypeName = codeTypeName;
	}

}
