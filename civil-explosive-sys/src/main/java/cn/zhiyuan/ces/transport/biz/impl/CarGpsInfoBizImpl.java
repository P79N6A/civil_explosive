package cn.zhiyuan.ces.transport.biz.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.zhiyuan.ces.camera.entity.CameraBean;
import cn.zhiyuan.ces.transport.biz.ICarGpsInfoBiz;
import cn.zhiyuan.ces.transport.entity.CarGpsInfo;
import cn.zhiyuan.frame.BaseDao;


@Service
public class CarGpsInfoBizImpl extends BaseDao<CarGpsInfo> implements ICarGpsInfoBiz {

	@Override
	public Integer findLastGpsId(Integer carId) {
		String sql = "select max(t.id) from car_gps_info t where t.car_id = :carId ";
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("carId", carId);
		Integer id =  super.queryObject(sql, paramMap, Integer.class);
		return id == null?0:id;
	}
	

  public void onGpsMsg(CameraBean cb, int ilng, int ilat, int speed, int direct, Date deviceTime)
  {
    CarGpsInfo cargps = new CarGpsInfo();
    cargps.setCarId(cb.getCarId());
    double lng = ilng / 36 / 10000.0D;
    double lat = ilat / 36 / 10000.0D;
    cargps.setGpsLng(Double.valueOf(lng));
    cargps.setGpsLat(Double.valueOf(lat));
    cargps.setGpsCreateTime(new Date());
    cargps.setGpsDeviceTime(deviceTime);
    cargps.setGpsSpeed(Integer.valueOf(speed));
    cargps.setGpsDirection(Integer.valueOf(direct));
    if (cb.getIlng() > 0)
    {
      cargps.setLngDiff(Integer.valueOf(ilng - cb.getIlng()));
      cargps.setLatDiff(Integer.valueOf(ilat - cb.getIlat()));
    }
    cb.setIlat(ilat);
    cb.setIlng(ilng);
    this.add(cargps);
    /*CarInfo carinfo = new CarInfo();
    carinfo.setId(cb.getId());
    carinfo.setGpsLat(Double.valueOf(lat));
    carinfo.setGpsLng(Double.valueOf(lng));*/
    //super.update(carinfo);
  }
	  
	  
}
