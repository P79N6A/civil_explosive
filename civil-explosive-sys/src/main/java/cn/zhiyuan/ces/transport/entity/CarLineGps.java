package cn.zhiyuan.ces.transport.entity;

import java.util.Date;

import javax.persistence.*;

import cn.zhiyuan.frame.orm.Formula;


@Table(name="car_line_gps")
public class CarLineGps {
	
	/**
	*所属单位名称
	*/
	@Formula(name = "unit_name",value="select tt1.short_name from working_unit tt1,car_info tt2 "
			+ " where tt1.id= tt2.unit_id and tt2.id = t.car_id")
	private String unitName;
	
	/**
	*车辆名称
	*/
	@Formula(name = "car_name",value="select tt.car_num from car_info tt "
			+ " where tt.id= t.car_id")
	private String carName;
	
	/**
	*车辆设备代码
	*/
	@Formula(name = "car_code",value="select tt.device_id from car_info tt "
			+ " where tt.id= t.car_id")
	private String carCode;
	
	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*所属车辆
	*/
	@Column(name = "car_id",nullable=false)
	private Integer carId;

	/**
	*起点
	*/
	@Column(name = "line_start",nullable=false)
	private Integer lineStart;

	/**
	*终点
	*/
	@Column(name = "line_end")
	private Integer lineEnd;

	/**
	*起点时间戳
	*/
	@Column(name = "start_time",nullable=false)
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	/**
	*终点时间戳
	*/
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "end_time")
	private Date endTime;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public Integer getCarId() {
		return carId;
	}

	public void setCarId(Integer carId) {
		this.carId=carId;
	}

	public Integer getLineStart() {
		return lineStart;
	}

	public void setLineStart(Integer lineStart) {
		this.lineStart=lineStart;
	}

	public Integer getLineEnd() {
		return lineEnd;
	}

	public void setLineEnd(Integer lineEnd) {
		this.lineEnd=lineEnd;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public String getCarCode() {
		return carCode;
	}

	public void setCarCode(String carCode) {
		this.carCode = carCode;
	}

}
