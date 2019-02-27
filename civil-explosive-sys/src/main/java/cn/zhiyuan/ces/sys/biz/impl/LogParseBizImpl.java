package cn.zhiyuan.ces.sys.biz.impl;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.zhiyuan.ces.sys.biz.ILogParseBiz;
import cn.zhiyuan.frame.CommonUtils;

@Service
public class LogParseBizImpl implements ILogParseBiz {

	private final static int BLOCK_SIZE = 1024 * 50;
	
	private final static String TIME_REGX = "\\d{2}:\\d{2}:\\d{2}\\.\\d{3}";
	//2018-08-24 02:59:43.869 com.garen.Main  33 [main] xxx
	private final static String DATE_TIME_REGX1 = "^(\\d{4}-\\d{2}-\\d{2}\\s"  + TIME_REGX + ")(.*)";
	//08-24 02:59:43.869 com.garen.Main  33 [main] xxxx
	private final static String DATE_TIME_REGX2 = "^(\\d{2}-\\d{2}" + "\\s" + TIME_REGX + ")(.*)";
	//02:59:43.869 com.garen.Main  33 [main] true
	private final static String DATE_TIME_REGX3 = "^(" + TIME_REGX + ")(.*)";
	
	//读取日志文件
	@Override
	public List<JSONObject> logRead(String logPath, String logName, Integer logTime) {
		List<JSONObject> jsons = new ArrayList<>();
		String[] results = readLog(logPath + logName,logTime);
		if(null == results) return jsons;
		JSONObject json = null;
		StringBuilder sb = null;
		for(String result : results) {
			String[] strs = isDatePrefix(result);
			if(strs == null) {
				if(sb != null) {
					sb.append("<br/>" + result);
				}
			}else {
				if(json != null) {//上一行结束
					json.put("body", sb.toString());
					jsons.add(json);
				}
				json = new JSONObject();
				json.put("datePrefix", strs[0]);
				sb = new StringBuilder();
				sb.append(strs[1]);
			}
		}
		if(json != null) {//上一行结束
			json.put("body", sb.toString());
			jsons.add(json);
		}
		return jsons;
	}

	//日志时间戳前缀
	private String[] isDatePrefix(String str) {
		String[] strs = null;
		strs = CommonUtils.regMatches(str,DATE_TIME_REGX1);
		if(strs.length == 0) {
			strs = CommonUtils.regMatches(str,DATE_TIME_REGX2);
			if(strs.length == 0) {
				strs = CommonUtils.regMatches(str,DATE_TIME_REGX3);
			}
		}
		return strs.length == 0?null:strs;
	}
	
	//读取日志文件
	private String[] readLog(String logPath,Integer logTime) {
		String[] results = null;
		try {
			RandomAccessFile raf = new RandomAccessFile(logPath, "r");
			byte[] body = new byte[BLOCK_SIZE];
			if(logTime == null) {//读取结尾
				raf.seek(raf.length() - BLOCK_SIZE);
			}else {
				raf.seek(raf.length() * logTime / 1000);
			}
			int len = raf.read(body);
			int offset = 0;
			String splitStr = "\n";
			for(int i = 0;i < len - 1;i++) {
				if(body[i] == 0xa) {
					if(body[i+1] == 0xd) {
						offset = i+1;
						splitStr = "\n\r";
					}else offset = i;
					break;
				}
			}
			String result = new String(Arrays.copyOfRange(body, offset,len),"gbk");
			results = result.split(splitStr);
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}
}
