package cn.zhiyuan.ces.contract.entity;

import javax.persistence.*;

import cn.zhiyuan.ces.base.entity.AuditBean;
import cn.zhiyuan.frame.orm.Formula;


@Table(name="purchase_card")
public class PurchaseCard extends AuditBean {
	
	/**
	*销售单位名称
	*/
	@Formula(name="market_unit_name",value="select tt.name from market_unit tt "
				+ " where tt.id = t.marketing_unit_id ")
	private String marketUnitName;
	
	/**
	*证件状态名称
	*/
	@Formula(name = "status_name",value="select tt.code_name from sys_code tt "
			+ " where tt.code_type = 'e' and tt.code_value= t.status")
	private String statusName;
	
	/**
	*购买单位名称
	*/
	@Formula(name="unit_name",value="select tt.name from working_unit tt "
				+ " where tt.id = t.unit_id ")
	private String unitName;
	
	/**
	*经办人姓名
	*/
	@Formula(name = "operator_name",value="select tt.name from working_person tt "
			+ " where tt.id = t.operator order by tt.id desc ")
	private String operatorName;
	
	/**
	*有效期至
	*/
	@Column(name = "card_end_date")
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd")
	private java.util.Date cardEndDate;

	/**
	*购买证状态
	*/
	@Column(name = "status")
	private String status;

	/**
	*销售单位
	*/
	@Column(name = "marketing_unit_id")
	private Integer marketingUnitId;

	/**
	*经办人
	*/
	@Column(name = "operator")
	private Integer operator;

	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*发证日期
	*/
	@Column(name = "card_date")
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd")
	private java.util.Date cardDate;

	/**
	*购买单位
	*/
	@Column(name = "unit_id")
	private Integer unitId;

	/**
	*购买证编号
	*/
	@Column(name = "code",nullable=false)
	private String code;

	/**
	*项目名称
	*/
	@Formula(name="item_name",value="select tt.contract_name from explosive_contract_record tt "
				+ " where tt.id = t.item_id ")
	private String itemName;
	
	/**
	*作业合同
	*/
	@Column(name = "item_id")
	private Integer itemId;

	public java.util.Date getCardEndDate() {
		return cardEndDate;
	}

	public void setCardEndDate(java.util.Date cardEndDate) {
		this.cardEndDate=cardEndDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status=status;
	}

	public Integer getMarketingUnitId() {
		return marketingUnitId;
	}

	public void setMarketingUnitId(Integer marketingUnitId) {
		this.marketingUnitId = marketingUnitId;
	}

	public Integer getOperator() {
		return operator;
	}

	public void setOperator(Integer operator) {
		this.operator=operator;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public java.util.Date getCardDate() {
		return cardDate;
	}

	public void setCardDate(java.util.Date cardDate) {
		this.cardDate=cardDate;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId=unitId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code=code;
	}

	public String getMarketUnitName() {
		return marketUnitName;
	}

	public void setMarketUnitName(String marketUnitName) {
		this.marketUnitName = marketUnitName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

}
