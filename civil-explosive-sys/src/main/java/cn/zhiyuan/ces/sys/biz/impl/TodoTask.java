package cn.zhiyuan.ces.sys.biz.impl;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.zhiyuan.ces.sys.biz.ITodoTask;
import cn.zhiyuan.ces.sys.entity.SysUser;
import cn.zhiyuan.frame.MsgBean;

@Service
public class TodoTask implements ITodoTask {

	@Override
	public JSONArray todoTask(MsgBean mbean, SysUser user) {
		JSONArray jsons = new JSONArray();
		JSONObject json = null;
		
		json = new JSONObject();
		json.put("taskName", "从业单位受理");
		json.put("taskType", 2);
		json.put("taskCount", 3);
		jsons.add(json);
		
		json = new JSONObject();
		json.put("taskName", "从业人员受理");
		json.put("taskType", 2);
		json.put("taskCount", 2);
		jsons.add(json);
		
		mbean.setObj(jsons);
		return jsons;
	}

	@Override
	public JSONArray pastDueTask(MsgBean mbean, SysUser user) {
		JSONArray jsons = new JSONArray();
		mbean.setObj(jsons);
		return jsons;
	}

}
