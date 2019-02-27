package cn.zhiyuan.ces.transport.web;

import cn.zhiyuan.ces.transport.biz.ICarGpsInfoBiz;
import cn.zhiyuan.ces.transport.biz.ICarLineGpsBiz;
import cn.zhiyuan.ces.transport.entity.CarGpsInfo;
import cn.zhiyuan.ces.transport.entity.CarLineGps;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.zhiyuan.frame.*;
import cn.zhiyuan.frame.orm.builder.IWhereSql;


@Controller
@RequestMapping("/transport")
public final class CarLineGpsAction extends BaseAction{

	@Autowired
	private ICarLineGpsBiz iCarLineGpsBiz;

	@Autowired
	private ICarGpsInfoBiz iCarGpsInfoBiz;
	
	@RequestMapping("/carlinegps/jsons") 
	public ModelAndView jsons(PageBean<CarLineGps> pb,CarLineGps carLineGps,Date kssj,Date jssj) {
		final Date kssj1 = CommonUtils.clearDate(kssj);
		final Date jssj1 = CommonUtils.fullDate(jssj);
		iCarLineGpsBiz.getPageList(pb,carLineGps,"t.id desc",new IWhereSql() {
			@Override
			public void where(StringBuilder sb, Map<String, Object> params) {
				sb.append(" and t.start_time between :kssj and :jssj ");
				params.put("kssj", kssj1);
				params.put("jssj", jssj1);
			}
		});
		return json(pb);
	}
	
	@RequestMapping("/carlinegps/gpsJsons") 
	public ModelAndView gpsJsons(PageBean<CarGpsInfo> pb,CarLineGps carLineGps) {
		CarGpsInfo carGpsInfo = new CarGpsInfo();
		carGpsInfo.setCarId(carLineGps.getCarId());
		iCarGpsInfoBiz.getPageList(pb,carGpsInfo,"t.id asc",new IWhereSql() {
			@Override
			public void where(StringBuilder sb, Map<String, Object> params) {
				sb.append(" and t.id between :gpsStart and :gpsEnd ");
				params.put("gpsStart", carLineGps.getLineStart());
				params.put("gpsEnd", carLineGps.getLineEnd());
			}
		});
		return json(pb);
	}

	@RequestMapping("/carlinegps/save") 
	public ModelAndView save(MsgBean mbean,CarLineGps carLineGps) {
		mbean.setInfo("保存成功");
		iCarLineGpsBiz.save(carLineGps);
		return json(mbean);
	}

	@RequestMapping("/carlinegps/delete") 
	public ModelAndView delete(MsgBean mbean,CarLineGps carLineGps) {
		mbean.setInfo("删除成功");
		iCarLineGpsBiz.delete(carLineGps);
		return json(mbean);
	}

}
