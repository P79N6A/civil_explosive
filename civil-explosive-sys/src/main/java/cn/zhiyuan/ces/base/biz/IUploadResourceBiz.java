package cn.zhiyuan.ces.base.biz;

import java.util.List;

import cn.zhiyuan.ces.base.entity.UploadResource;
import cn.zhiyuan.frame.IAbstractDao;


public interface IUploadResourceBiz extends IAbstractDao<UploadResource> {

	List<UploadResource> getList(Integer hostGenre,Integer hostId);
	
	//更新资源hostid
	int updateHostId(Integer hostId,List<Integer> ids);
	
}
