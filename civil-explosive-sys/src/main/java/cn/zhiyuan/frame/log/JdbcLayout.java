package cn.zhiyuan.frame.log;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletContext;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.web.context.ContextLoader;

import cn.zhiyuan.frame.CommonUtils;

public class JdbcLayout extends Layout {


	private BasicDataSource bds;
	
	Connection conn = null;
	@Override
	public void activateOptions() {
		ServletContext sc = ContextLoader.getCurrentWebApplicationContext().getServletContext();
		bds = CommonUtils.getSpringBean(sc, "dataSource", BasicDataSource.class);
		try {
			conn = bds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String format(LoggingEvent event) {
		try {
			String sql = "insert into web_log_1(cdate,thread,log_level,cls_name,msg) "
					+ " values(?,?,?,?,?)";
			//Connection conn = bds.getConnection();
			//System.out.println(Thread.currentThread().getName() +  ",,conn:" + conn.hashCode());
			conn.setAutoCommit(true);
			java.sql.PreparedStatement ps = conn.prepareStatement(sql);
			ps.setObject(1, new Date(event.timeStamp));
			ps.setObject(2,event.getThreadName());
			ps.setObject(3,String.valueOf(event.getLevel()));
			ps.setObject(4,event.getLoggerName());
			ps.setObject(5,event.getRenderedMessage());
			ps.execute();
			//conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean ignoresThrowable() {
		return false;
	}

}
