package cn.zhiyuan.frame.log;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.CommonUtils;
import cn.zhiyuan.frame.PageBean;

@Controller
@RequestMapping("/")
public class LogJdbcAction extends BaseAction {

	@Autowired
	private ILogJdbcBiz logbiz;
	
	@RequestMapping("/logJsons") 
	public ModelAndView logJsons(PageBean<JSONObject>pb, Date kssj, Integer dateOffset, String thread) {
		if(dateOffset == null) dateOffset = 1;
		if(dateOffset < 0 || dateOffset > 10) dateOffset = 1;
		Date jssj = new Date(kssj.getTime() + 60 * 1000 * dateOffset);
		logbiz.getLogList(pb, kssj, jssj, thread);
		for(JSONObject json : pb.getRows()) {
			Date date = json.getDate("cdate");
			json.put("dateStr", CommonUtils.date2str(date, "yyyy-MM-dd HH:mm:ss.SSS"));
		}
		return json(pb);
	}
	
}
