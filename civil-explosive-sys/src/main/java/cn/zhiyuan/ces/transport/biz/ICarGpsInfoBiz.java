package cn.zhiyuan.ces.transport.biz;

import java.util.Date;

import cn.zhiyuan.ces.camera.entity.CameraBean;
import cn.zhiyuan.ces.transport.entity.CarGpsInfo;
import cn.zhiyuan.frame.IAbstractDao;


public interface ICarGpsInfoBiz extends IAbstractDao<CarGpsInfo> {

	//获取最新的gps记录id
	Integer findLastGpsId(Integer carId);
	
	void onGpsMsg(CameraBean cb, int ilng, int ilat, int speed, int direct, Date deviceTime);
	
}
