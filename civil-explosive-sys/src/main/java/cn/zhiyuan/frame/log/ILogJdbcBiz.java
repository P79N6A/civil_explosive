package cn.zhiyuan.frame.log;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.zhiyuan.frame.PageBean;

public interface ILogJdbcBiz {

	List<JSONObject> getLogList(PageBean<JSONObject>pb,Date kssj,Date jssj,String thread);
	
}
