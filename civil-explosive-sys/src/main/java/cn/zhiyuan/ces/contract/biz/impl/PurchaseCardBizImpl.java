package cn.zhiyuan.ces.contract.biz.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zhiyuan.ces.base.biz.IAuditFlowBiz;
import cn.zhiyuan.ces.base.biz.IUploadResourceBiz;
import cn.zhiyuan.ces.base.entity.AuditFlow;
import cn.zhiyuan.ces.contract.biz.IExplosiveCardInfoBiz;
import cn.zhiyuan.ces.contract.biz.IPurchaseCardBiz;
import cn.zhiyuan.ces.contract.entity.PurchaseCard;
import cn.zhiyuan.frame.BaseDao;
import cn.zhiyuan.frame.CommonUtils;


@Service
public class PurchaseCardBizImpl extends BaseDao<PurchaseCard> implements IPurchaseCardBiz {

	@Autowired
	private IUploadResourceBiz iUploadResourceBiz;

	@Autowired
	private IAuditFlowBiz iAuditFlowBiz;
	
	@Autowired
	private IExplosiveCardInfoBiz iExplosiveCardInfoBiz;

	@Override
	public int save(PurchaseCard purchaseCard, String uploadIds,String cardIds,AuditFlow auditFlow) {
		if(purchaseCard.getId() == null) {
			purchaseCard.setCreateDate(new Date());
			super.addAndId(purchaseCard);
			if(uploadIds != null) {
				iUploadResourceBiz.updateHostId(purchaseCard.getId(),
						CommonUtils.str2IntList(uploadIds));
			}
			if(cardIds != null) {
				iExplosiveCardInfoBiz.updateHostId(purchaseCard.getId(),
						CommonUtils.str2IntList(cardIds));
			}
		}else super.update(purchaseCard);
		auditFlow.setHostId(purchaseCard.getId());
		auditFlow.setAuditTime(new Date());
		iAuditFlowBiz.addAndId(auditFlow);
		PurchaseCard obj = new PurchaseCard();
		obj.setId(purchaseCard.getId());
		obj.setAuditStatus(purchaseCard.getAuditStatus());
		obj.setAuditFlowId(auditFlow.getId());
		return super.update(obj);
	}
	
	@Override
	public int auditSave(PurchaseCard purchaseCard,AuditFlow auditFlow) {
		auditFlow.setHostId(purchaseCard.getId());
		auditFlow.setAuditTime(new Date());
		iAuditFlowBiz.addAndId(auditFlow);
		PurchaseCard obj = new PurchaseCard();
		obj.setId(purchaseCard.getId());
		obj.setAuditStatus(purchaseCard.getAuditStatus());
		obj.setAuditFlowId(auditFlow.getId());
		return super.update(obj);
	}

	@Override
	public List<PurchaseCard> getListByStatus(Integer unitId, Integer status) {
		String sql = "select t.id,t.code from purchase_card t "
				+ " where t.audit_status = :status and t.unit_id = :unitId order by t.id desc";
		Map<String,Object> params = new HashMap<>();
		params.put("unitId", unitId);
		params.put("status", status);
		return super.queryList(sql, params);
	}

}
