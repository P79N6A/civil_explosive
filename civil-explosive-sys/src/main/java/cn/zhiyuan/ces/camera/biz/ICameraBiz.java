package cn.zhiyuan.ces.camera.biz;

import java.util.List;

import cn.zhiyuan.ces.camera.entity.CameraBean;

/*
 * 摄像头控制接口
 * */
public interface ICameraBiz {
	static final String CAMERACALL_URL_REGDEVICE 		= "/regDevice";
	static final String CAMERACALL_URL_QUERYSTATUS 		= "/queryStatus";
	static final String CAMERACALL_URL_REMOVEDEVICE 	= "/removeDevice";
	static final String CAMERACALL_URL_SYNCDEVICE 		= "/syncDevice";
	static final String CAMERACALL_URL_DEVICELIST 		= "/deviceList";
	static final String CAMERACALL_URL_STARTVIDEO 		= "/startVideo";
	static final String CAMERACALL_URL_STOPVIDEO 		= "/stopVideo";
	static final String CAMERACALL_URL_STARTSAVE		= "/startSave";
	static final String CAMERACALL_URL_STOPSAVE			= "/stopSave";
	static final String CAMERACALL_URL_PTZCMD			= "/ptzCmd";//控制命令
	
	
	void init();
	
	//设备状态遍历
	void onTimer();
	
	/*
	 * 设备注册
	 * */
	boolean regDevice(CameraBean cb,int cameraCount);
	
	//从列表中删除
	void removeDevice(CameraBean cb);
	
	/*
	 * 同步设备状态
	 * */
	boolean syncDevice(CameraBean cb);
	
	/*
	 * 查询状态
	 * */
	boolean queryStatus(CameraBean cb);
	
	/*
	 * 开始视频
	 * */
	boolean startVideo(CameraBean cb);
	
	/*
	 * 结束视频
	 * */
	void stopVideo(CameraBean cb);
	
	void stopVideoCall(CameraBean cb);
	
	/*
	 * 开始保存
	 * */
	boolean startSave(CameraBean cb);
	
	/*
	 * 结束保存
	 * */
	boolean stopSave(CameraBean cb);
	
	/*
	 * 注册设备列表
	 * */
	 List<CameraBean>  deviceList(int platType);
	
	/*
	 * 设备控制命令
	 * */
	boolean ptzCmd(CameraBean cb);
	
}
