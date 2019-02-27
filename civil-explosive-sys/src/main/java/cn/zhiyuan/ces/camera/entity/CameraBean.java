package cn.zhiyuan.ces.camera.entity;


/*
 * 摄像头相关参数
 * */
public class CameraBean {

	private int carId;
	private int ilat;
	private int ilng;
	
	/*
	 * 设备ID
	 * */
	private String deviceId;
	/*
	 * 摄像头ID
	 * */
	private String cameraId;
	/*
	 * 平台类型
	 * 1 28181 2 hik
	 * */
	private int platType;
	/*
	 * 是否自动保存
	 * */
	private boolean autoSave;
	/*
	 * 视频流类型
	 * 0 主码流 1 子码流
	 * */
	private int streamType;
	
	private String appName;
	
	/*
	 * 声音编码16进制
	 * 7A
	 * 8A
	 * AF
	 * */
	private String audioCode;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getCameraId() {
		return cameraId;
	}

	public void setCameraId(String cameraId) {
		this.cameraId = cameraId;
	}

	public int getPlatType() {
		return platType;
	}

	public void setPlatType(int platType) {
		this.platType = platType;
	}

	public boolean isAutoSave() {
		return autoSave;
	}

	public int getStreamType() {
		return streamType;
	}

	public void setStreamType(int streamType) {
		this.streamType = streamType;
	}

	public void setAutoSave(boolean autoSave) {
		this.autoSave = autoSave;
	}

	public String getAudioCode() {
		return audioCode;
	}

	public void setAudioCode(String audioCode) {
		this.audioCode = audioCode;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}

	public int getIlat() {
		return ilat;
	}

	public void setIlat(int ilat) {
		this.ilat = ilat;
	}

	public int getIlng() {
		return ilng;
	}

	public void setIlng(int ilng) {
		this.ilng = ilng;
	}
	
}
