package cn.zhiyuan.ces.sys.biz;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public interface ILogParseBiz {

	List<JSONObject>  logRead(String logPath,String logName,Integer logTime);
	
}
