package cn.zhiyuan.ces.base.entity;

import javax.persistence.*;


@Table(name="delegation_unit")
public class DelegationUnit {
	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*单位名称
	*/
	@Column(name = "name",nullable=false)
	private String name;

	/**
	*单位名称
	*/
	@Column(name = "contacts")
	private String contacts;
	
	/**
	*备注
	*/
	@Column(name = "remark")
	private String remark;

	/**
	*移动电话
	*/
	@Column(name = "mobile")
	private String mobile;
	
	/**
	*单位地址
	*/
	@Column(name = "address")
	private String address;

	/**
	*联系电话
	*/
	@Column(name = "phone")
	private String phone;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address=address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone=phone;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
