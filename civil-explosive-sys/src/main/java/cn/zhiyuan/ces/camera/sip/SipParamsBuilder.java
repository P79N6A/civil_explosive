package cn.zhiyuan.ces.camera.sip;

/*
 * SipParams实例构建者
 * */
public class SipParamsBuilder {
	
	private SipParams params;
	
	public SipParamsBuilder(String method,long seq,boolean hasSubobj) {
		params = new SipParams();
		params.setMethod(method);
		params.setSubobj(hasSubobj);
		params.setSeq(seq);
	}
	
	//设置源参数
	public SipParamsBuilder setTag(String fromTag,String toTag) {
		params.setFromTag(fromTag);
		params.setToTag(toTag);
		return this;
	}
		
	//设置源参数
	public SipParamsBuilder setFromParams(String id,String addr,int port) {
		params.setFromAddr(addr);
		params.setFromSipId(id);
		params.setFromPort(port);
		return this;
	}
	//设置目的参数
	public SipParamsBuilder setToParams(String id,String addr,int port) {
		params.setToAddr(addr);
		params.setToSipId(id);
		params.setToPort(port);
		return this;
	}
	
	public SipParamsBuilder setContent(String type,String subType,Object content) {
		params.setContentType(type);
		params.setContentSubType(subType);
		params.setContent(content);
		return this;
	}
	
	public SipParams builder() {
		return params;
	}
}
