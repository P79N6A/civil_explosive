package cn.zhiyuan.ces.store.biz.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zhiyuan.ces.base.biz.IAuditFlowBiz;
import cn.zhiyuan.ces.base.entity.AuditFlow;
import cn.zhiyuan.ces.store.biz.IExplosiveStoreBiz;
import cn.zhiyuan.ces.store.entity.ExplosiveStore;
import cn.zhiyuan.frame.BaseDao;

@Service
public class ExplosiveStoreBizImpl extends BaseDao<ExplosiveStore> implements IExplosiveStoreBiz {

	@Autowired
	private IAuditFlowBiz iAuditFlowBiz;
	
	@Override
	public int save(ExplosiveStore explosiveStore, AuditFlow auditFlow) {
		if(explosiveStore.getId() == null) {
			explosiveStore.setCreateDate(new Date());
			super.addAndId(explosiveStore);
		}else {
			super.update(explosiveStore);
		}
		auditFlow.setHostId(explosiveStore.getId());
		auditFlow.setAuditTime(new Date());
		iAuditFlowBiz.addAndId(auditFlow);
		ExplosiveStore obj = new ExplosiveStore();
		obj.setId(explosiveStore.getId());
		obj.setAuditFlowId(auditFlow.getId());
		obj.setAuditStatus(explosiveStore.getAuditStatus());
		return super.update(obj);
	}
	
	@Override
	public int auditSave(ExplosiveStore explosiveStore, AuditFlow auditFlow) {
		auditFlow.setHostId(explosiveStore.getId());
		auditFlow.setAuditTime(new Date());
		iAuditFlowBiz.addAndId(auditFlow);
		ExplosiveStore obj = new ExplosiveStore();
		obj.setId(explosiveStore.getId());
		obj.setAuditFlowId(auditFlow.getId());
		obj.setAuditStatus(explosiveStore.getAuditStatus());
		return super.update(obj);
	}

}
