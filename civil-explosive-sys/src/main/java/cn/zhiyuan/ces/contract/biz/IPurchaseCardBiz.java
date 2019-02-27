package cn.zhiyuan.ces.contract.biz;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import cn.zhiyuan.ces.base.entity.AuditFlow;
import cn.zhiyuan.ces.contract.entity.PurchaseCard;
import cn.zhiyuan.frame.IAbstractDao;


public interface IPurchaseCardBiz extends IAbstractDao<PurchaseCard> {

	@Transactional
	int save(PurchaseCard purchaseCard, 
			String uploadIds,String cardIds, AuditFlow auditFlow);

	
	@Transactional
	int auditSave(PurchaseCard purchaseCard,AuditFlow auditFlow);

	List<PurchaseCard> getListByStatus(Integer unitId,Integer status);
	
}
