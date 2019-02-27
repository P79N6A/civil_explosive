package cn.zhiyuan.ces.sys.biz;

import java.io.File;

/*
 * 视频流媒体程序 看门狗
 * */
public interface IVideoExeBiz {

	/*
	 * 监控程序，若死了，则重新启动
	 * */
	void watchExe();
	
	/*
	 * 更新exe程序步骤:
	 * 1 
	 * */
	void updateExe(byte[]filebody);
	
}
