package cn.zhiyuan.ces.transport.web;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.zhiyuan.ces.transport.biz.ICarVideoRecordBiz;
import cn.zhiyuan.ces.transport.entity.CarVideoRecord;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.CommonUtils;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.PageBean;
import cn.zhiyuan.frame.orm.builder.IWhereSql;


@Controller
@RequestMapping("/transport")
public final class CarVideoRecordAction extends BaseAction{

	@Autowired
	private ICarVideoRecordBiz iCarVideoRecordBiz;

	@RequestMapping("/carvideorecord/jsons") 
	public ModelAndView json(PageBean<CarVideoRecord> pb,CarVideoRecord carVideoRecord,Date kssj,Date jssj) {
		final Date kssj1 = CommonUtils.clearDate(kssj);
		final Date jssj1 = CommonUtils.fullDate(jssj);
		iCarVideoRecordBiz.getPageList(pb,carVideoRecord,"t.id desc",new IWhereSql() {
			@Override
			public void where(StringBuilder sb, Map<String, Object> params) {
				sb.append(" and t.start_time between :kssj and :jssj ");
				params.put("kssj", kssj1);
				params.put("jssj", jssj1);
			}
		});
		return json(pb);
	}

	@RequestMapping("/carvideorecord/save") 
	public ModelAndView save(MsgBean mbean,CarVideoRecord carVideoRecord) {
		mbean.setInfo("保存成功");
		iCarVideoRecordBiz.save(carVideoRecord);
		return json(mbean);
	}

	@RequestMapping("/carvideorecord/delete") 
	public ModelAndView delete(MsgBean mbean,CarVideoRecord carVideoRecord) {
		mbean.setInfo("删除成功");
		iCarVideoRecordBiz.delete(carVideoRecord);
		return json(mbean);
	}

}
