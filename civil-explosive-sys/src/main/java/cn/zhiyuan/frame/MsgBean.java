package cn.zhiyuan.frame;

import com.alibaba.fastjson.annotation.JSONField;

//前台 后台 参数交互类
public class MsgBean {

	public MsgBean(){}
	
	public MsgBean(int id,String info){
		this.id = id;
		this.info = info;
	}
	
	private int id;
	
	private String info;

	private Object data;
	
	public int getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id == null?0:id;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public MsgBean setRetInfo(int i, String retmsg) {
		this.id = i;
		this.info = retmsg;
		return this;
	}


	@JSONField(serialize=false)
	public Object getObj() {
		return data;
	}


	public void setObj(Object obj) {
		this.data = obj;
	}

	@Override
	public String toString() {
		return "MsgBean [id=" + id + ", info=" + info + ", obj=" + data + "]";
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
