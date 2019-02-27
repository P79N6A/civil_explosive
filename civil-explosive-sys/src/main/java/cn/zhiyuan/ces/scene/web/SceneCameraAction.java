package cn.zhiyuan.ces.scene.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.zhiyuan.ces.scene.biz.ISceneCameraBiz;
import cn.zhiyuan.ces.scene.entity.SceneCamera;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.MsgBean;


@Controller
@RequestMapping("/scene")
public final class SceneCameraAction extends BaseAction{

	@Autowired
	private ISceneCameraBiz iSceneCameraBiz;
	
	@RequestMapping("/scenecamera/jsons")
	public ModelAndView jsons(MsgBean mbean,SceneCamera sceneCamera) {
		mbean.setObj(iSceneCameraBiz.getList(sceneCamera, "t.plat_type asc"));
		return json(mbean);
	}
	
	@RequestMapping("/scenecamera/online")
	public ModelAndView online(MsgBean mbean,SceneCamera sceneCamera) {
		mbean.setObj(iSceneCameraBiz.getOnlineList());
		return json(mbean);
	}

	@RequestMapping("/scenecamera/regAll") 
	public ModelAndView regAll(MsgBean mbean) {
		iSceneCameraBiz.regDevices();
		return json(mbean);
	}
	
	//创建sip id序号
	@RequestMapping("/scenecamera/createId") 
	public ModelAndView createSipId(MsgBean mbean) {
		mbean.setData(iSceneCameraBiz.createSipId());
		return json(mbean);
	}
	
	@RequestMapping("/scenecamera/playVideo") 
	public ModelAndView json(HttpServletRequest request,MsgBean mbean,SceneCamera sceneCamera,String cameraId) {
		String ip = request.getRemoteAddr();
		boolean isLocal = ip.indexOf("192.168.") != -1?true:false;
		if(StringUtils.isEmpty(cameraId)) cameraId = "1";
		mbean.setData(iSceneCameraBiz.playVideo(sceneCamera,cameraId,isLocal));
		return json(mbean);
	}
	
	/*更改配置*/
	@RequestMapping("/scenecamera/save") 
	public ModelAndView save(MsgBean mbean,SceneCamera sceneCamera) {
		mbean.setInfo("保存成功");
		iSceneCameraBiz.save(sceneCamera);
		return json(mbean);
	}

	@RequestMapping("/scenecamera/delete") 
	public ModelAndView delete(MsgBean mbean,SceneCamera sceneCamera) {
		mbean.setInfo("删除成功");
		iSceneCameraBiz.delete(sceneCamera);
		return json(mbean);
	}
	
}
