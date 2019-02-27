package cn.zhiyuan.ces.sys.entity;

import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;

import cn.zhiyuan.frame.orm.Formula;


@Table(name="sys_user")
public class SysUser {
	
	public static String DEFAULT_MM = "e48er96gjo32l9f4ke8b19bilg6io0h0";
	public static String ERROR_MSG = "帐号已经存在,请换一个重试 !";
	
	/**
	*所属单位名称
	*/
	@Formula(name = "unit_name",value="select tt.short_name from working_unit tt "
			+ " where tt.id= t.unit_id")
	private String unitName;
	
	//字段类别名称
	@Formula(name="role_names",value="select group_concat(tt1.role_name) from sys_role tt1,user_role tt2 "
			+ " where  tt1.role_id = tt2.role_id and tt2.user_id = t.user_id ")
	private String roleNames;
		
	//用户类型和role type对应
	//@Formula(name="user_type",value="selectt tt1.role_type from sys_role tt1,user_role tt2 "
		//	+ " where  tt1.role_id = tt2.role_id and tt2.user_id = t.user_id ")
	@Column(name = "role_type",insertable=false)
	private Integer roleType;

	/**
	*最后一次登录时间
	*/
	@Column(name = "last_login_time")
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd HH:mm:ss")
	private java.util.Date lastLoginTime;

	/**
	* 有效期
	*/
	@Column(name = "valid_date")
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd")
	private java.util.Date validDate;
	
	/**
	*固定电话
	*/
	@Column(name = "phone")
	private String phone;

	/**
	*状态：1正常0停用
	*/
	@Column(name = "status")
	private Integer status;
	
	/**
	*角色
	*/
	@Formula(name="role_ids",value="select group_concat(tt1.role_id) from sys_role tt1,user_role tt2 "
			+ " where  tt1.role_id = tt2.role_id and tt2.user_id = t.user_id ")
	private String roleIds;
	
	/**
	*主键
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id",nullable=false)
	private Integer userId;

	/**
	*密码
	*/
	@Column(name = "pass_word",nullable=false)
	@JSONField(serialize=false)
	private String passWord;

	/**
	*真实姓名
	*/
	@Column(name = "real_name")
	private String realName;
	
	/**
	*单位全称
	*/
	@Column(name = "real_name2")
	private String realName2;

	public String getRealName2() {
		return realName2;
	}

	public void setRealName2(String realName2) {
		this.realName2 = realName2;
	}

	/**
	*移动电话
	*/
	@Column(name = "mobile")
	private String mobile;

	/**
	*登录帐号
	*/
	@Column(name = "login_name")
	private String loginName;

	/**
	*备注一
	*/
	@Column(name = "remark")
	private String remark;

	/**
	*所属单位
	*/
	@Column(name = "unit_id")
	private Integer unitId;

	/**
	*身份证号码
	*/
	@Column(name = "id_card")
	private String idCard;

	/**
	*注册时间
	*/
	@Column(name = "reg_time",nullable=false)
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd HH:mm:ss")
	private java.util.Date regTime;

	public java.util.Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(java.util.Date lastLoginTime) {
		this.lastLoginTime=lastLoginTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone=phone;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId=userId;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord=passWord;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName=realName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile=mobile;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName=loginName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark=remark;
	}
	
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard=idCard;
	}

	public java.util.Date getRegTime() {
		return regTime;
	}

	public void setRegTime(java.util.Date regTime) {
		this.regTime=regTime;
	}

	public java.util.Date getValidDate() {
		return validDate;
	}

	public void setValidDate(java.util.Date validDate) {
		this.validDate = validDate;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleName) {
		this.roleNames = roleName;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getStatus() {
		return status;
	}

	public String getStatusName() {
		if(null == status) return null;
		return status == 1?"正常":"停用";
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRoleIds() {
		if(roleIds == null) return "";
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public Integer getRoleType() {
		return roleType;
	}

	public void setRoleType(Integer roleType) {
		this.roleType = roleType;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

}
