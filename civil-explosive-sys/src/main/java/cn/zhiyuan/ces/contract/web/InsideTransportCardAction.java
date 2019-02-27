package cn.zhiyuan.ces.contract.web;

import cn.zhiyuan.ces.contract.biz.IInsideTransportCardBiz;
import cn.zhiyuan.ces.contract.entity.InsideTransportCard;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.zhiyuan.frame.*;


@Controller
@RequestMapping("/contract")
public final class InsideTransportCardAction extends BaseAction{

	@Autowired
	private IInsideTransportCardBiz iInsideTransportCardBiz;

	@RequestMapping("/insidetransportcard/json") 
	public ModelAndView json(PageBean<InsideTransportCard> pb,InsideTransportCard insideTransportCard) {
		iInsideTransportCardBiz.getPageList(pb,insideTransportCard,null);
		return json(pb);
	}

	@RequestMapping("/insidetransportcard/save") 
	public ModelAndView save(MsgBean mbean,InsideTransportCard insideTransportCard) {
		mbean.setInfo("保存成功");
		iInsideTransportCardBiz.save(insideTransportCard);
		return json(mbean);
	}

	@RequestMapping("/insidetransportcard/delete") 
	public ModelAndView delete(MsgBean mbean,InsideTransportCard insideTransportCard) {
		mbean.setInfo("删除成功");
		iInsideTransportCardBiz.delete(insideTransportCard);
		return json(mbean);
	}

}
