package cn.zhiyuan.ces.base.web;

import cn.zhiyuan.ces.base.biz.ILocalPoliceStationBiz;
import cn.zhiyuan.ces.base.entity.LocalPoliceStation;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.zhiyuan.frame.*;


@Controller
@RequestMapping("/base")
public final class LocalPoliceStationAction extends BaseAction{

	@Autowired
	private ILocalPoliceStationBiz iLocalPoliceStationBiz;

	@RequestMapping("/localpolicestation/jsons") 
	public ModelAndView json(PageBean<LocalPoliceStation> pb,LocalPoliceStation localPoliceStation) {
		iLocalPoliceStationBiz.getPageList(pb,localPoliceStation,null);
		return json(pb);
	}

	@RequestMapping("/localpolicestation/boxJsons") 
	public ModelAndView json(MsgBean mbean,LocalPoliceStation localPoliceStation) {
		mbean.setObj(iLocalPoliceStationBiz.getList(localPoliceStation,null));
		return json( mbean);
	}
	
	@RequestMapping("/localpolicestation/save") 
	public ModelAndView save(MsgBean mbean,LocalPoliceStation localPoliceStation) {
		mbean.setInfo("保存成功");
		iLocalPoliceStationBiz.save(localPoliceStation);
		return json(mbean);
	}

	@RequestMapping("/localpolicestation/delete") 
	public ModelAndView delete(MsgBean mbean,LocalPoliceStation localPoliceStation) {
		mbean.setInfo("删除成功");
		iLocalPoliceStationBiz.delete(localPoliceStation);
		return json(mbean);
	}

}
