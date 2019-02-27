package cn.zhiyuan.ces.sys.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.zhiyuan.ces.sys.biz.ILogParseBiz;
import cn.zhiyuan.frame.BaseAction;
import cn.zhiyuan.frame.CommonUtils;
import cn.zhiyuan.frame.MsgBean;

@Controller
@RequestMapping("/sys")
public class LogParseAction extends BaseAction {

	@Autowired
	private ILogParseBiz logParseBiz;
	
	@RequestMapping("/logparse/logFiles") 
	public ModelAndView jsons(MsgBean mbean,String logPath) {
		JSONArray logFiles = new JSONArray();
		File file = new File(logPath);
		File[] files = file.listFiles();
		for(File logf : files) {
			if(logf.isDirectory()) continue;
			JSONObject json = new JSONObject();
			json.put("logName", logf.getName());
			logFiles.add(json);
		}
		mbean.setData(logFiles);
		return json(mbean);
	}
	
	@RequestMapping("/logparse/logRead") 
	public ModelAndView logRead(MsgBean mbean,String logPath,String logName,Integer logTime) {
		mbean.setData(logParseBiz.logRead(logPath, logName, logTime));
		return json(mbean);
	}
	
	@RequestMapping("/logparse/logGZip") 
	public ModelAndView logRead(MsgBean mbean,String logPath,String logName) {
		try {
			byte []body = new byte[4096];
			String gzipPath = CommonUtils.getAppPath("/temp/" + logName + ".gz");
			GZIPOutputStream gzipOut = new GZIPOutputStream(new FileOutputStream(gzipPath));
			FileInputStream fin = new FileInputStream(new File(logPath + logName));
			do {
				int len = fin.read(body);
				if(-1 == len) break;
				gzipOut.write(body,0, len);
			}while(true);
			fin.close();
			gzipOut.finish();
			gzipOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json(mbean);
	}
	
	@RequestMapping("/logparse/downLog") 
	public ModelAndView downLog(MsgBean mbean,String logName) {
		logName += ".gz";
		String gzipPath = CommonUtils.getAppPath("/temp/" + logName);
		File file = new File(gzipPath);
		return super.downFile(file, GZIP_MIME, logName);
	}
	
}
