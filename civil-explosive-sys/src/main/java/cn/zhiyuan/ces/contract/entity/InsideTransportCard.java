package cn.zhiyuan.ces.contract.entity;

import javax.persistence.*;


@Table(name="inside_transport_card")
public class InsideTransportCard {
	/**
	*所属项目
	*/
	@Column(name = "project_id")
	private Integer projectId;

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
	*经办人
	*/
	@Column(name = "operator",nullable=false)
	private Integer operator;

	/**
	*承运人
	*/
	@Column(name = "carrier")
	private String carrier;

	/**
	*有效期至
	*/
	@Column(name = "end_date")
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd")
	private java.util.Date endDate;

	/**
	*运输证编号
	*/
	@Column(name = "code",nullable=false)
	private String code;

	/**
	*车牌号码
	*/
	@Column(name = "car_num")
	private String carNum;

	/**
	*购买证状态
	*/
	@Column(name = "status")
	private String status;



	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId=projectId;
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

	public Integer getOperator() {
		return operator;
	}

	public void setOperator(Integer operator) {
		this.operator=operator;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier=carrier;
	}

	public java.util.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(java.util.Date endDate) {
		this.endDate=endDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code=code;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum=carNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status=status;
	}

}
