package cn.zhiyuan.ces.contract.entity;

import javax.persistence.*;

import cn.zhiyuan.frame.orm.Formula;


@Table(name="explosive_card_info")
public class ExplosiveCardInfo {
	
	/**
	*所属类别Id
	*/
	@Formula(name = "explosive_category_id",value="select tt1.id from explosive_category tt1,"
			+ " explosive_variety tt2 where tt1.id = tt2.category_id"
			+ "  and tt2.code = t.explosive_variet")
	private Integer explosiveCategoryId;
	
	/**
	*所属类别名称
	*/
	@Formula(name = "explosive_category_name",value="select tt1.name from explosive_category tt1,"
			+ " explosive_variety tt2 where tt1.id = tt2.category_id"
			+ "  and tt2.code = t.explosive_variet")
	private String explosiveCategoryName;
	
	/**
	*所属品种名称
	*/
	@Formula(name = "explosive_variet_name",value="select tt.name from"
			+ " explosive_variety tt where  tt.code = t.explosive_variet")
	private String explosiveVarietName;
	
	/**
	*炸药数量
	*/
	@Column(name = "explosive_count")
	private Integer explosiveCount;

	/**
	*炸药品种代码
	*/
	@Column(name = "explosive_variet")
	private String explosiveVariet;

	/**
	*证件类型
	*/
	@Column(name = "card_type",nullable=false)
	private String cardType;

	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*所属购买证
	*/
	@Column(name = "host_id",nullable=false)
	private Integer hostId;



	public Integer getExplosiveCount() {
		return explosiveCount;
	}

	public void setExplosiveCount(Integer explosiveCount) {
		this.explosiveCount=explosiveCount;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType=cardType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public Integer getHostId() {
		return hostId;
	}

	public void setHostId(Integer hostId) {
		this.hostId=hostId;
	}

	public Integer getExplosiveCategoryId() {
		return explosiveCategoryId;
	}

	public void setExplosiveCategoryId(Integer explosiveCategoryId) {
		this.explosiveCategoryId = explosiveCategoryId;
	}

	public String getExplosiveCategoryName() {
		return explosiveCategoryName;
	}

	public void setExplosiveCategoryName(String explosiveCategoryName) {
		this.explosiveCategoryName = explosiveCategoryName;
	}

	public String getExplosiveVarietName() {
		return explosiveVarietName;
	}

	public void setExplosiveVarietName(String explosiveVarietName) {
		this.explosiveVarietName = explosiveVarietName;
	}

	public String getExplosiveVariet() {
		return explosiveVariet;
	}

	public void setExplosiveVariet(String explosiveVariet) {
		this.explosiveVariet = explosiveVariet;
	}

}
