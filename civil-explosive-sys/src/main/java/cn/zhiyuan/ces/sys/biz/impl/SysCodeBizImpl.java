package cn.zhiyuan.ces.sys.biz.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.zhiyuan.ces.sys.biz.ISysCodeBiz;
import cn.zhiyuan.ces.sys.entity.SysCode;
import cn.zhiyuan.frame.BaseDao;

@Service
public class SysCodeBizImpl extends BaseDao<SysCode> implements ISysCodeBiz {

	//获取路径及上下文
	public String[] getRtmpInfo(String keyVal) {
		String videoRoot = "/home/wen/tmp";
		String appName = "scene";
		SysCode code = new SysCode();
		code.setCodeType("k");
		code.setCodeDesc4(keyVal);
		code = super.get(code);
		if(code != null) {
			appName = code.getCodeDesc1();
			videoRoot = code.getCodeDesc2();
		}
		if(StringUtils.isEmpty(appName)) appName = "scene";
		if(StringUtils.isEmpty(videoRoot)) videoRoot = "sceneRoot";
		return new String[] {appName,videoRoot};
	}
	
	
}
