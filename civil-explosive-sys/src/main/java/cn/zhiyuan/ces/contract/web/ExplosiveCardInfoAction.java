package cn.zhiyuan.ces.contract.web;

import cn.zhiyuan.ces.contract.biz.IExplosiveCardInfoBiz;
import cn.zhiyuan.ces.contract.entity.ExplosiveCardInfo;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.zhiyuan.frame.*;


@Controller
@RequestMapping("/contract")
public final class ExplosiveCardInfoAction extends BaseAction{

	@Autowired
	private IExplosiveCardInfoBiz iExplosiveCardInfoBiz;

	@RequestMapping("/explosivecardinfo/jsons") 
	public ModelAndView json(MsgBean mbean,ExplosiveCardInfo explosiveCardInfo) {
		mbean.setObj(iExplosiveCardInfoBiz.getList(explosiveCardInfo,null));
		return json(mbean);
	}

	@RequestMapping("/explosivecardinfo/save") 
	public ModelAndView save(MsgBean mbean,ExplosiveCardInfo explosiveCardInfo) {
		mbean.setInfo("保存成功");
		iExplosiveCardInfoBiz.save(explosiveCardInfo);
		return json(mbean);
	}

	@RequestMapping("/explosivecardinfo/delete") 
	public ModelAndView delete(MsgBean mbean,ExplosiveCardInfo explosiveCardInfo) {
		mbean.setInfo("删除成功");
		iExplosiveCardInfoBiz.delete(explosiveCardInfo);
		return json(mbean);
	}

}
