package cn.zhiyuan.ces.camera.sip;

/*
 * request/response参数
 * */
public class SipParams {

	
	private String method;
	private String fromTag;
	private String toTag;
	private long seq;
	private boolean subobj;
	
	//内容
	private String contentType;
	private String contentSubType;
	private Object content;
	/*
	 * 源
	 * */
	private String fromSipId;
	
	private String fromAddr;
	
	private String received;
	
	private int fromPort;
	private int rPort = -1;
	
	/*
	 * 目的
	 * */
	private String toSipId;
	
	private String toAddr;
	
	private int toPort;

	public String getFromAddr() {
		return fromAddr;
	}

	public String getFromAddrPort() {
		return fromAddr + ":" + fromPort;
	}
	
	public void setFromAddr(String fromAddr) {
		this.fromAddr = fromAddr;
	}

	
	public int getFromPort() {
		return fromPort;
	}

	public void setFromPort(int fromPort) {
		this.fromPort = fromPort;
	}

	public String getToAddr() {
		return toAddr;
	}

	public String getToAddrPort() {
		return toAddr + ":" + toPort;
	}
	
	public void setToAddr(String toAddr) {
		this.toAddr = toAddr;
	}

	public int getToPort() {
		return toPort;
	}

	public void setToPort(int toPort) {
		this.toPort = toPort;
	}

	public String getFromSipId() {
		return fromSipId;
	}

	public void setFromSipId(String fromSipId) {
		this.fromSipId = fromSipId;
	}

	public String getToSipId() {
		return toSipId;
	}

	public void setToSipId(String toSipId) {
		this.toSipId = toSipId;
	}

	public String getFromTag() {
		return fromTag;
	}

	public void setFromTag(String fromTag) {
		this.fromTag = fromTag;
	}

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentSubType() {
		return contentSubType;
	}

	public void setContentSubType(String contentSubType) {
		this.contentSubType = contentSubType;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public boolean isSubobj() {
		return subobj;
	}

	public void setSubobj(boolean subobj) {
		this.subobj = subobj;
	}

	public String getToTag() {
		return toTag;
	}

	public void setToTag(String toTag) {
		this.toTag = toTag;
	}

	public String getReceived() {
		return received;
	}

	public void setReceived(String received) {
		this.received = received;
	}

	public int getrPort() {
		return rPort;
	}

	public void setrPort(int rPort) {
		this.rPort = rPort;
	}
	
}
