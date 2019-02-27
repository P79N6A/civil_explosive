package cn.zhiyuan.ces.sys.web;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import cn.zhiyuan.ces.sys.biz.IVideoExeBiz;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.MsgBean;

@Controller
@RequestMapping("/sys")
public class VideoExeAction extends BaseAction {

	@Autowired
	private IVideoExeBiz iVideoExeBiz;
	
	@RequestMapping("/videoexe/updateFile") 
	public ModelAndView uploadFile(MsgBean mbean,@RequestParam MultipartFile exefile) {
		try {
			iVideoExeBiz.updateExe(exefile.getBytes());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return html(JSON.toJSONString(mbean));
	}
	
}
