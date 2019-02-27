package cn.zhiyuan.frame.orm.builder;

import java.util.Map;

/*封装where条件*/
public interface IWhereSql {

	void where(StringBuilder sb,Map<String,Object> params);
	
}
