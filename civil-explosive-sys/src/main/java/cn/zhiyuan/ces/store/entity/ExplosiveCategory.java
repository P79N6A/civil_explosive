package cn.zhiyuan.ces.store.entity;

import javax.persistence.*;

import cn.zhiyuan.frame.orm.Formula;


@Table(name="explosive_category")
public class ExplosiveCategory {
	
	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;
	
	/**
	*类型名称
	*/
	@Column(name = "name",nullable=false)
	private String name;

	/**
	*计量单位
	*/
	@Column(name = "measure_unit")
	private String measureUnit;

	/**
	*仓库类型1炸药类2雷管类
	*/
	@Column(name = "store_type")
	private String storeType;

	/**
	*仓库类型名称
	*/
	@Formula(name = "store_type_name",value=" select tt.code_name from sys_code tt "
			+ " where tt.code_type = 'f' and tt.code_value = t.store_type")
	private String storeTypeName;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public String getStoreTypeName() {
		return storeTypeName;
	}

	public void setStoreTypeName(String storeTypeName) {
		this.storeTypeName = storeTypeName;
	}

}
