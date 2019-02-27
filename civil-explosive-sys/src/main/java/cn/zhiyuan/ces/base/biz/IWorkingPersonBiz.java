package cn.zhiyuan.ces.base.biz;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.zhiyuan.ces.base.entity.AuditFlow;
import cn.zhiyuan.ces.base.entity.WorkingPerson;
import cn.zhiyuan.frame.IAbstractDao;
import cn.zhiyuan.frame.PageBean;


public interface IWorkingPersonBiz extends IAbstractDao<WorkingPerson> {

	//条件查询列表
	List<WorkingPerson> getWorkingPersonList(PageBean<WorkingPerson> pb,WorkingPerson person,String q);
	
	//获取combobox列表数据
	List<WorkingPerson> getWorkingPersonBoxList(Integer unitId);
	//备案
	@Transactional
	int save(WorkingPerson workingPerson,String uploadIds
			,AuditFlow auditFlow);
	//审核保存
	@Transactional
	int auditSave(WorkingPerson workingPerson,AuditFlow auditFlow);
	
	//类型分组
	List<JSONObject> typeCount(int unitId);
	
}
