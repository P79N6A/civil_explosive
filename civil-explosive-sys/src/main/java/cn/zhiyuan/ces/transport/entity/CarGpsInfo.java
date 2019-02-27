package cn.zhiyuan.ces.transport.entity;

import javax.persistence.*;


@Table(name="car_gps_info")
public class CarGpsInfo {
	
	//线路间隔时间
	public final static long TIME_TICK =  1000 * 60 * 30;
	
	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*车辆设备Id
	*/
	@Column(name = "car_id",nullable=false)
	private Integer carId;

	/**
	*经度
	*/
	@Column(name = "gps_lng",nullable=false)
	private Double gpsLng;

	/**
	*纬度
	*/
	@Column(name = "gps_lat",nullable=false)
	private Double gpsLat;

	/**
	*信号设备采集时间戳
	*/
	@Column(name = "gps_device_time",nullable=false)
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd")
	private java.util.Date gpsDeviceTime;

	/**
	*插入数据库时间戳
	*/
	@Column(name = "gps_create_time",nullable=false)
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd")
	private java.util.Date gpsCreateTime;

	/**
	*备注
	*/
	@Column(name = "remark")
	private String remark;
	
	//方向
	@Column(name = "gps_direction")
	private Integer gpsDirection;
	
	//速度
	@Column(name = "gps_speed")
	private Integer gpsSpeed;
	
	//lng差距
	@Column(name = "lng_diff")
	private Integer lngDiff;
	
	//lat差距
	@Column(name = "lat_diff")
	private Integer latDiff;
		
		
		
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public Double getGpsLng() {
		return gpsLng;
	}

	public void setGpsLng(Double gpsLng) {
		this.gpsLng=gpsLng;
	}

	public Double getGpsLat() {
		return gpsLat;
	}

	public void setGpsLat(Double gpsLat) {
		this.gpsLat=gpsLat;
	}

	public java.util.Date getGpsDeviceTime() {
		return gpsDeviceTime;
	}

	public void setGpsDeviceTime(java.util.Date gpsDeviceTime) {
		this.gpsDeviceTime=gpsDeviceTime;
	}

	public java.util.Date getGpsCreateTime() {
		return gpsCreateTime;
	}

	public void setGpsCreateTime(java.util.Date gpsCreateTime) {
		this.gpsCreateTime=gpsCreateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark=remark;
	}

	public Integer getGpsDirection() {
		return gpsDirection;
	}

	public void setGpsDirection(Integer gpsDirection) {
		this.gpsDirection = gpsDirection;
	}

	public Integer getGpsSpeed() {
		return gpsSpeed;
	}

	public void setGpsSpeed(Integer gpsSpeed) {
		this.gpsSpeed = gpsSpeed;
	}

	public Integer getCarId() {
		return carId;
	}

	public void setCarId(Integer carId) {
		this.carId = carId;
	}

	public Integer getLngDiff() {
		return lngDiff;
	}

	public void setLngDiff(Integer lngDiff) {
		this.lngDiff = lngDiff;
	}

	public Integer getLatDiff() {
		return latDiff;
	}

	public void setLatDiff(Integer latDiff) {
		this.latDiff = latDiff;
	}

}
