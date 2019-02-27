package cn.zhiyuan.ces.camera.sip;

import javax.sip.Dialog;

import cn.zhiyuan.ces.camera.entity.CameraBean;

public class SipCamera extends CameraBean{

	private final static byte ptz1 = (byte)0xA5;
	private final static byte ptz2 = (byte)0x0F;//版本为0，默认为0F
	
	public  final static byte CMD_NOOP = (byte)0x00;//上
	public  final static byte CMD_RIGHT = (byte)0x01;//右
	public  final static byte CMD_LEFT = (byte)0x02;//左
	public  final static byte CMD_DOWN = (byte)0x04;//下
	public  final static byte CMD_UP = (byte)0x08;//上
	public  final static byte CMD_ZOOMIN = (byte)0x10;//放大
	public  final static byte CMD_ZOOMOUT = (byte)0x20;//缩小
	
	/*
	 * 计算ptz命令字符串
	 * 1 num 通道编号
	 * 2 cmd 命令
	 * 3 data数据
	 * */
	public static String ptzCode(int num,byte ptz4,byte data) {
		byte ptz3 = (byte)num,ptz5 = 0,ptz6 = 0,ptz7 = 0;
		switch(ptz4) {
		case CMD_RIGHT:
		case CMD_LEFT:
			ptz5 = data;
			break;
		case CMD_DOWN:
		case CMD_UP:
			ptz6 = data;
			break;
		case CMD_ZOOMIN:
		case CMD_ZOOMOUT:
			ptz7 = (byte)(data << 4 + ((num >> 8) & 0x0f));
			break;
		}
		byte ptz8 = (byte)((ptz1 + ptz2 + ptz3 + ptz4 + ptz5 + ptz6 + ptz7) % 256);
		return String.format("%02X%02X%02X%02X%02X%02X%02X%02X"
				,ptz1,ptz2,ptz3,ptz4,ptz5,ptz6,ptz7,ptz8);
	}
	
	/*
	 * 摄像头名称
	 * */
	private String cameraName;
	
	private Dialog dialog;
	
	private String cameraId;
	
	private boolean online;

	//sip会话结束标志
	private boolean videoEnd;
	
	//关闭视频流
	public void closeVideo() {
		this.dialog = null;
	}
		
	public Dialog getDialog() {
		return dialog;
	}

	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}

	public String getCameraId() {
		return cameraId;
	}

	public void setCameraId(String cameraId) {
		this.cameraId = cameraId;
	}

	public String getCameraName() {
		return cameraName;
	}

	public void setCameraName(String cameraName) {
		this.cameraName = cameraName;
	}

	public boolean isVideoEnd() {
		return videoEnd;
	}

	public void setVideoEnd(boolean videoEnd) {
		this.videoEnd = videoEnd;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean isPullVideo() {
		return dialog != null;
	}

}
