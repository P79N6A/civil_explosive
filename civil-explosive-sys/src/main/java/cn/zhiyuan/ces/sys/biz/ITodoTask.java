package cn.zhiyuan.ces.sys.biz;

import com.alibaba.fastjson.JSONArray;

import cn.zhiyuan.ces.sys.entity.SysUser;
import cn.zhiyuan.frame.MsgBean;

/*
 * 待办，提醒任务
 * */
public interface ITodoTask {
	/*
	 * 待办任务
	 * */
	JSONArray todoTask(MsgBean mbean, SysUser user);
	
	/*
	 * 过期任务
	 * */
	JSONArray pastDueTask(MsgBean mbean, SysUser user);
	
}
