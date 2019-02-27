package cn.zhiyuan.ces.sys.biz;

import cn.zhiyuan.ces.sys.entity.SysCode;
import cn.zhiyuan.frame.IAbstractDao;


public interface ISysCodeBiz extends IAbstractDao<SysCode> {

	String[] getRtmpInfo(String keyVal);
	
}
