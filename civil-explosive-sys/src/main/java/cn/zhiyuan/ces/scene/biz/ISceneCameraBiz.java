package cn.zhiyuan.ces.scene.biz;

import java.util.List;

import com.alibaba.fastjson.JSONArray;

import cn.zhiyuan.ces.scene.entity.SceneCamera;
import cn.zhiyuan.frame.IAbstractDao;


public interface ISceneCameraBiz extends IAbstractDao<SceneCamera> {

	//注册设备
	void regDevices();
	//保存视频
	void saveVideo();
	
	List<SceneCamera> getOnlineList();
	
	SceneCamera playVideo(SceneCamera camera,String cameraId,boolean local);
	
	String createSipId();
	
	JSONArray getList4Client(Integer unitId);
	
}
