package cn.zhiyuan.ces.base.web;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import cn.zhiyuan.ces.base.biz.IUploadResourceBiz;
import cn.zhiyuan.ces.base.entity.UploadResource;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.CommonUtils;
import cn.zhiyuan.frame.MsgBean;


@Controller
@RequestMapping("/base")
public final class UploadResourceAction extends BaseAction{

	@Autowired
	private IUploadResourceBiz iUploadResourceBiz;

	private String getUploadPath(String path) {
		return CommonUtils.getAppPath("/upload" + path);
	}
	
	/*
	 * 上传附件
	 * */
	@RequestMapping("/uploadresource/upload")
	public ModelAndView upload(MsgBean mbean,UploadResource uploadResource
			,@RequestParam MultipartFile imgfile,String extName) {
		if(imgfile.isEmpty() 
				|| uploadResource.getHostGenre() == null) {
			mbean.setRetInfo(-1, "参数不正确 !");
		}else {
			//扩展名
			//String extName = CommonUtils.getExtName(imgfile.getOriginalFilename());
			if(extName == null) extName = "png";
			String imgPath = CommonUtils.date2str(new Date(), "/yyyy/MM/dd/");
			String imgName = CommonUtils.createUUID()  + extName;
			uploadResource.setPath(imgPath + imgName);
			uploadResource.setFileType(extName);
			uploadResource.setSize((int)imgfile.getSize());
			uploadResource.setName(imgfile.getOriginalFilename());
			log.debug(imgfile.getName() + "," + imgfile.getOriginalFilename());
			//写入文件
			try {
				File imgDir = new File(getUploadPath(imgPath));
				if(!imgDir.exists()) imgDir.mkdirs();
				File imgFile = new File(imgDir,imgName);
				imgfile.transferTo(imgFile);
				iUploadResourceBiz.addAndId(uploadResource);
				mbean.setObj(uploadResource);
			} catch (IllegalStateException | IOException e) {
				mbean.setRetInfo(-2, "保存附件发生异常 : " + e.getLocalizedMessage());
			}
		}
		return html(JSON.toJSONString(mbean));
	}
	
	@RequestMapping("/uploadresource/jsons") 
	public ModelAndView jsons(MsgBean mbean,UploadResource uploadResource) {
		if(uploadResource.getHostGenre() == null
				|| uploadResource.getHostId() == null) {
			mbean.setRetInfo(-1, "参数不正确 !");
		}else {
			mbean.setObj(iUploadResourceBiz.getList(uploadResource.getHostGenre(),uploadResource.getHostId()));
		}
		return json(mbean);
	}

	@RequestMapping("/uploadresource/loadimg") 
	public ModelAndView loadImg(MsgBean mbean,UploadResource uploadResource) {
		mbean.setInfo("保存成功");
		if(uploadResource.getId() != null) {
			uploadResource = iUploadResourceBiz.get(uploadResource);
			if(uploadResource != null) {
				String imgPath = getUploadPath(uploadResource.getPath());
				byte[] imgBody = CommonUtils.readData(imgPath);
				return down(imgBody, PNG_MIME);
			}
		}
		return null;
	}

	@RequestMapping("/uploadresource/down") 
	public ModelAndView down(MsgBean mbean,UploadResource uploadResource) {
		mbean.setInfo("保存成功");
		if(uploadResource.getId() != null) {
			uploadResource = iUploadResourceBiz.get(uploadResource);
			if(uploadResource != null) {
				String imgPath = getUploadPath(uploadResource.getPath());
				byte[] imgBody = CommonUtils.readData(imgPath);
				String mime = null;
				switch(uploadResource.getFileType()) {
				case "pdf":
					mime = PDF_MIME;
					break;
				case "doc":
				case "docx":
					mime = WORD_MIME;
					break;
				case "xls":
				case "xlsx":
					mime = XLS_MIME;
					break;
				}
				downFile(imgBody, mime,uploadResource.getName());
			}
		}
		return null;
	}
	@RequestMapping("/uploadresource/loadImgBypath") 
	public ModelAndView loadImgBypath(MsgBean mbean,String imgPath) {
		mbean.setInfo("保存成功");
		imgPath = getUploadPath(imgPath);
		byte[] imgBody = CommonUtils.readData(imgPath);
		return imgBody == null?null:down(imgBody, PNG_MIME);
	}
	
	@RequestMapping("/uploadresource/delete") 
	public ModelAndView delete(MsgBean mbean,UploadResource uploadResource) {
		mbean.setInfo("删除成功");
		if(uploadResource.getId() != null) {
			uploadResource = iUploadResourceBiz.get(uploadResource);
			if(uploadResource != null) {
				String imgPath = getUploadPath(uploadResource.getPath());
				new File(imgPath).delete();//删除文件
				iUploadResourceBiz.delete(uploadResource.getId());
			}
		}
		return json(mbean);
	}

}
