package cn.zhiyuan.ces.base.biz.impl;

import cn.zhiyuan.ces.base.entity.UploadResource;
import cn.zhiyuan.ces.base.biz.IUploadResourceBiz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import cn.zhiyuan.frame.BaseDao;


@Service
public class UploadResourceBizImpl extends BaseDao<UploadResource> implements IUploadResourceBiz {

	//查询宿主资源列表
	@Override
	public List<UploadResource> getList(Integer hostGenre, Integer hostId) {
		String sql = "SELECT t1.name,t2.path,t2.id,t1.id as host_genre"
					+ ",:hostId as host_id,t2.file_type "
				+ " from upload_genre t1 left JOIN upload_resource t2 "
				+ " on t1.id = t2.host_genre and t2.host_id = :hostId where t1.type_code = :hostGenre ";
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("hostGenre",hostGenre);
		paramMap.put("hostId",hostId);
		return super.queryList(sql,paramMap);
	}

	@Override
	public int updateHostId(Integer hostId, List<Integer> ids) {
		if(ids.isEmpty()) return 0;
		String sql = "update upload_resource set host_id = :hostId where id in (:ids) ";
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("ids",ids);
		paramMap.put("hostId",hostId);
		return super.update(sql, paramMap);
	}

}
