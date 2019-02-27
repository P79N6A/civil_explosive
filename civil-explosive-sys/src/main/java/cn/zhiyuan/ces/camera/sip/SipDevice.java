package cn.zhiyuan.ces.camera.sip;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

import cn.zhiyuan.ces.camera.biz.impl.CameraUitls;
import cn.zhiyuan.ces.camera.entity.CameraBean;
import cn.zhiyuan.frame.CommonUtils;

/*sip协议注册的设备*/
public class SipDevice{

	//摄像头列表
	private List<SipCamera> cameraList = new ArrayList<>();
	
	private CameraBean cb;
	
	//是否注册
	private boolean bReg;
		
	public SipDevice(CameraBean cb) {
		this.cb = cb;
		tick = 0;
	}
	
	private String deviceIp;
	
	private int devicePort;

	private long tick;
	
	private boolean login;
	
	private long seqNum;

	public SipCamera findCamera(String cameraId) {
		if(StringUtils.isEmpty(cameraId)) return null;
		for(SipCamera camera : cameraList) {
			if(cameraId.equals(camera.getCameraId())) return camera;
		}
		return null;
	}
	
	public SipCamera findCameraByIndex(String cameraId) {
		if(StringUtils.isEmpty(cameraId) || cameraList.isEmpty()) return null;
		int index = CommonUtils.str2int(cameraId, 1) - 1;
		if(index < 0 || index > cameraList.size()) index = 0;
		return cameraList.get(index);
	}
	
	public SipCamera addCamera(String cameraId,String cameraName) {
		SipCamera sipCamera = new SipCamera();
		sipCamera.setCameraId(cameraId);
		sipCamera.setCameraName(cameraName);
		cameraList.add(sipCamera);
		return sipCamera;
	}
	
	public boolean getOnline() {
		return ((new Date().getTime() - tick) > 2 * CameraUitls.DEVICE_TIMEOUT)?false:true;
	}
	
	public CameraBean createCameraBean(String cameraId) {
		CameraBean obj = null;
		obj = JSON.parseObject(JSON.toJSONString(cb), CameraBean.class);
		obj.setCameraId(cameraId);
		return obj;
	}
	
	public String info() {
		return getDeviceId() + "," + deviceIp;
	}
	
	public String getIpPort() {
		return deviceIp + ":" + devicePort;
	}
	
	public long plusSeqNum() {
		return ++seqNum;
	}

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public int getDevicePort() {
		return devicePort;
	}

	public void setDevicePort(int devicePort) {
		this.devicePort = devicePort;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	public long getSeqNum() {
		return seqNum;
	}

	public long getTick() {
		return tick;
	}

	public void setTick(long tick) {
		this.tick = tick;
	}

	public String getDeviceId() {
		return cb.getDeviceId();
	}

	public CameraBean getCb() {
		return cb;
	}

	public void setCb(CameraBean cb) {
		this.cb = cb;
	}

	public List<SipCamera> getCameraList() {
		return cameraList;
	}

	public boolean isbReg() {
		return bReg;
	}

	public void setbReg(boolean bReg) {
		this.bReg = bReg;
	}
	
}
