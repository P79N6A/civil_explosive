package cn.zhiyuan.ces.sys.entity;

import javax.persistence.*;

import cn.zhiyuan.frame.orm.Formula;


@Table(name="sys_role")
public class SysRole {
	
	@Formula(name = "menu_ids", value = "SELECT group_concat(tt2.menu_id) from role_menu tt1,sys_menu tt2 \n" + 
			"where tt1.menu_id = tt2.menu_id and  tt1.role_id = t.role_id")
	private String menuIds;
	
	/**
	*
	*/
	@Column(name = "remark")
	private String remark;

	/**
	*角色名称
	*/
	@Column(name = "role_name",nullable=false)
	private String roleName;

	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id",nullable=false)
	private Integer roleId;

	/**
	*顺序号
	*/
	@Column(name = "order_num")
	private Integer orderNum;

	/**
	*角色类型
	*/
	@Column(name = "role_type")
	private Integer roleType;
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark=remark;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName=roleName;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId=roleId;
	}

	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	public Integer getRoleType() {
		return roleType;
	}

	public void setRoleType(Integer roleType) {
		this.roleType = roleType;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

}
