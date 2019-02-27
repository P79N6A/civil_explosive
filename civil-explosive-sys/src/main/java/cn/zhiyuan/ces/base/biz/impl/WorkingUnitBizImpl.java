package cn.zhiyuan.ces.base.biz.impl;

import cn.zhiyuan.ces.base.entity.AuditFlow;
import cn.zhiyuan.ces.base.entity.WorkingUnit;
import cn.zhiyuan.ces.base.entity.WorkinguintType;
import cn.zhiyuan.ces.base.biz.IAuditFlowBiz;
import cn.zhiyuan.ces.base.biz.IUploadResourceBiz;
import cn.zhiyuan.ces.base.biz.IWorkingUnitBiz;
import cn.zhiyuan.ces.base.biz.IWorkinguintTypeBiz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.zhiyuan.frame.BaseDao;
import cn.zhiyuan.frame.CommonUtils;


@Service
public class WorkingUnitBizImpl extends BaseDao<WorkingUnit> implements IWorkingUnitBiz {

	@Autowired
	private IWorkinguintTypeBiz iWorkinguintTypeBiz;

	@Autowired
	private IUploadResourceBiz iUploadResourceBiz;

	@Autowired
	private IAuditFlowBiz iAuditFlowBiz;
	
	/*
	 * 查询设计施工or道路运输单位
	 * */
	public List<WorkingUnit> getList4Client(Integer unitId){
		String sql = null;
		List<WorkingUnit> list = null;
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("unitId", unitId);
		sql = " SELECT t2.id,t2.`name`,t2.short_name from workingunit_type t1,working_unit t2 \n" + 
				"where t1.unit_id = t2.id and (t1.type_code = '7' or t1.type_code = '10')\n" + 
				"and t2.id = :unitId";
		WorkingUnit unit = super.query(sql, paramMap);
		if(unit != null) {
			list = new ArrayList<>();
			list.add(unit);
		}else {
			sql = " SELECT t2.id,t2.`name`,t2.short_name from workingunit_type t1,working_unit t2 \n" + 
					"where t1.unit_id = t2.id and (t1.type_code = '7' or t1.type_code = '10')\n";
			list = super.queryList(sql, paramMap);
		}
		return list;
	}
	
	@Override
	public List<WorkingUnit> getList4Box(Integer auditStatus) {
		String sql = "select t.id,t.name,t.short_name from working_unit t  ";
		Map<String,Object> paramMap = new HashMap<>();
		if(auditStatus != null) {
			sql += " where t.audit_status = :auditStatus ";
			paramMap.put("auditStatus", auditStatus);
		}
		return super.queryList(sql,paramMap);
	}

	@Override
	public int save(WorkingUnit workingUnit, String uploadIds, AuditFlow auditFlow) {
		if(workingUnit.getId() == null) {
			super.addAndId(workingUnit);
			if(uploadIds != null) {
				iUploadResourceBiz.updateHostId(workingUnit.getId(),
						CommonUtils.str2IntList(uploadIds));
			}
		}else {
			super.update(workingUnit);
		}
		//审核状态更新
		auditFlow.setHostId(workingUnit.getId());
		auditFlow.setAuditTime(new Date());
		iAuditFlowBiz.addAndId(auditFlow);
		WorkingUnit workingUnit1 = new WorkingUnit();
		workingUnit1.setId(workingUnit.getId());
		workingUnit1.setAuditStatus(workingUnit.getAuditStatus());
		workingUnit1.setAuditFlowId(auditFlow.getId());
		super.update(workingUnit1);
		return 0;
	}
	
	@Override
	public int auditSave(WorkingUnit workingUnit, AuditFlow auditFlow) {
		auditFlow.setHostId(workingUnit.getId());
		auditFlow.setAuditTime(new Date());
		iAuditFlowBiz.addAndId(auditFlow);
		WorkingUnit workingUnit1 = new WorkingUnit();
		workingUnit1.setId(workingUnit.getId());
		workingUnit1.setAuditStatus(workingUnit.getAuditStatus());
		workingUnit1.setAuditFlowId(auditFlow.getId());
		return super.update(workingUnit1);
	}

	@Override
	public int saveWorkingUnit(WorkingUnit workingUnit,String unitTypes) {
		List<String> unitTypeCodes = CommonUtils.str2StrList(unitTypes);
		if(unitTypeCodes.isEmpty()) return 0;
		if(null == workingUnit.getId()) {
			super.addAndId(workingUnit);
		}else {
			super.update(workingUnit);
		}
		WorkinguintType workinguintType = new WorkinguintType();
		workinguintType.setUnitId(workingUnit.getId());
		iWorkinguintTypeBiz.delete(workinguintType);
		for(String code : unitTypeCodes) {
			workinguintType.setTypeCode(code);
			iWorkinguintTypeBiz.add(workinguintType);
		}
		return workingUnit.getId();
	}

}
