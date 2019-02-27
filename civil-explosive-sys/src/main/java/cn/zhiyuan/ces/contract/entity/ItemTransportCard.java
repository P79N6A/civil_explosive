package cn.zhiyuan.ces.contract.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.zhiyuan.ces.base.entity.AuditBean;
import cn.zhiyuan.frame.orm.Formula;


@Table(name="item_transport_card")
public class ItemTransportCard extends AuditBean{
	
	/**
	*项目名称
	*/
	@Formula(name="item_name",value="select tt.contract_name from explosive_contract_record tt "
				+ " where tt.id = t.item_id ")
	private String itemName;
	
	/**
	*承运单位名称
	*/
	@Formula(name="carrier_unit_name",value="select tt.name from working_unit tt "
				+ " where tt.id = t.carrier_unit_id ")
	private String carrierUnitName;
	
	/**
	*承运人姓名
	*/
	@Formula(name="carrier_name",value="select tt.name from working_person tt "
				+ " where tt.id = t.carrier_id ")
	private String carrierName;
	
	/**
	*证件状态名称
	*/
	@Formula(name = "status_name",value="select tt.code_name from sys_code tt "
			+ " where tt.code_type = 'e' and tt.code_value= t.status")
	private String statusName;
	
	/**
	*经办人姓名
	*/
	@Formula(name = "operator_name",value="select tt.name from working_person tt "
			+ " where tt.id = t.operator ")
	private String operatorName;
		
	/**
	*派出所名称
	*/
	@Formula(name = "police_name",value="select tt.short_name from local_police_station tt "
			+ " where tt.id = t.police_id")
	private String policeName;
	
	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*运输证编号
	*/
	@Column(name = "code",nullable=false)
	private String code;

	/**
	*购买证状态
	*/
	@Column(name = "status")
	private String status;

	/**
	*作业合同
	*/
	@Column(name = "item_id")
	private Integer itemId;

	/**
	*承运单位
	*/
	@Column(name = "carrier_unit_id")
	private Integer carrierUnitId;

	/**
	*承运人
	*/
	@Column(name = "carrier_id")
	private String carrierId;

	/**
	*车牌号码
	*/
	@Column(name = "car_num")
	private String carNum;

	/**
	*发证日期
	*/
	@Column(name = "begin_date")
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd")
	private java.util.Date beginDate;

	/**
	*有效期至
	*/
	@Column(name = "end_date")
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd")
	private java.util.Date endDate;

	/**
	*经办人
	*/
	@Column(name = "operator",nullable=false)
	private Integer operator;

	/**
	*辖区派出所
	*/
	@Column(name = "police_id")
	private Integer policeId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code=code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status=status;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId=itemId;
	}

	public Integer getCarrierUnitId() {
		return carrierUnitId;
	}

	public void setCarrierUnitId(Integer carrierUnitId) {
		this.carrierUnitId=carrierUnitId;
	}

	public String getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(String carrierId) {
		this.carrierId=carrierId;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum=carNum;
	}

	public java.util.Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(java.util.Date beginDate) {
		this.beginDate=beginDate;
	}

	public java.util.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(java.util.Date endDate) {
		this.endDate=endDate;
	}

	public Integer getOperator() {
		return operator;
	}

	public void setOperator(Integer operator) {
		this.operator=operator;
	}

	public Integer getPoliceId() {
		return policeId;
	}

	public void setPoliceId(Integer policeId) {
		this.policeId = policeId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getCarrierUnitName() {
		return carrierUnitName;
	}

	public void setCarrierUnitName(String carrierUnitName) {
		this.carrierUnitName = carrierUnitName;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getPoliceName() {
		return policeName;
	}

	public void setPoliceName(String policeName) {
		this.policeName = policeName;
	}

}
