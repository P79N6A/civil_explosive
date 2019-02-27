package cn.zhiyuan.ces.camera.biz.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.SipException;
import javax.sip.message.MessageFactory;
import javax.sip.message.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zhiyuan.ces.camera.biz.ICameraBiz;
import cn.zhiyuan.ces.camera.entity.CameraBean;
import cn.zhiyuan.ces.camera.sip.AbstractSipServiceImpl;
import cn.zhiyuan.ces.camera.sip.SipCamera;
import cn.zhiyuan.ces.camera.sip.SipDevice;
import cn.zhiyuan.ces.transport.biz.ICarGpsInfoBiz;
import cn.zhiyuan.frame.CommonUtils;

@Service
public class CameraBizImpl extends AbstractSipServiceImpl implements ICameraBiz {

	private static  Log log = LogFactory.getLog(CameraBizImpl.class);
	
	@Autowired
	private ICarGpsInfoBiz iCarGpsInfoBiz;
	
	@Override
	public void init() {
		super.initSip(configBiz.getSipPort());
	}
	
	@Override
	public void onTimer() {
		for(SipDevice sipDevice : deviceMap.values()) {
			CameraBean cb = sipDevice.getCb();
			if(!sipDevice.isbReg()) continue;
			if(sipDevice.getOnline()) {//在线
				int num = 1;
				for(SipCamera sipCamera : sipDevice.getCameraList()) {
					if(cb.isAutoSave() && !sipCamera.isPullVideo()) {//未开始
						CameraBean newCb = sipDevice.createCameraBean("" + num);
						this.startVideo(newCb);
					}
					num++;
				}
			}else {//离线
				int num = 1;
				for(SipCamera sipCamera : sipDevice.getCameraList()) {
					if(sipCamera.isPullVideo()) {//未开始
						CameraBean newCb = sipDevice.createCameraBean("" + num);
						this.stopVideo(newCb);
					}
				}
				num++;
			}
		}
	}
	
	/*
	 * 设备状态在线
	 * */
	protected void onCameraStatus(SipDevice deviceBean) {
		int num = 1;
		if(deviceBean.isbReg()) return;
		log.debug("start reg device:" + deviceBean.getDeviceId());
		for(SipCamera sipCamera : deviceBean.getCameraList()) {
			CameraBean cb = deviceBean.createCameraBean(sipCamera.getCameraId());
			cb.setCameraId("" + num);
			num++;
			regDevice4Call(cb,0);
			if(cb.isAutoSave()) startVideo(cb);
		}
		deviceBean.setbReg(true);
	}
	
	public void stopVideoCall(CameraBean cb) {
		stopVideo(cb);
		if(CameraUitls.isGB28181(cb)) {
			SipDevice device = deviceMap.get(cb.getDeviceId());
			if(device != null) device.setTick(0);//设置离线
		}
	}
	
	/*
	 * 查询状态
	 * */
	public boolean queryStatus(CameraBean cb) {
		boolean bret = false;
		if(CameraUitls.isHIK(cb)) bret = sendCmd(cb,CAMERACALL_URL_QUERYSTATUS);
		else if(CameraUitls.isGB28181(cb)) {
			SipDevice device = deviceMap.get(cb.getDeviceId());
			if(device != null) bret = device.getOnline();
		}
		return bret;
	}
	
	private boolean regDevice4Call(CameraBean cb, int cameraCount) {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("http");
		CommonUtils.xmlAddNodeTxt(root, "deviceId", cb.getDeviceId());
		CommonUtils.xmlAddNodeTxt(root, "cameraId", cb.getCameraId());
		CommonUtils.xmlAddNodeTxt(root, "platType", cb.getPlatType());
		CommonUtils.xmlAddNodeTxt(root, "channelCount", cameraCount);
		CommonUtils.xmlAddNodeTxt(root, "streamType", cb.getStreamType());
		CommonUtils.xmlAddNodeTxt(root, "audioCode", cb.getAudioCode());
		CommonUtils.xmlAddNodeTxt(root, "appName", cb.getAppName());
		CommonUtils.xmlAddNodeTxt(root, "autoSave", cb.isAutoSave());
		String result = CommonUtils.postXML(configBiz.getHttpdUrl() 
							+ CAMERACALL_URL_REGDEVICE, doc.asXML());
		return  CameraUitls.parseResult(result) > 0;
	}
	
