package cn.zhiyuan.ces.camera.biz.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import cn.zhiyuan.ces.camera.entity.CameraBean;
import cn.zhiyuan.frame.CommonUtils;

/*
 * 设备工具方法及常量
 * */
public final class CameraUitls {
	
	private static  Log log = LogFactory.getLog(CameraUitls.class);
	
	//设备离线时间 
	static public final int DEVICE_TIMEOUT = 30 * 1000;
		
	//直播视频超时时间
	static public final int LIVE_VIDEO_TIMEOUT = 2 * DEVICE_TIMEOUT;
		
	static public final Integer PLAT_GB28181 = 1;
	static public final Integer PLAT_HIK = 2;
	
	static public final Integer DEVICE_SCENE = 1;
	static public final Integer DEVICE_CAR = 2;
	static public final Integer DEVICE_STORE = 3;
	
	static final int UP_CTRL = 0;
	static final int RIGHT_CTRL = 1;
	static final int DOWN_CTRL = 2;
	static final int LEFT_CTRL = 3;
	static final int ZOOM_IN_CTRL = 4;
	static final int ZOOM_OUT_CTRL = 5;
	
	//是否现场设备
	static public  boolean isSceneDevice(Integer deviceType) {
		return DEVICE_SCENE.equals(deviceType);
	}
		
	//是否运输车辆
	static public  boolean isCarDevice(Integer deviceType) {
		return DEVICE_CAR.equals(deviceType);
	}
	
	//是否仓库
	static public  boolean isStoreDevice(Integer deviceType) {
		return DEVICE_STORE.equals(deviceType);
	}
		
	//是否海康平台
	static public  boolean isHIK(CameraBean cb) {
		return PLAT_HIK.equals(cb.getPlatType());
	}
		
	//是否GB28181平台
	static public boolean isGB28181(CameraBean cb) {
		return PLAT_GB28181.equals(cb.getPlatType());
	} 
		
	static public String createPlayPath(CameraBean cb) {
		Date date = new Date();
		String str = CommonUtils.date2str(date,"yyyy/MM/dd");
		return str + "/" + cb.getDeviceId() + "_" +  cb.getCameraId() +  "_" + cb.getStreamType();
	}
		
	//解析结果
	static public int parseResult(String resultStr) {
		try {
			log.debug(resultStr);
			if(StringUtils.isEmpty(resultStr)) return 0;
			Document doc = DocumentHelper.parseText(resultStr);
			Node node = doc.selectSingleNode("/http/id");
			if(node == null) return 0;
			return CommonUtils.str2int(node.getText(), -1);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static int sendCmd(String url,String method,String deviceId, String cameraId) {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("http");
		root.addElement("deviceId").addText(deviceId);
		root.addElement("cameraId").addText(cameraId);
		String result = CommonUtils.postXML(url + method, doc.asXML());
		return parseResult(result);
	}
	
	public static String sendCmdStr(String url,String method,String deviceId, String cameraId) {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("http");
		root.addElement("deviceId").addText(deviceId);
		root.addElement("cameraId").addText(cameraId);
		return CommonUtils.postXML(url + method, doc.asXML());
	}
	
}
