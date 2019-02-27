package cn.zhiyuan.ces.base.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;

import cn.zhiyuan.ces.base.biz.IWorkingUnitBiz;
import cn.zhiyuan.ces.scene.biz.ISceneCameraBiz;
import cn.zhiyuan.ces.sys.entity.SysUser;
import cn.zhiyuan.ces.transport.biz.ICarInfoBiz;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.filter.SysFilter;


/*
 * 客户端离线视频上传
 * */
@Controller
@RequestMapping("/base")
public final class ClientUploadAction extends BaseAction{

	@Autowired
	private IWorkingUnitBiz iWorkingUnitBiz;

	@Autowired
	private ISceneCameraBiz iSceneCameraBiz;
	
	@Autowired
	private ICarInfoBiz iCarInfoBiz;
	
	/*
	 * 查询设计施工or道路运输单位
	 * */
	@RequestMapping("/clientupload/units")
	public ModelAndView getList4Client(MsgBean mbean) {
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		mbean.setData(iWorkingUnitBiz.getList4Client(user.getUnitId()));
		return json(mbean);
	}
	
	/*
	 * 视频采集设备列表
	 * play:1现场2运输车辆
	 * */
	@RequestMapping("/clientupload/deviceList") 
	public ModelAndView deviceList(MsgBean mbean,Integer unitId,Integer place) {
		JSONArray jsons = null;
		if(1 == place) jsons = iSceneCameraBiz.getList4Client(unitId);
		else if(2 == place) jsons = iCarInfoBiz.getList4Client(unitId);
		mbean.setData(jsons);
		return json(mbean);
	}
	
	
	
}
