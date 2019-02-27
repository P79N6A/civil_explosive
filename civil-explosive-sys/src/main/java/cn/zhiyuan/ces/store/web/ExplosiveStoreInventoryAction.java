package cn.zhiyuan.ces.store.web;

import cn.zhiyuan.ces.store.biz.IExplosiveStoreInventoryBiz;
import cn.zhiyuan.ces.store.entity.ExplosiveStoreInventory;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.zhiyuan.frame.*;


@Controller
@RequestMapping("/store")
public final class ExplosiveStoreInventoryAction extends BaseAction{

	@Autowired
	private IExplosiveStoreInventoryBiz iExplosiveStoreInventoryBiz;

	@RequestMapping("/explosivestoreinventory/json") 
	public ModelAndView json(PageBean<ExplosiveStoreInventory> pb,ExplosiveStoreInventory explosiveStoreInventory) {
		iExplosiveStoreInventoryBiz.getPageList(pb,explosiveStoreInventory,null);
		return json(pb);
	}

	@RequestMapping("/explosivestoreinventory/save") 
	public ModelAndView save(MsgBean mbean,ExplosiveStoreInventory explosiveStoreInventory) {
		mbean.setInfo("保存成功");
		iExplosiveStoreInventoryBiz.save(explosiveStoreInventory);
		return json(mbean);
	}

	@RequestMapping("/explosivestoreinventory/delete") 
	public ModelAndView delete(MsgBean mbean,ExplosiveStoreInventory explosiveStoreInventory) {
		mbean.setInfo("删除成功");
		iExplosiveStoreInventoryBiz.delete(explosiveStoreInventory);
		return json(mbean);
	}

}
