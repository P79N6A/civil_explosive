package cn.zhiyuan.frame.log;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;

import cn.zhiyuan.ces.sys.biz.ISysCodeBiz;
import cn.zhiyuan.ces.sys.entity.SysCode;
import cn.zhiyuan.frame.BaseDao;
import cn.zhiyuan.frame.CommonUtils;
import cn.zhiyuan.frame.PageBean;

@Repository
public class LogJdbcBizImpl extends BaseDao<Object> implements ILogJdbcBiz {

	@Autowired
	private ISysCodeBiz iSysCodeBiz;
	
	@Override
	public List<JSONObject> getLogList(PageBean<JSONObject>pb, Date kssj, Date jssj, String thread) {
		String sql = "select * from web_log_1 t where t.cdate between :kssj and :jssj ";
		JSONObject params = new JSONObject();
		params.put("kssj",kssj);
		params.put("jssj",jssj);
		if(StringUtils.isNotEmpty(thread)) {
			params.put("thread","%"+thread+"%");
			sql += " and t.thread like :thread ";
		}
		return super.queryPageJsons(pb, sql, params, " t.xh asc ");
	}

	/*
	 * 定时清理 
	 * 3天内的日志，导入备份表
	 * 15天前的，日志删除
	 * */
	public void cleanLog() {
		log.debug("清理备份日志--开始!");
		long t1 = System.currentTimeMillis();
		NamedParameterJdbcTemplate jdbcTemp = super.getJdbcTemp();
		SysCode code = new SysCode();
		code.setCodeType("j");
		code.setCodeValue("4");
		code = iSysCodeBiz.get(code);
		if(code == null) return;
		Integer offset1 = CommonUtils.str2int(code.getCodeDesc1(), 15);
		Integer offset2 = CommonUtils.str2int(code.getCodeDesc2(), 3);
		log.debug(offset1 + "," + offset2);
		Calendar cale = null;
		Date date = null;
		String sql = null;
		Map<String,Object> params = new HashMap<>();
		try {
			//清理备份库
			cale = Calendar.getInstance();
			cale.add(Calendar.DATE,0 - offset1);
			date = cale.getTime();
			sql = "delete from web_log_2 where cdate < :date";
			params.put("date", date);
			log.debug(sql);
			log.debug(params);
			jdbcTemp.update(sql, params);
			//复制数据到备份库
			cale = Calendar.getInstance();
			cale.add(Calendar.DATE,0 - offset2);
			date = cale.getTime();
			sql = "insert into web_log_2(cdate,thread,log_level,cls_name,msg) "
					+ " select cdate,thread,log_level,cls_name,msg from web_log_1 t1 where t1.cdate < :date";
			params.put("date", date);
			log.debug(sql);
			log.debug(params);
			jdbcTemp.update(sql, params);
			//清理当前库
			sql = "delete from web_log_1 where cdate < :date";
			log.debug(sql);
			log.debug(params);
			jdbcTemp.update(sql, params);//参数不变
			long t2 = System.currentTimeMillis();
			log.debug("清理备份日志--结束!耗时:" + (t2 - t1) + "ms");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
