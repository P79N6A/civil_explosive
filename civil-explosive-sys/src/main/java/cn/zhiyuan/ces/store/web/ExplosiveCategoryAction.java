package cn.zhiyuan.ces.store.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.zhiyuan.ces.store.biz.IExplosiveCategoryBiz;
import cn.zhiyuan.ces.store.entity.ExplosiveCategory;
import cn.zhiyuan.ces.store.entity.ExplosiveVariety;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.PageBean;

/*
 * 炸药类别
 * */
@Controller
@RequestMapping("/store")
public final class ExplosiveCategoryAction extends BaseAction{

	@Autowired
	private IExplosiveCategoryBiz iExplosiveCategoryBiz;

	@RequestMapping("/explosivecategory/jsons") 
	public ModelAndView json(PageBean<ExplosiveCategory> pb,ExplosiveCategory explosiveCategory) {
		iExplosiveCategoryBiz.getPageList(pb,explosiveCategory,null);
		return json(pb);
	}
	

	@RequestMapping("/explosivecategory/boxJson") 
	public ModelAndView boxJson(MsgBean mbean,ExplosiveCategory explosiveCategory) {
		mbean.setObj(iExplosiveCategoryBiz.getList(explosiveCategory,null));
		return json(mbean);
	}
	
	@RequestMapping("/explosivecategory/save") 
	public ModelAndView save(@ModelAttribute MsgBean mbean,ExplosiveCategory explosiveCategory) {
		mbean.setInfo("保存成功");
		log.debug("类别测试:" + mbean);
		iExplosiveCategoryBiz.save(explosiveCategory);
		return json(mbean);
	}

	@RequestMapping("/explosivecategory/delete") 
	public ModelAndView delete(MsgBean mbean,ExplosiveCategory explosiveCategory) {
		mbean.setInfo("删除成功");
		iExplosiveCategoryBiz.delete(explosiveCategory);
		return json(mbean);
	}

}
