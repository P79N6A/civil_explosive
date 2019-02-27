package cn.zhiyuan.ces.camera.web;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.zhiyuan.ces.base.biz.IVideoRecordBiz;
import cn.zhiyuan.ces.camera.biz.ICameraBiz;
import cn.zhiyuan.ces.camera.biz.impl.CameraUitls;
import cn.zhiyuan.ces.camera.entity.CameraBean;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.MsgBean;


@Controller
@RequestMapping("/camera")
public final class CameraAction extends BaseAction{

	@Autowired
	private ICameraBiz cameraBiz;

	@Autowired
	private IVideoRecordBiz iVideoRecordBiz;
	
	@RequestMapping("/regDevice") 
	public ModelAndView regDevice(MsgBean mbean,CameraBean cb) {
		cameraBiz.regDevice(cb, 1);
		return json(mbean);
	}
	
	@RequestMapping("/saveRecord") 
	public ModelAndView saveRecord(MsgBean mbean,CameraBean cb,Integer deviceType,Long kssj,Long jssj,String path,Long fileSize) {
		log.debug("kssj="+kssj + ",jssj=" + jssj + ",path=" + path);
		Date kssjDate = new Date(kssj);
		Date jssjDate = new Date(jssj);
		if(CameraUitls.isSceneDevice(deviceType)) {
			iVideoRecordBiz.saveVideoRecord(cb.getDeviceId(), cb.getAppName(), path, kssjDate, jssjDate, fileSize);
		}
		return json(mbean);
	}
	
	@RequestMapping("/stopVideoCall") 
	public ModelAndView stopVideoCall(MsgBean mbean,CameraBean cb) {
		cameraBiz.stopVideoCall(cb);
		return json(mbean);
	}
	
	@RequestMapping("/removeDevice") 
	public ModelAndView removeDevice(MsgBean mbean,CameraBean cb) {
		cameraBiz.removeDevice(cb);
		return json(mbean);
	}
	
	//设备列表
	@RequestMapping("/deviceList") 
	public ModelAndView deviceList(MsgBean mbean,CameraBean cb) {
		cameraBiz.deviceList(cb.getPlatType());
		return json(mbean);
	}
	
	@RequestMapping("/syncDevice") 
	public ModelAndView syncDevice(MsgBean mbean,CameraBean cb) {
		cameraBiz.syncDevice(cb);
		return json(mbean);
	}
	
	@RequestMapping("/startVideo") 
	public ModelAndView startVideo(MsgBean mbean,CameraBean cb) {
		cameraBiz.startVideo(cb);
		return json(mbean);
	}
	
	@RequestMapping("/stopVideo") 
	public ModelAndView stopVideo(MsgBean mbean,CameraBean cb) {
		cameraBiz.stopVideo(cb);
		return json(mbean);
	}
	
	@RequestMapping("/startSave") 
	public ModelAndView startSave(MsgBean mbean,CameraBean cb) {
		cameraBiz.startSave(cb);
		return json(mbean);
	}
	
	@RequestMapping("/stopSave") 
	public ModelAndView stopSave(MsgBean mbean,CameraBean cb) {
		cameraBiz.stopSave(cb);
		return json(mbean);
	}
	
}
