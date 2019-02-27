package cn.zhiyuan.ces.contract.biz;

import cn.zhiyuan.ces.contract.entity.ProjectConstructionList;
import cn.zhiyuan.frame.IAbstractDao;


public interface IProjectConstructionListBiz extends IAbstractDao<ProjectConstructionList> {

	//批量保存
	int batSave(Integer itemId,Integer unitType,String staffs);
	
}
