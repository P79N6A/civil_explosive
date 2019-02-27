package cn.zhiyuan.ces.contract.entity;

import javax.persistence.*;


@Table(name="transport_line_list")
public class TransportLineList {
	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*开始纬度坐标
	*/
	@Column(name = "start_gps_lat")
	private Float startGpsLat;

	/**
	*路段名称
	*/
	@Column(name = "name")
	private String name;

	/**
	*结束经度坐标
	*/
	@Column(name = "end_gps_lng")
	private Float endGpsLng;

	/**
	*结束纬度坐标
	*/
	@Column(name = "end_gps_lat")
	private Float endGpsLat;

	/**
	*所属路线
	*/
	@Column(name = "line_id",nullable=false)
	private Integer lineId;

	/**
	*开始经度坐标
	*/
	@Column(name = "start_gps_lng")
	private Float startGpsLng;

	/**
	*顺序号
	*/
	@Column(name = "order_num")
	private Integer orderNum;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public Float getStartGpsLat() {
		return startGpsLat;
	}

	public void setStartGpsLat(Float startGpsLat) {
		this.startGpsLat=startGpsLat;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public Float getEndGpsLng() {
		return endGpsLng;
	}

	public void setEndGpsLng(Float endGpsLng) {
		this.endGpsLng=endGpsLng;
	}

	public Float getEndGpsLat() {
		return endGpsLat;
	}

	public void setEndGpsLat(Float endGpsLat) {
		this.endGpsLat=endGpsLat;
	}

	public Integer getLineId() {
		return lineId;
	}

	public void setLineId(Integer lineId) {
		this.lineId=lineId;
	}

	public Float getStartGpsLng() {
		return startGpsLng;
	}

	public void setStartGpsLng(Float startGpsLng) {
		this.startGpsLng=startGpsLng;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum=orderNum;
	}

}
