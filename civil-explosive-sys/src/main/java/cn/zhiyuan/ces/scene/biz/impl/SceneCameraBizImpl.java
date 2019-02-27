package cn.zhiyuan.ces.scene.biz.impl;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;

import cn.zhiyuan.ces.base.biz.IVideoRecordBiz;
import cn.zhiyuan.ces.camera.biz.ICameraBiz;
import cn.zhiyuan.ces.camera.biz.ICameraConfigBiz;
import cn.zhiyuan.ces.camera.biz.impl.CameraUitls;
import cn.zhiyuan.ces.camera.entity.CameraBean;
import cn.zhiyuan.ces.scene.biz.ISceneCameraBiz;
import cn.zhiyuan.ces.scene.entity.SceneCamera;
import cn.zhiyuan.ces.sys.biz.ISysCodeBiz;
import cn.zhiyuan.frame.BaseDao;
import cn.zhiyuan.frame.CommonUtils;


@Service
public class SceneCameraBizImpl extends BaseDao<SceneCamera> implements ISceneCameraBiz{

	@Autowired
	private ICameraBiz cameraBiz;
	
	@Autowired
	private ISysCodeBiz iSysCodeBiz;
	
	@Autowired
	private IVideoRecordBiz iVideoRecordBiz;
	
	@Autowired
	protected ICameraConfigBiz configBiz;
	
	public JSONArray getList4Client(Integer unitId){
		return null;
	}
	
	public String createSipId() {
		String sipId = "34020000001320000607";
		String sql = "select * from scene_camera t where t.plat_type = 1 order by t.id desc limit 1";
		SceneCamera sceneCamera = super.query(sql, null);
		if(sceneCamera == null) return sipId;
		String id = sceneCamera.getSipId();
		String id1 = id.substring(0, 17);
		String id2 = id.substring(17);
		sipId = String.format("%s%03d", id1,Integer.parseInt(id2) + 1);
		return sipId;
	}
	
	public List<SceneCamera> getOnlineList(){
		List<SceneCamera> sceneList = super.getList(null,null);
		for(SceneCamera camera : sceneList) {//获取设备在线状态
			CameraBean cb = new CameraBean();
			cb.setDeviceId(camera.getSipId());
			cb.setPlatType(camera.getPlatType());
			cb.setCameraId("1");
			camera.setOnline(cameraBiz.queryStatus(cb));
		}
		return sceneList;
	}
	
	public SceneCamera playVideo(SceneCamera camera,String cameraId,boolean local) {
		camera = super.get(camera);
		if(camera == null) return null;
		camera.setRtmpUrl(configBiz.getRtmpUrl(local) + camera.getRtmpdCtx());
		CameraBean cb = new CameraBean();
		cb.setDeviceId(camera.getSipId());
		cb.setCameraId(cameraId);
		cb.setStreamType(camera.getStreamType());
		camera.setPlayPath(CameraUitls.createPlayPath(cb));
		return camera;
	}
	
	/*
	 * 保存视频
	 * 获取上下文根目录
	 * 生成视频目录
	 * 遍历目录中文件
	 * 若视频没入库，则保存入库
	 * */
	public void saveVideo() {
		String []names = iSysCodeBiz.getRtmpInfo("2");
		String appName = names[0];
		String rootPath = names[1];
		String path = CommonUtils.date2str(new Date(), "yyyy/MM/dd/");
		//log.debug("遍历目录:" + rootPath + path);
		File videoDir = new File(rootPath + path);
		File[] videoFiles = videoDir.listFiles();
		if(videoFiles == null) return;
		Arrays.sort(videoFiles, new Comparator<File>(){
			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		for(File file : videoFiles) {
			Date createTime = CommonUtils.getFileCreateTime(file.getAbsolutePath());
			String name = file.getName();
			//log.debug(name + "=======");
			String []strs = null;
			strs = name.split("\\.");
			if(".mp4".equals(strs[1]) && strs.length == 2) continue;
			if(".flv".equals(strs[1]) && strs.length == 2) continue;
			String name1 = strs[0];
			strs = name.split("_");
			String deviceId = strs[0];
			String path1 = path + name1;
			//log.debug(path1 + ",," + deviceId);
			SceneCamera sceneCamera = new SceneCamera();
			sceneCamera.setSipId(deviceId);
			super.islog = false;
			sceneCamera = super.get(sceneCamera);
			if(sceneCamera == null) continue;
			//iVideoRecordBiz.saveVideoRecord(sceneCamera, appName, path1, createTime,
					//new Date(file.lastModified()), file.length());
		}
	}
		
	@Override
	public void regDevices() {
		String[] names = iSysCodeBiz.getRtmpInfo("2");
		List<SceneCamera> cameraList = super.getList(null,"t.id asc");
		for(SceneCamera camera : cameraList) {
			//log.debug("==plat:" + camera.getPlatType());
			CameraBean cb = new CameraBean();
			cb.setDeviceId(camera.getSipId());
			//cb.setCameraId(camera.getCameraCode());
			cb.setAutoSave(camera.getAutoSave() == 1);
			cb.setPlatType(camera.getPlatType());
			cb.setAudioCode(camera.getAudioCode());
			cb.setAppName(names[0]);
			Integer st = camera.getStreamType();
			if(st == null) st = 1;
			cb.setStreamType(st);
			cameraBiz.regDevice(cb, 1);
		}
	}
	
}
