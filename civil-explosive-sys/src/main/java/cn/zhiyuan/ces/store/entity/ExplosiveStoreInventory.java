package cn.zhiyuan.ces.store.entity;

import javax.persistence.*;


@Table(name="explosive_store_inventory")
public class ExplosiveStoreInventory {
	/**
	*备注
	*/
	@Column(name = "remark",nullable=false)
	private String remark;

	/**
	*所属品种
	*/
	@Column(name = "explosive_variety",nullable=false)
	private String explosiveVariety;

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
	@Column(name = "store_id")
	private Integer storeId;

	/**
	*炸药数量
	*/
	@Column(name = "explosive_count",nullable=false)
	private Integer explosiveCount;



	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark=remark;
	}

	public String getExplosiveVariety() {
		return explosiveVariety;
	}

	public void setExplosiveVariety(String explosiveVariety) {
		this.explosiveVariety=explosiveVariety;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId=storeId;
	}

	public Integer getExplosiveCount() {
		return explosiveCount;
	}

	public void setExplosiveCount(Integer explosiveCount) {
		this.explosiveCount=explosiveCount;
	}

}
