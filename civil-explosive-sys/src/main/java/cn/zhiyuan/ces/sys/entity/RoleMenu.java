package cn.zhiyuan.ces.sys.entity;

import javax.persistence.*;


@Table(name="role_menu")
public class RoleMenu {
	/**
	*角色
	*/
	@Id
	@Column(name = "role_id",nullable=false)
	private Integer roleId;

	/**
	*菜单
	*/
	@Id
	@Column(name = "menu_id",nullable=false)
	private Integer menuId;

	/**
	*
	*/
	@Column(name = "remark")
	private String remark;



	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId=roleId;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId=menuId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark=remark;
	}

}
