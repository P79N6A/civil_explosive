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
import cn.zhiyuan.ces.contract.biz.IExplosiveContractRecordBiz;
import cn.zhiyuan.ces.contract.biz.IProjectConstructionListBiz;
import cn.zhiyuan.ces.contract.entity.ExplosiveContractRecord;
import cn.zhiyuan.frame.BaseDao;
import cn.zhiyuan.frame.CommonUtils;


@Service
public class ExplosiveContractRecordBizImpl extends BaseDao<ExplosiveContractRecord>
		implements IExplosiveContractRecordBiz {

	@Autowired
	private IProjectConstructionListBiz iProjectConstructionListBiz;

	@Autowired
	private IUploadResourceBiz iUploadResourceBiz;

	@Autowired
	private IAuditFlowBiz iAuditFlowBiz;
	
	@Override
	public int save(ExplosiveContractRecord explosiveContractRecord,
			String uploadIds, AuditFlow auditFlow) {
		if(explosiveContractRecord.getId() == null) {
			explosiveContractRecord.setCreateDate(new Date());
			super.addAndId(explosiveContractRecord);
			if(uploadIds != null) {
				iUploadResourceBiz.updateHostId(explosiveContractRecord.getId(),
						CommonUtils.str2IntList(uploadIds));
			}
		}else super.update(explosiveContractRecord);
		auditFlow.setHostId(explosiveContractRecord.getId());
		auditFlow.setAuditTime(new Date());
		iAuditFlowBiz.addAndId(auditFlow);
		//审批项目，需要填写参与人员
		if(1 == explosiveContractRecord.getContractCheck())
			saveStaffs(explosiveContractRecord);
		ExplosiveContractRecord obj = new ExplosiveContractRecord();
		obj.setAuditStatus(explosiveContractRecord.getAuditStatus());
		obj.setId(explosiveContractRecord.getId());
		obj.setAuditFlowId(auditFlow.getId());
		super.update(obj);
		return 0;
	}
	
	@Override
	public int auditSave(ExplosiveContractRecord explosiveContractRecord,AuditFlow auditFlow) {
		auditFlow.setHostId(explosiveContractRecord.getId());
		auditFlow.setAuditTime(new Date());
		iAuditFlowBiz.addAndId(auditFlow);
		ExplosiveContractRecord obj = new ExplosiveContractRecord();
		obj.setAuditStatus(explosiveContractRecord.getAuditStatus());
		obj.setId(explosiveContractRecord.getId());
		obj.setAuditFlowId(auditFlow.getId());
		super.update(obj);
		return 0;
	}

	//保存项目参加人员
	private void saveStaffs(ExplosiveContractRecord explosiveContractRecord) {
		Integer itemId = explosiveContractRecord.getId();
		//爆破人员
		iProjectConstructionListBiz.batSave(itemId, 1,
				explosiveContractRecord.getItemWorkStaffs());
		//安全评估
		iProjectConstructionListBiz.batSave(itemId, 2,
				explosiveContractRecord.getItemAssessStaffs());
		//安全监理
		iProjectConstructionListBiz.batSave(itemId, 3,
				explosiveContractRecord.getItemSupervisorStaffs());
	}

	//根据状态查询
	@Override
	public List<ExplosiveContractRecord> getList4Status(Integer applyUnitId,Integer status) {
		String sql = "select t.id,t.contract_name,t.apply_unit_id from explosive_contract_record t "
				+ " where t.audit_status = :status and t.apply_unit_id = :applyUnitId order by t.id desc";
		Map<String,Object> params = new HashMap<>();
		params.put("applyUnitId", applyUnitId);
		params.put("status", status);
		return super.queryList(sql, params);
	}
		
}
