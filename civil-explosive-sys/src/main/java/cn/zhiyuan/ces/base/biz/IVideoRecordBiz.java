package cn.zhiyuan.ces.base.biz;

import java.util.Date;

import cn.zhiyuan.ces.base.entity.VideoRecord;
import cn.zhiyuan.ces.scene.entity.SceneCamera;
import cn.zhiyuan.frame.IAbstractDao;


public interface IVideoRecordBiz extends IAbstractDao<VideoRecord> {

	void saveVideoRecord(String deviceId,String appName,String path,Date kssj,Date jssj,long fileSize);
	
}
