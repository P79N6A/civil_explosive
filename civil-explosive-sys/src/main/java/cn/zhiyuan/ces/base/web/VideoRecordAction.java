package cn.zhiyuan.ces.base.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.zhiyuan.ces.base.biz.IVideoRecordBiz;
import cn.zhiyuan.ces.base.entity.VideoRecord;
import cn.zhiyuan.ces.camera.biz.ICameraConfigBiz;
import cn.zhiyuan.ces.sys.biz.ISysCodeBiz;
import cn.zhiyuan.ces.sys.entity.SysCode;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.CommonUtils;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.PageBean;
import cn.zhiyuan.frame.orm.builder.IWhereSql;

@Controller
@RequestMapping("/base")
public final class VideoRecordAction extends BaseAction{

	@Autowired
	private IVideoRecordBiz iVideoRecordBiz;

	@Autowired
	private ISysCodeBiz iSysCodeBiz;
	
	@Autowired
	protected ICameraConfigBiz configBiz;
	
	private String getVodRtmpUrl(List<SysCode> codeList,String appName,boolean isLocal) {
		for(SysCode code : codeList) {
			if(appName.equals(code.getCodeDesc3())) {
				return isLocal?code.getCodeDesc1():code.getCodeDesc2();
			}
		}
		return configBiz.getRtmpUrl(isLocal);
	}
	
	/*
	 * 查询回放记录
	 * */
	@RequestMapping("/videorecord/jsons") 
	public ModelAndView json(HttpServletRequest request,PageBean<VideoRecord> pb,VideoRecord videoRecord,Date kssj,Date jssj) {
		final Date kssj1 = CommonUtils.clearDate(kssj);
		final Date jssj1 = CommonUtils.fullDate(jssj);
		iVideoRecordBiz.getPageList(pb,videoRecord,"t.id desc",new IWhereSql() {
			@Override
			public void where(StringBuilder sb, Map<String, Object> params) {
				sb.append(" and t.start_time between :kssj and :jssj ");
				params.put("kssj", kssj1);
				params.put("jssj", jssj1);
			}
		});
		SysCode code = new SysCode();
		code.setCodeType("k");
		code.setCodeDesc4("3");
		String ip = request.getRemoteAddr();
		boolean isLocal = ip.indexOf("192.168.") != -1?true:false;
		List<SysCode> codeList = iSysCodeBiz.getList(code,null);
		for(VideoRecord record : pb.getRows()) {
			record.setRtmpUrl(getVodRtmpUrl(codeList, record.getVideoPath1(), isLocal));
		}
		return json(pb);
	}
	
	@RequestMapping("/videorecord/save") 
	public ModelAndView save(MsgBean mbean,VideoRecord videoRecord) {
		mbean.setInfo("保存成功");
		iVideoRecordBiz.save(videoRecord);
		return json(mbean);
	}

	@RequestMapping("/videorecord/delete") 
	public ModelAndView delete(MsgBean mbean,VideoRecord videoRecord) {
		mbean.setInfo("删除成功");
		iVideoRecordBiz.delete(videoRecord);
		return json(mbean);
	}

}