	protected void processBye(RequestEvent requestEvent) {
		log.debug("dialog1 = " + requestEvent.getDialog() + ",dilog2 = " 
				+ requestEvent.getServerTransaction().getDialog());
		try {
			Dialog dialog =  requestEvent.getDialog();
			for(SipDevice device : deviceMap.values()) {
				for(SipCamera camera : device.getCameraList()) {
					if(camera.getDialog() == null) continue;
					if(dialog == camera.getDialog()) {
						CameraBean cb = device.createCameraBean(camera.getCameraId());
						cb.setCameraId(camera.getCameraId());
						sendCmd(cb,CAMERACALL_URL_STOPVIDEO);
						camera.setDialog(null);//重置
						break;
					}
				}
			}
			MessageFactory msgFactory = sipFactory.createMessageFactory();
			Response response = msgFactory.createResponse(200, requestEvent.getRequest());
			requestEvent.getServerTransaction().sendResponse(response);
		} catch (SipException | InvalidArgumentException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean regDevice(CameraBean cb, int cameraCount) {
		//log.debug("regDevice:" + cb.getPlatType());
		boolean bret = false;
		if(CameraUitls.isHIK(cb)) {
			//bret = regDevice4Call(cb,cameraCount);
		}else{//28181
			SipDevice sipDevice = deviceMap.get(cb.getDeviceId());
			if(sipDevice == null) {
				sipDevice =  new SipDevice(cb);
				deviceMap.put(cb.getDeviceId(),sipDevice);
			}else {
				sipDevice.setCb(cb);
			}
			sipDevice.getCameraList().clear();//清空
			sipDevice.setbReg(false);
		}
		return bret;
	}

	@Override
	public void removeDevice(CameraBean cb) {
		sendCmd(cb,CAMERACALL_URL_REMOVEDEVICE);
		if(CameraUitls.isGB28181(cb)) {
			deviceMap.remove(cb.getDeviceId());
		}
	}

	@Override
	public boolean syncDevice(CameraBean cb) {
		boolean bret = false;
		if(CameraUitls.isHIK(cb)) {
			bret = sendCmd(cb,CAMERACALL_URL_SYNCDEVICE);
		}else if(CameraUitls.isGB28181(cb)) {//同步
			queryCamera(cb.getDeviceId());
		}
		return bret;
	}

	protected void onGpsInfo(SipDevice sipDevice,String lat,String lng) {
		if(StringUtils.isEmpty(lat) || StringUtils.isEmpty(lng))
			return;
		iCarGpsInfoBiz.onGpsMsg(sipDevice.getCb(), 0, 0, 0, 0, new Date());
	}
	
	//gb28181开启视频
	protected int onStartVideo(CameraBean cb) {
		return sendCmdInt(cb,CAMERACALL_URL_STARTVIDEO);
	}
	
	@Override
	public boolean startVideo(CameraBean cb) {
		boolean bret = false;
		if(CameraUitls.isHIK(cb)) {
			bret = sendCmd(cb,CAMERACALL_URL_STARTVIDEO);
		}else if(CameraUitls.isGB28181(cb)) {
			bret = super.startVideo28181(cb);
		}
		return bret;
	}

	@Override
	public void stopVideo(CameraBean cb) {
		if(CameraUitls.isGB28181(cb)) super.stopVideo(cb);
		sendCmd(cb,CAMERACALL_URL_STOPVIDEO);
	}

	@Override
	public boolean startSave(CameraBean cb) {
		return sendCmd(cb,CAMERACALL_URL_STARTSAVE);
	}

	@Override
	public boolean stopSave(CameraBean cb) {
		return sendCmd(cb,CAMERACALL_URL_STOPSAVE);
	}

	@Override
	public List<CameraBean> deviceList(int platType) {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("http");
		root.addElement("platType").addText(platType + "");
		List<CameraBean> cbList = new ArrayList<>();
		String result = CommonUtils.postXML(configBiz.getHttpdUrl() 
							+ CAMERACALL_URL_DEVICELIST, doc.asXML());
		log.debug(result);
		return cbList;
	}
	
	@Override
	public boolean ptzCmd(CameraBean cb) {
		
		return false;
	}
	
	//发送命令
	public boolean sendCmd(CameraBean cb,String method) {
		return  sendCmdInt(cb,method) > 0;
	}
	
	//发送命令
	public int sendCmdInt(CameraBean cb,String method) {
		return CameraUitls.sendCmd(configBiz.getHttpdUrl(),method
				,cb.getDeviceId(),cb.getCameraId());
	}
	
}
