package cn.zhiyuan.ces.sys.entity;

import javax.persistence.*;


@Table(name="user_role")
public class UserRole {
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
	@Column(name = "user_id",nullable=false)
	private Integer userId;

	/**
	*
	*/
	@Column(name = "role_id",nullable=false)
	private Integer roleId;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId=userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId=roleId;
	}

}
