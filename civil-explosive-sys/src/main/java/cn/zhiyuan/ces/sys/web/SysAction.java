package cn.zhiyuan.ces.sys.web;

import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.zhiyuan.ces.camera.biz.ICameraBiz;
import cn.zhiyuan.ces.camera.biz.ICameraConfigBiz;
import cn.zhiyuan.ces.camera.biz.impl.CameraUitls;
import cn.zhiyuan.ces.scene.biz.ISceneCameraBiz;
import cn.zhiyuan.ces.transport.biz.ICarInfoBiz;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.CommonUtils;


@Controller
public final class SysAction extends BaseAction{

	@Autowired
	private ICarInfoBiz iCarInfoBiz;
	
	@Autowired
	private ISceneCameraBiz iSceneCameraBiz;
	
	@Autowired
	private ICameraConfigBiz cameraConfigBiz;
	
	@Autowired
	private ICameraBiz cameraBiz;
	
	private Timer videoSaveTimer;
	
	private void startTask() {
		videoSaveTimer = new Timer();
		videoSaveTimer.schedule(new TimerTask() {
			public void run() {
				try {
					log.debug("状态同步");
					cameraBiz.onTimer();
				} catch (Exception e) {
					log.error(CommonUtils.log4stack(e));
				}
			}
		}, 600, CameraUitls.DEVICE_TIMEOUT);
	}
	
	/*
	 * 系统启动初始化入口
	 * */
	@PostConstruct
	private void sysInit() {
		log.debug("系统初始化...");
		cameraConfigBiz.initConfig();
		cameraBiz.init();
		iSceneCameraBiz.regDevices();
		//iCarInfoBiz.regDevices();
		startTask();
	}

	@PreDestroy
	private void over() {
		log.debug("系统退出中...");
		videoSaveTimer.cancel();
	}
	
}
