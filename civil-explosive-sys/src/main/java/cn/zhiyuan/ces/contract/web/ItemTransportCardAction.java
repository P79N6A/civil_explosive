package cn.zhiyuan.ces.contract.web;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.zhiyuan.ces.base.entity.AuditFlow;
import cn.zhiyuan.ces.contract.biz.IItemTransportCardBiz;
import cn.zhiyuan.ces.contract.entity.ItemTransportCard;
import cn.zhiyuan.ces.sys.entity.SysUser;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.CommonUtils;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.PageBean;
import cn.zhiyuan.frame.filter.SysFilter;
import cn.zhiyuan.frame.orm.builder.IWhereSql;


@Controller
@RequestMapping("/contract")
public final class ItemTransportCardAction extends BaseAction{

	public final static String AUDIT_TYPE = "7";//运输证
	
	@Autowired
	private IItemTransportCardBiz iItemTransportCardBiz;

	@RequestMapping("/itemtransportcard/jsons") 
	public ModelAndView json(PageBean<ItemTransportCard> pb,ItemTransportCard itemTransportCard
			,final Date kssj,final Date jssj) {
		iItemTransportCardBiz.getPageList(pb,itemTransportCard,"t.id desc",new IWhereSql() {
			@Override
			public void where(StringBuilder sb, Map<String, Object> params) {
				if(kssj != null && jssj != null) {
					Date kssj1 = CommonUtils.clearDate(kssj);
					Date jssj1 = CommonUtils.fullDate(jssj);
					sb.append(" and t.create_date between :kssj and :jssj ");
					params.put("kssj", kssj1);
					params.put("jssj", jssj1);
				}
			}
		});
		return json(pb);
	}

	/*
	 * app端获取未受理购买证
	 * */
	@RequestMapping("/itemtransportcard/appJsons") 
	public ModelAndView appJsons(MsgBean mbean,Integer status) {
		SysUser  user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		mbean.setObj(iItemTransportCardBiz.getListByStatus(user.getUnitId(),status));
		return json(mbean);
	}
	
	//审核
	private void auditSave(ItemTransportCard itemTransportCard,AuditFlow auditFlow) {
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		auditFlow.setAuditType(AUDIT_TYPE);//审核类型
		iItemTransportCardBiz.auditSave(itemTransportCard, auditFlow);
	}
	
	//备案
	@RequestMapping("/itemtransportcard/save1") 
	public ModelAndView save1(MsgBean mbean,ItemTransportCard itemTransportCard
			,String uploadIds,String cardIds,AuditFlow auditFlow) {
		mbean.setInfo("保存成功");
		itemTransportCard.setAuditStatus(AuditFlow.AUDIT_STATUS_START);
		itemTransportCard.setStatus("1");
		auditFlow.setAuditStep("1");
		auditFlow.setAuditResult(1);
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		auditFlow.setAuditType(AUDIT_TYPE);//审核类型
		iItemTransportCardBiz.save(itemTransportCard,uploadIds,cardIds,auditFlow);
		return json(mbean);
	}

	//受理
	@RequestMapping("/itemtransportcard/save2") 
	public ModelAndView save2(MsgBean mbean,Integer id,Integer auditResult,String remark) {
		mbean.setInfo("受理成功");
		ItemTransportCard itemTransportCard = new ItemTransportCard();
		itemTransportCard.setId(id);
		AuditFlow auditFlow = new AuditFlow();
		auditFlow.setRemark(remark);
		auditFlow.setAuditResult(auditResult);
		itemTransportCard.setAuditStatus(AuditFlow.AUDIT_STATUS_GOON);
		auditFlow.setAuditStep("2");
		auditSave(itemTransportCard,auditFlow);
		return json(mbean);
	}
		
	//审核
	@RequestMapping("/itemtransportcard/save3") 
	public ModelAndView save3(MsgBean mbean,Integer id,Integer auditResult,String remark) {
		mbean.setInfo("审核成功");
		ItemTransportCard itemTransportCard = new ItemTransportCard();
		itemTransportCard.setId(id);
		AuditFlow auditFlow = new AuditFlow();
		auditFlow.setRemark(remark);
		auditFlow.setAuditResult(auditResult);
		itemTransportCard.setAuditStatus(AuditFlow.AUDIT_STATUS_GOON);
		auditFlow.setAuditStep("3");
		auditSave(itemTransportCard,auditFlow);
		return json(mbean);
	}
		
	//审批
	@RequestMapping("/itemtransportcard/save4") 
	public ModelAndView save4(MsgBean mbean,Integer id,Integer auditResult,String remark) {
		mbean.setInfo("审核成功");
		ItemTransportCard itemTransportCard = new ItemTransportCard();
		itemTransportCard.setId(id);
		AuditFlow auditFlow = new AuditFlow();
		auditFlow.setRemark(remark);
		auditFlow.setAuditResult(auditResult);
		itemTransportCard.setAuditStatus(AuditFlow.AUDIT_STATUS_GOON);
		auditFlow.setAuditStep("4");
		auditSave(itemTransportCard,auditFlow);
		return json(mbean);
	}
	
	//审批
	@RequestMapping("/itemtransportcard/save5") 
	public ModelAndView save5(MsgBean mbean,Integer id,Integer auditResult,String remark) {
		mbean.setInfo("审批成功");
		ItemTransportCard itemTransportCard = new ItemTransportCard();
		itemTransportCard.setId(id);
		AuditFlow auditFlow = new AuditFlow();
		auditFlow.setRemark(remark);
		auditFlow.setAuditResult(auditResult);
		itemTransportCard.setAuditStatus(AuditFlow.AUDIT_STATUS_END);
		auditFlow.setAuditStep("5");
		auditSave(itemTransportCard,auditFlow);
		return json(mbean);
	}
		
}
