package cn.zhiyuan.ces.camera.biz.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zhiyuan.ces.camera.biz.ICameraConfigBiz;
import cn.zhiyuan.ces.sys.biz.ISysCodeBiz;
import cn.zhiyuan.ces.sys.entity.SysCode;

/*参数配置*/
@Service
public class CameraConfigBizImpl implements ICameraConfigBiz {

	private static Log log = LogFactory.getLog(CameraConfigBizImpl.class);   
	
	@Autowired
	private ISysCodeBiz iSysCodeBiz;
	
	private SysCode sipCode;
	private SysCode rtmpCode;
	private SysCode httpdCode;
	
	@Override
	public void initConfig() {
		log.debug("初始化系统参数.");
		SysCode code = new SysCode();
		code.setCodeType("i");
		List<SysCode> codeList = iSysCodeBiz.getList(code, "code_id asc");
		for(SysCode syscode : codeList) {
			switch (syscode.getCodeValue()) {
			case "1":
				sipCode = syscode;
				break;
			case "2":
				httpdCode = syscode;
				break;
			case "3":
				rtmpCode = syscode;
				break;
			default:
				break;
			}
		}
	}
	
	/*
	 * sip服务相关参数
	 * */
	@Override
	public String getSipName() {
		return sipCode.getCodeDesc1();
	}

	@Override
	public String getSipId() {
		return sipCode.getCodeDesc2();
	}

	@Override
	public String getSipIP() {
		return sipCode.getCodeDesc3();
	}

	@Override
	public int getSipPort() {
		return Integer.parseInt(sipCode.getCodeDesc4());
	}

	@Override
	public String getSipIpPort() {
		return getSipIP() + ":" + getSipIpPort();
	}

	/*
	 * rtmpd相关参数
	 * */
	@Override
	public String getLoopRtmpUrl() {
		return rtmpCode.getCodeDesc1();
	}

	@Override
	public String getRtmpUrl(boolean isLocal) {
		return isLocal?rtmpCode.getCodeDesc2():rtmpCode.getCodeDesc3();
	}

	/*
	 * httpd服务参数
	 * */
	@Override
	public String getHttpdUrl() {
		return "http://" + httpdCode.getCodeDesc1()
				+ ":" + httpdCode.getCodeDesc2();
	}
	
	@Override
	public String getHttpdAddr() {
		return httpdCode.getCodeDesc1();
	}

	@Override
	public String getRtpdAddr() {
		return httpdCode.getCodeDesc3();
	}
	

}
