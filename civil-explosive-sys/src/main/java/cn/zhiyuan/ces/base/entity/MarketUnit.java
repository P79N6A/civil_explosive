package cn.zhiyuan.ces.base.entity;

import javax.persistence.*;


@Table(name="market_unit")
public class MarketUnit {
	
	/**
	*移动电话
	*/
	@Column(name = "mobile")
	private String mobile;
	
	/**
	*单位名称
	*/
	@Column(name = "name",nullable=false)
	private String name;

	/**
	*所在省份
	*/
	@Column(name = "province")
	private String province;

	/**
	*许可证编号
	*/
	@Column(name = "license_number")
	private String licenseNumber;

	/**
	*联系人
	*/
	@Column(name = "contacts")
	private String contacts;
	
	/**
	*备注
	*/
	@Column(name = "remark")
	private String remark;

	/**
	*联系电话
	*/
	@Column(name = "phone")
	private String phone;

	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*单位地址
	*/
	@Column(name = "address")
	private String address;

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone=phone;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address=address;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
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
