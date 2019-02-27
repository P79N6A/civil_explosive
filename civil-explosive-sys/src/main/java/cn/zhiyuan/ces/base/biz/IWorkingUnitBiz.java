package cn.zhiyuan.ces.base.biz;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import cn.zhiyuan.ces.base.entity.AuditFlow;
import cn.zhiyuan.ces.base.entity.WorkingUnit;
import cn.zhiyuan.frame.IAbstractDao;


public interface IWorkingUnitBiz extends IAbstractDao<WorkingUnit> {

	int saveWorkingUnit(WorkingUnit workingUnit,String unitTypes);
	List<WorkingUnit> getList4Box(Integer auditStatus);
	//备案
	@Transactional
	int save(WorkingUnit workingUnit, String uploadIds, AuditFlow auditFlow);
	//审核资料
	@Transactional
	int auditSave(WorkingUnit workingUnit,AuditFlow auditFlow);
	
	/*
	 * 查询设计施工or道路运输单位
	 * */
	List<WorkingUnit> getList4Client(Integer uintId);

}
