package cn.zhiyuan.ces.sys.biz.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zhiyuan.ces.sys.biz.ISysCodeBiz;
import cn.zhiyuan.ces.sys.biz.IVideoExeBiz;
import cn.zhiyuan.ces.sys.entity.SysCode;

/*
 * 视频流媒体看门狗
 * */
@Service
public class VideoExeBizImpl implements IVideoExeBiz {
	
	private static  Log log = LogFactory.getLog(VideoExeBizImpl.class);
	
	@Autowired
	private ISysCodeBiz iSysCodeBiz;
	
	private Process process;
	
	@Override
	synchronized public void watchExe() {
		boolean islive = isLive();
		if(!islive) {
			log.error("视频流程序死了!");
			SysCode code = getExeConfig();
			if(code != null) startExe(code);
		}
	}
	
	@PreDestroy
	private void over() {
		if(isLive()) process.destroy();
	}
	
	@Override
	synchronized public void updateExe(byte[]filebody) {
		SysCode code = getExeConfig();
		if(code == null) return;
		if(isLive()) {
			process.destroyForcibly();//关闭程序
		}
		try {
			Thread.sleep(2 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		File file = new  File(code.getCodeDesc1());
		try {
			file.delete();
			file.createNewFile();
			FileOutputStream fout = new FileOutputStream(file);
			fout.write(filebody);
			fout.close();
			startExe(code);//重新启动
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startExe(SysCode code) {
		Runtime runtime = Runtime.getRuntime();
		try {
			File file = new File(code.getCodeDesc1());
			if(!file.exists()) return;
			process = runtime.exec(code.getCodeDesc1(),null,file.getParentFile());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private boolean isLive() {
		return process == null || !process.isAlive()?false:true;
	}
	
	//读取程序路径配置
	private SysCode getExeConfig() {
		SysCode code = new SysCode();
		code.setCodeType("j");
		code.setCodeValue("3");
		return iSysCodeBiz.get(code);
	}
	
}
