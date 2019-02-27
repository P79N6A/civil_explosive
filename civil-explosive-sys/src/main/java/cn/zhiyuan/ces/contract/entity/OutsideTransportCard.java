package cn.zhiyuan.ces.contract.entity;

import javax.persistence.*;


@Table(name="outside_transport_card")
public class OutsideTransportCard {
	/**
	*运输证编号
	*/
	@Column(name = "code",nullable=false)
	private String code;

	/**
	*发证日期
	*/
	@Column(name = "begin_date")
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd")
	private java.util.Date beginDate;

	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*销售单位
	*/
	@Column(name = "marketing_id")
	private Integer marketingId;

	/**
	*承运人
	*/
	@Column(name = "carrier")
	private String carrier;

	/**
	*购买单位
	*/
	@Column(name = "purchase_id")
	private Integer purchaseId;

	/**
	*车牌号码
	*/
	@Column(name = "car_num")
	private String carNum;

	/**
	*经办人
	*/
	@Column(name = "operator",nullable=false)
	private Integer operator;

	/**
	*有效期至
	*/
	@Column(name = "end_date")
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd")
	private java.util.Date endDate;

	/**
	*购买证状态
	*/
	@Column(name = "status")
	private String status;



	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code=code;
	}

	public java.util.Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(java.util.Date beginDate) {
		this.beginDate=beginDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public Integer getMarketingId() {
		return marketingId;
	}

	public void setMarketingId(Integer marketingId) {
		this.marketingId=marketingId;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier=carrier;
	}

	public Integer getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(Integer purchaseId) {
		this.purchaseId=purchaseId;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum=carNum;
	}

	public Integer getOperator() {
		return operator;
	}

	public void setOperator(Integer operator) {
		this.operator=operator;
	}

	public java.util.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(java.util.Date endDate) {
		this.endDate=endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status=status;
	}

}
