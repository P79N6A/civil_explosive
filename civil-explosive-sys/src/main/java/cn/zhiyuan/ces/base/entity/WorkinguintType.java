package cn.zhiyuan.ces.base.entity;

import javax.persistence.*;


@Table(name="workingunit_type")
public class WorkinguintType {
	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*working_unit外键
	*/
	@Column(name = "unit_id",nullable=false)
	private Integer unitId;

	/**
	*单位类型代码
	*/
	@Column(name = "type_code",nullable=false)
	private String typeCode;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId=unitId;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode=typeCode;
	}

}
