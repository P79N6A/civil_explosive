package cn.zhiyuan.ces.contract.web;

import cn.zhiyuan.ces.contract.biz.IOutsideTransportCardBiz;
import cn.zhiyuan.ces.contract.entity.OutsideTransportCard;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.zhiyuan.frame.*;


@Controller
@RequestMapping("/contract")
public final class OutsideTransportCardAction extends BaseAction{

	@Autowired
	private IOutsideTransportCardBiz iOutsideTransportCardBiz;

	@RequestMapping("/outsidetransportcard/json") 
	public ModelAndView json(PageBean<OutsideTransportCard> pb,OutsideTransportCard outsideTransportCard) {
		iOutsideTransportCardBiz.getPageList(pb,outsideTransportCard,null);
		return json(pb);
	}

	@RequestMapping("/outsidetransportcard/save") 
	public ModelAndView save(MsgBean mbean,OutsideTransportCard outsideTransportCard) {
		mbean.setInfo("保存成功");
		iOutsideTransportCardBiz.save(outsideTransportCard);
		return json(mbean);
	}

	@RequestMapping("/outsidetransportcard/delete") 
	public ModelAndView delete(MsgBean mbean,OutsideTransportCard outsideTransportCard) {
		mbean.setInfo("删除成功");
		iOutsideTransportCardBiz.delete(outsideTransportCard);
		return json(mbean);
	}

}
