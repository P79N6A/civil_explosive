package cn.zhiyuan.ces.store.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.zhiyuan.ces.store.biz.IExplosiveVarietyBiz;
import cn.zhiyuan.ces.store.entity.ExplosiveVariety;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.PageBean;

/*
 * 品种
 * */
@Controller
@RequestMapping("/store")
public final class ExplosiveVarietyAction extends BaseAction{

	@Autowired
	private IExplosiveVarietyBiz iExplosiveVarietyBiz;

	@RequestMapping("/explosivevariety/jsons") 
	public ModelAndView json(PageBean<ExplosiveVariety> pb,ExplosiveVariety explosiveVariety) {
		iExplosiveVarietyBiz.getPageList(pb,explosiveVariety,null);
		return json(pb);
	}

	@RequestMapping("/explosivevariety/boxJson") 
	public ModelAndView boxJson(MsgBean mbean,ExplosiveVariety explosiveVariety) {
		mbean.setObj(iExplosiveVarietyBiz.getList(explosiveVariety,null));
		return json(mbean);
	}
	
	@RequestMapping("/explosivevariety/save") 
	public ModelAndView save(MsgBean mbean,ExplosiveVariety explosiveVariety) {
		mbean.setInfo("保存成功");
		iExplosiveVarietyBiz.save(explosiveVariety);
		return json(mbean);
	}

	@RequestMapping("/explosivevariety/delete") 
	public ModelAndView delete(MsgBean mbean,ExplosiveVariety explosiveVariety) {
		mbean.setInfo("删除成功");
		iExplosiveVarietyBiz.delete(explosiveVariety);
		return json(mbean);
	}

}
