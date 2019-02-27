package cn.zhiyuan.ces.base.entity;

import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;

import cn.zhiyuan.frame.orm.Formula;


@Table(name="video_record")
public class VideoRecord {
	
	private String rtmpUrl;
	
	/**
	*所属单位名称
	*/
	@Formula(name = "unit_name",value="select tt.short_name from working_unit tt "
			+ " where tt.id= t.unit_id")
	private String unitName;
	
	/**
	*平台设备名称
	*/
	@Formula(name = "plat_device_name",value="select tt.camera_name from scene_camera tt "
			+ " where tt.id= t.plat_device_id")
	private String platDeviceName;
	
	@Column(name = "device_name")
	private String deviceName;
	
	/*
	 * 兼容旧数据
	 * */
	public String getCameraName() {
		String name = null;
		if(StringUtils.isEmpty(platDeviceName))
			name = deviceName;
		else 
			name = platDeviceName;
		return name;
	}
	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*视频大小
	*/
	@Column(name = "video_size")
	private Long videoSize;

	/**
	*设备外键
	*/
	@Column(name = "plat_device_id")
	private Integer platDeviceId;
	
	/**
	*摄像头sip id
	*/
	@Column(name = "camera_sip_id")
	private String cameraSipId;
	
	/**
	*所属单位
	*/
	@Column(name = "unit_id")
	private Integer unitId;

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
	@Column(name = "record_status")
	private String recordStatus;

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
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd HH:mm:ss")
	private java.util.Date endTime;
	
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

	public Integer getPlatDeviceId() {
		return platDeviceId;
	}

	public void setPlatDeviceId(Integer platDeviceId) {
		this.platDeviceId=platDeviceId;
	}

	public String getCameraSipId() {
		return cameraSipId;
	}

	public void setCameraSipId(String cameraSipId) {
		this.cameraSipId=cameraSipId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId=unitId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId=itemId;
	}

	/*
	public String getVideoPath() {
		return videoPath1 + videoPath2;
	}*/
	
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

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus=recordStatus;
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

	public String getPlatDeviceName() {
		return platDeviceName;
	}

	public void setPlatDeviceName(String platDeviceName) {
		this.platDeviceName = platDeviceName;
	}

	public Long getVideoSize() {
		return videoSize;
	}
	
	public String getVideoSizeStr() {
		return videoSize == null?null:String.format("%,d",videoSize);
	}

	public void setVideoSize(Long videoSize) {
		this.videoSize = videoSize;
	}

	public java.util.Date getStartTime() {
		return startTime;
	}

	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}

	public java.util.Date getEndTime() {
		return endTime;
	}

	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getRtmpUrl() {
		return rtmpUrl;
	}

	public void setRtmpUrl(String rtmpUrl) {
		this.rtmpUrl = rtmpUrl;
	}

}
