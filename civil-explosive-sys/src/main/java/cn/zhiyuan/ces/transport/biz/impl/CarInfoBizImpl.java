package cn.zhiyuan.ces.transport.biz.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.zhiyuan.ces.camera.biz.ICameraBiz;
import cn.zhiyuan.ces.camera.biz.ICameraConfigBiz;
import cn.zhiyuan.ces.camera.biz.impl.CameraUitls;
import cn.zhiyuan.ces.camera.entity.CameraBean;
import cn.zhiyuan.ces.sys.biz.ISysCodeBiz;
import cn.zhiyuan.ces.transport.biz.ICarInfoBiz;
import cn.zhiyuan.ces.transport.entity.CarInfo;
import cn.zhiyuan.frame.BaseDao;

@Service
public class CarInfoBizImpl extends BaseDao<CarInfo> implements ICarInfoBiz {
	
	@Autowired
	private ISysCodeBiz iSysCodeBiz;
	
	@Autowired
	private ICameraBiz cameraBiz;
	
	@Autowired
	protected ICameraConfigBiz configBiz;
	
	public CarInfo playVideo(CarInfo carInfo, boolean isLocal,String cameraId) {
		carInfo = super.get(carInfo);
		if(carInfo == null) return null;
		carInfo.setRtmpUrl(configBiz.getRtmpUrl(isLocal) + carInfo.getRtmpdCtx());
		CameraBean cb = new CameraBean();
		cb.setDeviceId(carInfo.getDeviceId());
		cb.setCameraId(cameraId);
		cb.setStreamType(carInfo.getStreamType());
		carInfo.setPlayPath(CameraUitls.createPlayPath(cb));
		return carInfo;
	}
	
	public void startVideo(CarInfo carInfo) {
		CameraBean cb = new CameraBean();
		cb.setDeviceId(carInfo.getDeviceId());
		cb.setCameraId("1");
		cameraBiz.startVideo(cb);
	}
	
	public void stopVideo(CarInfo carInfo) {
		CameraBean cb = new CameraBean();
		cb.setDeviceId(carInfo.getDeviceId());
		cb.setCameraId("1");
		cameraBiz.stopVideo(cb);
	}
	
	@Override
	public void regDevice(CarInfo car,String[] names) {
		if(names == null) names = iSysCodeBiz.getRtmpInfo("1");
		CameraBean cb = new CameraBean();
		cb.setDeviceId(car.getDeviceId());
		cb.setAutoSave(car.getAutoSave() == 1);
		cb.setPlatType(car.getPlatType());
		cb.setAudioCode(car.getAudioCode());
		cb.setAppName(names[0]);
		Integer st = car.getStreamType();
		if(st == null) st = 1;
		cb.setStreamType(st);
		cameraBiz.regDevice(cb, car.getCameraCount());
	}

	@Override
	public void regDevices() {
		String[] names = iSysCodeBiz.getRtmpInfo("1");
		List<CarInfo> carList = super.getList(null,"t.id asc");
		for(CarInfo car : carList) {
			regDevice(car,names);
		}
	}
	
	//获取车辆在线状态
	@Override
	public List<CarInfo> getCarStatusList(){
		String sql = "select t.id,t.device_id,t.plat_type from car_info t";
		List<CarInfo> carList = super.queryList(sql, null);
		for(CarInfo carinfo : carList) {
			CameraBean cb = new CameraBean();
			cb.setDeviceId(carinfo.getDeviceId());
			cb.setPlatType(carinfo.getPlatType());
			cb.setCameraId("1");
			carinfo.setOnline(cameraBiz.queryStatus(cb));
		}
		return carList;
	}
	
	@Override
	public List<JSONObject> treeJson() {
		List<JSONObject> carJsons = new ArrayList<>();
		List<CarInfo> carList = super.getList(null, "t.unit_id desc");
		for(CarInfo carinfo : carList) {
			JSONObject carJson = null;
			Integer unitId = null;
			//寻找单位
			for(JSONObject car : carJsons) {
				if(1 == car.getIntValue("nodeType") 
						&& carinfo.getUnitName().equals(car.getString("nodeText"))) {
					unitId = car.getIntValue("nodeId");
					break;
				}
			}
			//空,则新增
			if(unitId == null) {
				unitId = carinfo.getUnitId();
				carJson = new JSONObject();
				carJson.put("nodeId",unitId);
				carJson.put("nodeText", carinfo.getUnitName());
				carJson.put("nodeType", 1);
				carJsons.add(carJson);
			}
			//新增车辆
			carJson = new JSONObject();
			carJson.put("nodeId",carinfo.getDeviceId());
			carJson.put("nodeText", carinfo.getCarNum());
			carJson.put("parentId", unitId);
			carJson.put("nodeType", 2);
			carJson.put("state","closed");
			carJsons.add(carJson);
			//新增通道
			int count = carinfo.getCameraCount();
			for(int i = 1;i <= count;i++) {
				carJson = new JSONObject();
				carJson.put("nodeId",carinfo.getDeviceId() + i);
				carJson.put("nodeText", "通道" + i);
				carJson.put("parentId", carinfo.getDeviceId());
				carJson.put("deviceId", carinfo.getDeviceId());
				carJson.put("carNum", carinfo.getCarNum());
				carJson.put("cameraId", i);
				carJson.put("nodeType", 3);
				carJsons.add(carJson);
			}
		}
		return carJsons;
	}

	@Override
	public List<CarInfo> carGps() {
		String sql = "select t.id,t.gps_lng,t.gps_lat,t.car_num from car_info t";
		return super.queryList(sql, null);
	}

	@Override
	public JSONArray getList4Client(Integer unitId) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
