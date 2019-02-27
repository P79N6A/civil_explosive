package cn.zhiyuan.ces.contract.biz.impl;

import cn.zhiyuan.ces.contract.entity.ExplosiveCardInfo;
import cn.zhiyuan.ces.contract.biz.IExplosiveCardInfoBiz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import cn.zhiyuan.frame.BaseDao;


@Service
public class ExplosiveCardInfoBizImpl extends BaseDao<ExplosiveCardInfo> implements IExplosiveCardInfoBiz {

	@Override
	public int updateHostId(Integer hostId, List<Integer> ids) {
		if(ids.isEmpty()) return 0;
		String sql = "update explosive_card_info set host_id = :hostId where id in (:ids) ";
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("ids",ids);
		paramMap.put("hostId",hostId);
		return super.update(sql, paramMap);
	}

}
