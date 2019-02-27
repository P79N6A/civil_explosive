package cn.zhiyuan.ces.base.biz.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zhiyuan.ces.base.biz.IVideoRecordBiz;
import cn.zhiyuan.ces.base.entity.VideoRecord;
import cn.zhiyuan.ces.scene.biz.ISceneCameraBiz;
import cn.zhiyuan.ces.scene.entity.SceneCamera;
import cn.zhiyuan.frame.BaseDao;


@Service
public class VideoRecordBizImpl extends BaseDao<VideoRecord> implements IVideoRecordBiz {

	@Autowired
	private ISceneCameraBiz iSceneCameraBiz;
	
	public void saveVideoRecord(String deviceId,String appName,String path,Date kssj,Date jssj,long fileSize) {
		//log.debug("--------" + path);
		islog = false;
		SceneCamera sceneCamera = new SceneCamera();
		sceneCamera.setSipId(deviceId);
		sceneCamera = iSceneCameraBiz.get(sceneCamera);
		if(sceneCamera == null) return;
		VideoRecord record = new VideoRecord();
		record.setVideoPath2(path);
		record.setVideoPath1(appName);
		record = super.get(record);
		if(record == null) {
			record = new VideoRecord();
			record.setUnitId(sceneCamera.getUnitId());
			record.setVideoPath1(appName);
			record.setVideoPath2(path);
			record.setStartTime(kssj);
			record.setEndTime(jssj);
			record.setVideoSize(fileSize);
			record.setDeviceName(sceneCamera.getCameraName());
			super.add(record);
		}else {
			VideoRecord record1 = new VideoRecord();
			record1.setId(record.getId());
			record1.setEndTime(jssj);
			record1.setVideoSize(fileSize);
			super.update(record1);//only upate endtime
		}
		
	}
}
