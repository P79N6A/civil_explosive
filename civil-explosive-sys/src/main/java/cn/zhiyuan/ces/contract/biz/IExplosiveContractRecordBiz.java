package cn.zhiyuan.ces.contract.biz;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import cn.zhiyuan.ces.base.entity.AuditFlow;
import cn.zhiyuan.ces.contract.entity.ExplosiveContractRecord;
import cn.zhiyuan.frame.IAbstractDao;


public interface IExplosiveContractRecordBiz extends IAbstractDao<ExplosiveContractRecord> {

	//备案
	@Transactional
	int save(ExplosiveContractRecord explosiveContractRecord,
			String uploadIds, AuditFlow auditFlow);
	
	//审核
	@Transactional
	int auditSave(ExplosiveContractRecord explosiveContractRecord,AuditFlow auditFlow);

	//根据状态查询列表
	List<ExplosiveContractRecord> getList4Status(Integer applyUnitId,Integer status);
	
}
