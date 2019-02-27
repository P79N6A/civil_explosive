package cn.zhiyuan.ces.base.entity;

import javax.persistence.*;


@Table(name="local_police_station")
public class LocalPoliceStation {
	/**
	*备注
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
	*简称
	*/
	@Column(name = "short_name")
	private String shortName;

	/**
	*全称
	*/
	@Column(name = "all_name",nullable=false)
	private String allName;

	/**
	*联系电话
	*/
	@Column(name = "phone")
	private String phone;



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

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName=shortName;
	}

	public String getAllName() {
		return allName;
	}

	public void setAllName(String allName) {
		this.allName=allName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone=phone;
	}

}
