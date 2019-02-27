package cn.zhiyuan.ces.transport.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.zhiyuan.frame.orm.Formula;


@Table(name="car_info")
public class CarInfo {

	private boolean online;
	
	private String playPath;

	private String rtmpUrl;
	
	public String getAutoSaveName() {
		if(autoSave == null) return null;
		return autoSave == 1?"是":"否";
	}

	public String getPlatTypeName() {
		if(platType == null) return null;
		return platType == 1?"GB28181":"海康平台";
	}
	
	/**
	*码流类型:0主码流 1子码流
	*/
	@Column(name = "stream_type")
	private Integer streamType;
	
	/**
	*音频编码
	*/
	@Column(name = "audio_code",nullable=false)
	private String audioCode;
	
	/**
	*rtmpd上下文
	*/
	@Column(name = "rtmpd_ctx")
	private String rtmpdCtx;
	
	/**
	*平台类型:1gb28181 2 海康平台
	*/
	@Column(name = "plat_type",nullable=false)
	private Integer platType;

	/**
	*自动录制
	*/
	@Column(name = "auto_save",nullable=false)
	private Integer autoSave;

	//最新经纬度
	/**
	*经度
	*/
	@Column(name = "gps_lng")
	private Double gpsLng;

	/**
	*纬度
	*/
	@Column(name = "gps_lat")
	private Double gpsLat;
	
	/**
	*申请单位名称
	*/
	@Formula(name="unit_name",value="select tt.name from working_unit tt "
				+ " where tt.id = t.unit_id ")
	private String unitName;
	
	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*所属从业单位
	*/
	@Column(name = "unit_id",nullable=false)
	private Integer unitId;

	/**
	*车辆号牌
	*/
	@Column(name = "car_num",nullable=false)
	private String carNum;

	/**
	*设备编号
	*/
	@Column(name = "device_id")
	private String deviceId;

	/**
	*设备头数量
	*/
	@Column(name = "camera_count")
	private Integer cameraCount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId=unitId;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum=carNum;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId=deviceId;
	}

	public Integer getCameraCount() {
		return cameraCount;
	}

	public void setCameraCount(Integer cameraCount) {
		this.cameraCount=cameraCount;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Double getGpsLng() {
		return gpsLng;
	}

	public void setGpsLng(Double gpsLng) {
		this.gpsLng = gpsLng;
	}

	public Double getGpsLat() {
		return gpsLat;
	}

	public void setGpsLat(Double gpsLat) {
		this.gpsLat = gpsLat;
	}
	
	public String info() {
		return ("设备状态:" + deviceId);
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public String getAudioCode() {
		return audioCode;
	}

	public void setAudioCode(String audioCode) {
		this.audioCode = audioCode;
	}

	public Integer getStreamType() {
		return streamType;
	}

	public void setStreamType(Integer streamType) {
		this.streamType = streamType;
	}

	public String getRtmpdCtx() {
		return rtmpdCtx;
	}

	public void setRtmpdCtx(String rtmpdCtx) {
		this.rtmpdCtx = rtmpdCtx;
	}

	public Integer getPlatType() {
		return platType;
	}

	public void setPlatType(Integer platType) {
		this.platType = platType;
	}

	public Integer getAutoSave() {
		return autoSave;
	}

	public void setAutoSave(Integer autoSave) {
		this.autoSave = autoSave;
	}

	public String getRtmpUrl() {
		return rtmpUrl;
	}

	public void setRtmpUrl(String rtmpUrl) {
		this.rtmpUrl = rtmpUrl;
	}

	public String getPlayPath() {
		return playPath;
	}

	public void setPlayPath(String playPath) {
		this.playPath = playPath;
	}

}
