package cn.zhiyuan.ces.contract.web;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.zhiyuan.ces.base.entity.AuditFlow;
import cn.zhiyuan.ces.contract.biz.IPurchaseCardBiz;
import cn.zhiyuan.ces.contract.entity.PurchaseCard;
import cn.zhiyuan.ces.sys.entity.SysUser;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.CommonUtils;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.PageBean;
import cn.zhiyuan.frame.filter.SysFilter;
import cn.zhiyuan.frame.orm.builder.IWhereSql;


@Controller
@RequestMapping("/contract")
public final class PurchaseCardAction extends BaseAction{

	public final static String AUDIT_TYPE = "6";//爆破物品购买许可证
	
	@Autowired
	private IPurchaseCardBiz iPurchaseCardBiz;
	@RequestMapping("/purchasecard/jsons") 
	public ModelAndView json(PageBean<PurchaseCard> pb,PurchaseCard purchaseCard
			,final Date kssj,final Date jssj) {
		iPurchaseCardBiz.getPageList(pb,purchaseCard,"t.id desc",new IWhereSql() {
			
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
	@RequestMapping("/purchasecard/appJsons") 
	public ModelAndView appJsons(MsgBean mbean,Integer status) {
		SysUser  user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		mbean.setObj(iPurchaseCardBiz.getListByStatus(user.getUnitId(),status));
		return json(mbean);
	}
	
	//备案
	@RequestMapping("/purchasecard/save1") 
	public ModelAndView save1(MsgBean mbean,PurchaseCard purchaseCard,String uploadIds
			,String cardIds,AuditFlow auditFlow) {
		mbean.setInfo("备案成功");
		purchaseCard.setAuditStatus(AuditFlow.AUDIT_STATUS_START);
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		auditFlow.setAuditType(AUDIT_TYPE);//审核类型
		auditFlow.setAuditStep("1");
		auditFlow.setAuditResult(1);
		purchaseCard.setStatus("1");//证件初始状态
		iPurchaseCardBiz.save(purchaseCard,uploadIds,cardIds,auditFlow);
		return json(mbean);
	}
	
	//受理
	@RequestMapping("/purchasecard/save2") 
	public ModelAndView save2(MsgBean mbean,PurchaseCard purchaseCard,String uploadIds
			,AuditFlow auditFlow) {
		mbean.setInfo("受理成功");
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		purchaseCard.setAuditStatus(AuditFlow.AUDIT_STATUS_GOON);
		auditFlow.setAuditType(AUDIT_TYPE);//审核类型
		purchaseCard.setAuditStatus(0);
		auditFlow.setAuditStep("2");
		iPurchaseCardBiz.auditSave(purchaseCard,auditFlow);
		return json(mbean);
	}
	//审核
	@RequestMapping("/purchasecard/save3") 
	public ModelAndView save3(MsgBean mbean,PurchaseCard purchaseCard,String uploadIds
			,AuditFlow auditFlow) {
		mbean.setInfo("审核成功");
		purchaseCard.setAuditStatus(AuditFlow.AUDIT_STATUS_GOON);
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		auditFlow.setAuditType(AUDIT_TYPE);//审核类型
		auditFlow.setAuditStep("3");
		iPurchaseCardBiz.auditSave(purchaseCard,auditFlow);
		return json(mbean);
	}
	//审批
	@RequestMapping("/purchasecard/save4") 
	public ModelAndView save4(MsgBean mbean,PurchaseCard purchaseCard,String uploadIds
			,AuditFlow auditFlow) {
		mbean.setInfo("审核成功");
		purchaseCard.setAuditStatus(AuditFlow.AUDIT_STATUS_GOON);
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		auditFlow.setAuditType(AUDIT_TYPE);//审核类型
		auditFlow.setAuditStep("4");
		iPurchaseCardBiz.auditSave(purchaseCard,auditFlow);
		return json(mbean);
	}

	//审批
	@RequestMapping("/purchasecard/save5") 
	public ModelAndView save5(MsgBean mbean,PurchaseCard purchaseCard,String uploadIds
			,AuditFlow auditFlow) {
		mbean.setInfo("审批成功");
		purchaseCard.setAuditStatus(AuditFlow.AUDIT_STATUS_END);
		SysUser user = (SysUser)super.getHttpSession().getAttribute(SysFilter.USER);
		auditFlow.setAuditId(user.getUserId());//审核人
		auditFlow.setAuditType(AUDIT_TYPE);//审核类型
		auditFlow.setAuditStep("5");
		iPurchaseCardBiz.auditSave(purchaseCard,auditFlow);
		return json(mbean);
	}
		
}
