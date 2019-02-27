package cn.zhiyuan.ces.contract.biz;

import java.util.List;

import cn.zhiyuan.ces.contract.entity.ExplosiveCardInfo;
import cn.zhiyuan.frame.IAbstractDao;


public interface IExplosiveCardInfoBiz extends IAbstractDao<ExplosiveCardInfo> {

	int updateHostId(Integer hostId, List<Integer> ids);

}
