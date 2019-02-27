package cn.zhiyuan.ces.base.web;

import cn.zhiyuan.ces.base.biz.IUploadGenreBiz;
import cn.zhiyuan.ces.base.entity.UploadGenre;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import cn.zhiyuan.frame.*;


@Controller
@RequestMapping("/base")
public final class UploadGenreAction extends BaseAction{

	@Autowired
	private IUploadGenreBiz iUploadGenreBiz;

	@RequestMapping("/uploadgenre/jsons") 
	public ModelAndView json(PageBean<UploadGenre> pb,UploadGenre uploadGenre) {
		iUploadGenreBiz.getPageList(pb,uploadGenre,null);
		return json(pb);
	}

	@RequestMapping("/uploadgenre/save") 
	public ModelAndView save(MsgBean mbean,UploadGenre uploadGenre) {
		mbean.setInfo("保存成功");
		iUploadGenreBiz.save(uploadGenre);
		return json(mbean);
	}

	@RequestMapping("/uploadgenre/delete") 
	public ModelAndView delete(MsgBean mbean,UploadGenre uploadGenre) {
		mbean.setInfo("删除成功");
		iUploadGenreBiz.delete(uploadGenre);
		return json(mbean);
	}

}
