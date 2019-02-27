package cn.zhiyuan.ces.transport.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.zhiyuan.ces.transport.biz.ICarInfoBiz;
import cn.zhiyuan.ces.transport.entity.CarInfo;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.PageBean;


@Controller
@RequestMapping("/transport")
public final class CarInfoAction extends BaseAction{

	@Autowired
	private ICarInfoBiz iCarInfoBiz;

	
	@RequestMapping("/carinfo/jsons") 
	public ModelAndView json(PageBean<CarInfo> pb,CarInfo carInfo) {
		iCarInfoBiz.getPageList(pb,carInfo,null);
		return json(pb);
	}
	
	@RequestMapping("/carinfo/regAll") 
	public ModelAndView regAll(MsgBean mbean) {
		iCarInfoBiz.regDevices();
		return json(mbean);
	}
	
	@RequestMapping("/carinfo/regCar") 
	public ModelAndView regCar(MsgBean mbean,CarInfo carInfo) {
		iCarInfoBiz.regDevices();
		return json(mbean);
	}
	
	@RequestMapping("/carinfo/startVideo") 
	public ModelAndView startVideo(MsgBean mbean,CarInfo carInfo) {
		log.debug("startVideo");
		iCarInfoBiz.startVideo(carInfo);
		return json(mbean);
	}
	
	@RequestMapping("/carinfo/stopVideo") 
	public ModelAndView stopVideo(MsgBean mbean,CarInfo carInfo) {
		log.debug("stopVideo");
		iCarInfoBiz.stopVideo(carInfo);
		return json(mbean);
	}
	
	@RequestMapping("/carinfo/treeJson") 
	public ModelAndView json(MsgBean mbean,CarInfo carInfo) {
		mbean.setObj(iCarInfoBiz.treeJson());
		return json(mbean);
	}
	
	@RequestMapping("/carinfo/liveVideo") 
	public ModelAndView liveVideo(HttpServletRequest request,MsgBean mbean,
					CarInfo carInfo,String cameraId) {
		carInfo = iCarInfoBiz.get(carInfo);
		String ip = request.getRemoteAddr();
		boolean isLocal = ip.indexOf("192.168.") != -1?true:false;
		mbean.setData(iCarInfoBiz.playVideo(carInfo,isLocal,cameraId));
		return json(mbean);
	}	

	//车辆最新gps
	@RequestMapping("/carinfo/carGps") 
	public ModelAndView carGps(MsgBean mbean) {
		mbean.setObj(iCarInfoBiz.carGps());
		return json(mbean);
	}
	
	@RequestMapping("/carinfo/save") 
	public ModelAndView save(MsgBean mbean,CarInfo carInfo) {
		mbean.setInfo("保存成功");
		iCarInfoBiz.save(carInfo);
		return json(mbean);
	}
	
	@RequestMapping("/carinfo/carStatus") 
	public ModelAndView carStatus(MsgBean mbean) {
		mbean.setInfo("保存成功");
		mbean.setObj(iCarInfoBiz.getCarStatusList());
		return json(mbean);
	}
	
	@RequestMapping("/carinfo/delete") 
	public ModelAndView delete(MsgBean mbean,CarInfo carInfo) {
		mbean.setInfo("删除成功");
		iCarInfoBiz.delete(carInfo);
		return json(mbean);
	}

}
