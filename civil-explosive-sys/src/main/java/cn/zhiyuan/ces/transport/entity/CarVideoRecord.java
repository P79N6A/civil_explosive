package cn.zhiyuan.ces.transport.entity;

import javax.persistence.*;

import cn.zhiyuan.frame.orm.Formula;


@Table(name="car_video_record")
public class CarVideoRecord {
	
	/**
	*所属单位名称
	*/
	@Formula(name = "unit_name",value="select tt1.short_name from working_unit tt1,car_info tt2 "
			+ " where tt1.id= tt2.unit_id and tt2.id = t.car_id")
	private String unitName;
	
	/**
	*车辆号牌
	*/
	@Formula(name = "car_name",value="select tt.car_num from car_info tt "
			+ " where tt.id= t.car_id")
	private String carName;
	
	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*设备外键
	*/
	@Column(name = "car_id",nullable=false)
	private Integer carId;

	/**
	*摄像头id
	*/
	@Column(name = "camera_id",nullable=false)
	private String cameraId;

	/**
	*摄像头名称
	*/
	@Column(name = "camera_name",nullable=false)
	private String cameraName;

	/**
	*项目id
	*/
	@Column(name = "item_id")
	private Integer itemId;

	/**
	*
	*/
	@Column(name = "video_path1")
	private String videoPath1;

	/**
	*
	*/
	@Column(name = "video_path2")
	private String videoPath2;

	/**
	*
	*/
	@Column(name = "start_time",nullable=false)
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd HH:mm:ss")
	private java.util.Date startTime;

	/**
	*
	*/
	@Column(name = "end_time")
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd  HH:mm:ss")
	private java.util.Date endTime;

	/**
	*
	*/
	@Column(name = "video_size")
	private Long videoSize;

	/**
	*
	*/
	@Column(name = "remark")
	private String remark;



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

	public String getCameraId() {
		return cameraId;
	}

	public void setCameraId(String cameraId) {
		this.cameraId=cameraId;
	}

	public String getCameraName() {
		return cameraName;
	}

	public void setCameraName(String cameraName) {
		this.cameraName=cameraName;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId=itemId;
	}

	public String getVideoPath1() {
		return videoPath1;
	}

	public void setVideoPath1(String videoPath1) {
		this.videoPath1=videoPath1;
	}

	public String getVideoPath2() {
		return videoPath2;
	}

	public void setVideoPath2(String videoPath2) {
		this.videoPath2=videoPath2;
	}

	public java.util.Date getStartTime() {
		return startTime;
	}

	public void setStartTime(java.util.Date startTime) {
		this.startTime=startTime;
	}

	public java.util.Date getEndTime() {
		return endTime;
	}

	public void setEndTime(java.util.Date endTime) {
		this.endTime=endTime;
	}

	public Long getVideoSize() {
		return videoSize;
	}
	public String getVideoSizeStr() {
		return videoSize == null?null:String.format("%,d",videoSize);
	}
	public void setVideoSize(Long videoSize) {
		this.videoSize=videoSize;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark=remark;
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

}
