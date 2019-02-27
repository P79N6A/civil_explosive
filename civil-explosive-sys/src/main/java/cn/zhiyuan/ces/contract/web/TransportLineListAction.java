package cn.zhiyuan.ces.contract.web;

import cn.zhiyuan.ces.contract.biz.ITransportLineListBiz;
import cn.zhiyuan.ces.contract.entity.TransportLineList;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.zhiyuan.frame.*;


@Controller
@RequestMapping("/contract")
public final class TransportLineListAction extends BaseAction{

	@Autowired
	private ITransportLineListBiz iTransportLineListBiz;

	@RequestMapping("/transportlinelist/json") 
	public ModelAndView json(PageBean<TransportLineList> pb,TransportLineList transportLineList) {
		iTransportLineListBiz.getPageList(pb,transportLineList,null);
		return json(pb);
	}

	@RequestMapping("/transportlinelist/save") 
	public ModelAndView save(MsgBean mbean,TransportLineList transportLineList) {
		mbean.setInfo("保存成功");
		iTransportLineListBiz.save(transportLineList);
		return json(mbean);
	}

	@RequestMapping("/transportlinelist/delete") 
	public ModelAndView delete(MsgBean mbean,TransportLineList transportLineList) {
		mbean.setInfo("删除成功");
		iTransportLineListBiz.delete(transportLineList);
		return json(mbean);
	}

}
