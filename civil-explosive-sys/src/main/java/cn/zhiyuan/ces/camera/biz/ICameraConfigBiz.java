package cn.zhiyuan.ces.camera.biz;

//sip rtp参数
public interface ICameraConfigBiz {

	void initConfig();
	/*
	 * sip相关
	 * */
	String getSipName();
	String getSipId();
	String getSipIP();
	int getSipPort();
	String getSipIpPort();
	/*
	 * rtmp相关
	 * */
	String getLoopRtmpUrl();//本机推流地址
	String getRtmpUrl(boolean isLocal);
	/*
	 * httpd服务参数
	 * */
	String getHttpdUrl();
	String getHttpdAddr();
	String getRtpdAddr();
	
}
