package cn.zhiyuan.ces.transport.biz;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.zhiyuan.ces.transport.entity.CarInfo;
import cn.zhiyuan.frame.IAbstractDao;


public interface ICarInfoBiz extends IAbstractDao<CarInfo> {

	
	//注册设备
	void regDevices();
	
	void regDevice(CarInfo carInfo,String[] names);
	
	void startVideo(CarInfo carInfo);
	
	void stopVideo(CarInfo carInfo);
	
	List<JSONObject> treeJson();

	//获取车辆在线状态
	List<CarInfo> getCarStatusList();

	List<CarInfo> carGps();
	
	CarInfo playVideo(CarInfo carInfo, boolean isLocal,String cameraId);
	
	JSONArray getList4Client(Integer unitId);
	
}
