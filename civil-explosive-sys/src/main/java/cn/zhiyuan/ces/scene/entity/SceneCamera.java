package cn.zhiyuan.ces.scene.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.zhiyuan.frame.orm.Formula;


@Table(name="scene_camera")
public class SceneCamera {
	
	private String playPath;
	
	private String rtmpUrl;
	
	public String info() {
		return ("设备状态:" + sipId + ",cameraId:" 
				+ cameraCount);
	}
	
	public String getAutoSaveName() {
		if(autoSave == null) return null;
		return autoSave == 1?"是":"否";
	}

	public String getPlatTypeName() {
		if(platType == null) return null;
		return platType == 1?"GB28181":"海康平台";
	}
	
	/**
	*申请单位名称
	*/
	@Formula(name="unit_name",value="select tt.short_name from working_unit tt "
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

	private boolean online;
	
	/**
	*设备名称
	*/
	@Column(name = "camera_name",nullable=false)
	private String cameraName;

	/**
	*摄像头数量
	*/
	@Column(name = "camera_count")
	private Integer cameraCount;

	/**
	*设备型号
	*/
	@Column(name = "camera_mode")
	private String cameraMode;

	/**
	*设备id
	*/
	@Column(name = "sip_id",nullable=false)
	private String sipId;

	/**
	*设备sip密码
	*/
	@Column(name = "sip_pwd")
	private String sipPwd;

	
	
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

	public String getCameraName() {
		return cameraName;
	}

	public void setCameraName(String cameraName) {
		this.cameraName=cameraName;
	}

	public String getCameraMode() {
		return cameraMode;
	}

	public void setCameraMode(String cameraMode) {
		this.cameraMode=cameraMode;
	}

	public String getSipId() {
		return sipId;
	}

	public void setSipId(String sipId) {
		this.sipId=sipId;
	}

	public String getSipPwd() {
		return sipPwd;
	}

	public void setSipPwd(String sipPwd) {
		this.sipPwd=sipPwd;
	}

	public Integer getPlatType() {
		return platType;
	}

	public void setPlatType(Integer platType) {
		this.platType=platType;
	}

	public Integer getAutoSave() {
		return autoSave;
	}

	public void setAutoSave(Integer autoSave) {
		this.autoSave=autoSave;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public String getRtmpdCtx() {
		return rtmpdCtx;
	}

	public void setRtmpdCtx(String rtmpdCtx) {
		this.rtmpdCtx = rtmpdCtx;
	}

	public Integer getStreamType() {
		return streamType;
	}

	public void setStreamType(Integer streamType) {
		this.streamType = streamType;
	}

	public String getAudioCode() {
		return audioCode;
	}

	public void setAudioCode(String audioCode) {
		this.audioCode = audioCode;
	}

	public String getPlayPath() {
		return playPath;
	}

	public void setPlayPath(String playPath) {
		this.playPath = playPath;
	}

	public String getRtmpUrl() {
		return rtmpUrl;
	}

	public void setRtmpUrl(String rtmpUrl) {
		this.rtmpUrl = rtmpUrl;
	}

	public Integer getCameraCount() {
		return cameraCount;
	}

	public void setCameraCount(Integer cameraCount) {
		this.cameraCount = cameraCount;
	}

}
