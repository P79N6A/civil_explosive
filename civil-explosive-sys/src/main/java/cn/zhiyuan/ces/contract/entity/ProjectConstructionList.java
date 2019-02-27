package cn.zhiyuan.ces.contract.entity;

import javax.persistence.*;


@Table(name="project_construction_list")
public class ProjectConstructionList {
	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*所属项目
	*/
	@Column(name = "item_id",nullable=false)
	private Integer itemId;

	/**
	*从业单位
	*/
	@Column(name = "unit_type",nullable=false)
	private Integer unitType;

	/**
	*人员外键
	*/
	@Column(name = "staff_id",nullable=false)
	private Integer staffId;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId=itemId;
	}

	public Integer getStaffId() {
		return staffId;
	}

	public void setStaffId(Integer staffId) {
		this.staffId=staffId;
	}

	public Integer getUnitType() {
		return unitType;
	}

	public void setUnitType(Integer unitType) {
		this.unitType = unitType;
	}

}
