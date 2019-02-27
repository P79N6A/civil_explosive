package cn.zhiyuan.ces.store.biz;

import org.springframework.transaction.annotation.Transactional;

import cn.zhiyuan.ces.base.entity.AuditFlow;
import cn.zhiyuan.ces.store.entity.ExplosiveStore;
import cn.zhiyuan.frame.IAbstractDao;


public interface IExplosiveStoreBiz extends IAbstractDao<ExplosiveStore> {

	//审核保存
	@Transactional
	int save(ExplosiveStore explosiveStore,AuditFlow auditFlow);
	
	@Transactional
	int auditSave(ExplosiveStore explosiveStore,AuditFlow auditFlow);
	
}
