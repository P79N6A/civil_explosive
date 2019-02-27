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
import cn.zhiyuan.ces.contract.biz.IItemTransportCardBiz;
import cn.zhiyuan.ces.contract.entity.ItemTransportCard;
import cn.zhiyuan.frame.BaseDao;
import cn.zhiyuan.frame.CommonUtils;


@Service
public class ItemTransportCardBizImpl extends BaseDao<ItemTransportCard> implements IItemTransportCardBiz {

	@Autowired
	private IUploadResourceBiz iUploadResourceBiz;

	@Autowired
	private IAuditFlowBiz iAuditFlowBiz;
	
	@Autowired
	private IExplosiveCardInfoBiz iExplosiveCardInfoBiz;

	//审核
	@Override
	public int auditSave(ItemTransportCard itemTransportCard, AuditFlow auditFlow) {
		auditFlow.setHostId(itemTransportCard.getId());
		auditFlow.setAuditTime(new Date());
		iAuditFlowBiz.addAndId(auditFlow);
		itemTransportCard.setAuditFlowId(auditFlow.getId());
		return super.update(itemTransportCard);
	}

	@Override
	public int save(ItemTransportCard itemTransportCard, String uploadIds, String cardIds, AuditFlow auditFlow) {
		if(itemTransportCard.getId() == null) {
			itemTransportCard.setCreateDate(new Date());
			super.addAndId(itemTransportCard);
			if(uploadIds != null) {
				iUploadResourceBiz.updateHostId(itemTransportCard.getId(),
						CommonUtils.str2IntList(uploadIds));
			}
			if(cardIds != null) {
				iExplosiveCardInfoBiz.updateHostId(itemTransportCard.getId(),
						CommonUtils.str2IntList(cardIds));
			}
		}else super.update(itemTransportCard);
		auditFlow.setHostId(itemTransportCard.getId());
		auditFlow.setAuditTime(new Date());
		iAuditFlowBiz.addAndId(auditFlow);
		ItemTransportCard obj = new ItemTransportCard();
		obj.setId(itemTransportCard.getId());
		obj.setAuditStatus(itemTransportCard.getAuditStatus());
		obj.setAuditFlowId(auditFlow.getId());
		super.update(obj);
		return 1;
	}

	@Override
	public List<ItemTransportCard> getListByStatus(Integer unitId, Integer status) {
		String sql = "select t1.id,t1.code from item_transport_card t1,explosive_contract_record t2 "
				+ " where t1.item_id = t2.id and  t1.audit_status = :status "
				+ " and t2.apply_unit_id = :unitId order by t1.id desc";
		Map<String,Object> params = new HashMap<>();
		params.put("unitId", unitId);
		params.put("status", status);
		return super.queryList(sql, params);
	}
	
}
