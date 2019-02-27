package cn.zhiyuan.ces.contract.biz;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import cn.zhiyuan.ces.base.entity.AuditFlow;
import cn.zhiyuan.ces.contract.entity.ItemTransportCard;
import cn.zhiyuan.ces.contract.entity.PurchaseCard;
import cn.zhiyuan.frame.IAbstractDao;


public interface IItemTransportCardBiz extends IAbstractDao<ItemTransportCard> {

	@Transactional
	int auditSave(ItemTransportCard itemTransportCard,AuditFlow auditFlow);
	//备案
	@Transactional
	int save(ItemTransportCard itemTransportCard,
			String uploadIds, String cardIds,AuditFlow auditFlow);
	
	List<ItemTransportCard> getListByStatus(Integer unitId, Integer status);
}
