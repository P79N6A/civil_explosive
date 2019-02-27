package cn.zhiyuan.ces.contract.biz.impl;

import cn.zhiyuan.ces.contract.entity.ProjectConstructionList;
import cn.zhiyuan.ces.contract.biz.IProjectConstructionListBiz;

import java.util.List;

import org.springframework.stereotype.Service;
import cn.zhiyuan.frame.BaseDao;
import cn.zhiyuan.frame.CommonUtils;


@Service
public class ProjectConstructionListBizImpl extends BaseDao<ProjectConstructionList> implements IProjectConstructionListBiz {

	//批量添加
	@Override
	public int batSave(Integer itemId, Integer unitType, String staffs) {
		List<Integer> staffList = CommonUtils.str2IntList(staffs);
		if(itemId == null || staffList.isEmpty()) return 0;
		//清空
		ProjectConstructionList projectConstructionList = new ProjectConstructionList();
		projectConstructionList.setItemId(itemId);
		projectConstructionList.setUnitType(unitType);
		super.delete(projectConstructionList);
		for(Integer id :staffList) {
			projectConstructionList.setStaffId(id);
			super.add(projectConstructionList);
		}
		return 1;
	}

}
