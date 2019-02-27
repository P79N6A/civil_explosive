package cn.zhiyuan.ces.base.biz.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.zhiyuan.ces.base.biz.IAuditFlowBiz;
import cn.zhiyuan.ces.base.biz.IUploadResourceBiz;
import cn.zhiyuan.ces.base.biz.IWorkingPersonBiz;
import cn.zhiyuan.ces.base.entity.AuditFlow;
import cn.zhiyuan.ces.base.entity.WorkingPerson;
import cn.zhiyuan.frame.BaseDao;
import cn.zhiyuan.frame.CommonUtils;
import cn.zhiyuan.frame.PageBean;
import cn.zhiyuan.frame.orm.builder.IWhereSql;


@Service
public class WorkingPersonBizImpl extends BaseDao<WorkingPerson> implements IWorkingPersonBiz {

	@Autowired
	private IUploadResourceBiz iUploadResourceBiz;

	@Autowired
	private IAuditFlowBiz iAuditFlowBiz;
	
	//条件查询列表
	@Override
	public List<WorkingPerson> getWorkingPersonList(PageBean<WorkingPerson> pb
			,WorkingPerson person,final String q){
		return super.getPageList(pb, person, "t.id asc",new IWhereSql() {
			@Override
			public void where(StringBuilder sb, Map<String, Object> params) {
				if(StringUtils.isNotEmpty(q)) {
					sb.append(" and t.name like :q ");
					params.put("q","%" + q + "%");
				}
			}
		});
	}
	
	//条件查询列表
	public List<WorkingPerson> getWorkingPersonList1(PageBean<WorkingPerson> pb,WorkingPerson person,String q){
		String sql = "select t.* "
				+ ", (select tt.code_name from sys_code tt  "
					+ " where tt.code_type = '6' and tt.code_value= t.tech_level) as tech_level_name "
				+ ", (select tt.code_name from sys_code tt  "
					+ " where tt.code_type = '5' and tt.code_value= t.licence_type) as licence_type_name "
				+ ", (select tt.code_name from sys_code tt  "
					+ " where tt.code_type = '7' and tt.code_value= t.status) as status_name "
				+ " from working_person t "
				+ " where 1 = 1 ";
		if(person.getUnitId() != null)
			sql += " and t.unit_id = :unitId ";
		if(StringUtils.isNotEmpty(person.getLicenceType()))
			sql += " and t.licence_type = :licenceType ";
		if(StringUtils.isNotEmpty(q))
			sql += " and t.name like :q ";
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("unitId",person.getUnitId());
		paramMap.put("licenceType",person.getLicenceType());
		paramMap.put("q","%" + q + "%");
		return super.queryPageList(pb,sql,paramMap,"t.id asc");
	}
	
	
		
	@Override
	public List<WorkingPerson> getWorkingPersonBoxList(Integer unitId) {
		String sql = "  SELECT t.id,t.name from working_person t "
				+ "where t.audit_status = 3 and t.unit_id = :unitId ";
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("unitId",unitId);
		return super.queryList(sql,paramMap);
	}

	//备案
	@Override
	public int save(WorkingPerson workingPerson, String uploadIds,AuditFlow auditFlow) {
		if(workingPerson.getId() == null) {
			workingPerson.setCreateDate(new Date());
			super.addAndId(workingPerson);
			if(uploadIds != null) {
				iUploadResourceBiz.updateHostId(workingPerson.getId(),
						CommonUtils.str2IntList(uploadIds));
			}
		}else {
			super.update(workingPerson);
		}
		auditFlow.setHostId(workingPerson.getId());
		auditFlow.setAuditTime(new Date());
		iAuditFlowBiz.addAndId(auditFlow);
		//更新流程id
		WorkingPerson workingPerson1 = new WorkingPerson();
		workingPerson1.setId(workingPerson.getId());
		workingPerson1.setAuditStatus(workingPerson.getAuditStatus());
		workingPerson1.setAuditFlowId(auditFlow.getId());
		return super.update(workingPerson1);
	}
	
	@Override
	public int auditSave(WorkingPerson workingPerson,AuditFlow auditFlow) {
		auditFlow.setHostId(workingPerson.getId());
		auditFlow.setAuditTime(new Date());
		iAuditFlowBiz.addAndId(auditFlow);
		//更新流程id
		WorkingPerson workingPerson1 = new WorkingPerson();
		workingPerson1.setId(workingPerson.getId());
		workingPerson1.setAuditStatus(workingPerson.getAuditStatus());
		workingPerson1.setAuditFlowId(auditFlow.getId());
		super.update(workingPerson1);
		return 0;
	}

	@Override
	public List<JSONObject> typeCount(int unitId) {
		//and t1.audit_status = '3'
		String sql = " select t2.code_value as licence_type_value, t2.code_name as licence_type_name ,count(t1.id) as licence_type_count from working_person t1,sys_code t2 " + 
				" where t2.code_type = '5' and t1.licence_type = t2.code_value and t1.unit_id = :unitId  " + 
				" GROUP BY t1.licence_type ";
		JSONObject paramMap = new JSONObject();
		paramMap.put("unitId",unitId);
		return super.queryJsons(sql, paramMap);
	}

}
