package cn.zhiyuan.frame.log;

import java.sql.SQLException;

import org.apache.log4j.jdbc.JDBCAppender;

public class LogJdbcAppender extends JDBCAppender {

	@Override
	protected void execute(String sql) throws SQLException {
		//System.out.println(this + ":" + Thread.currentThread() + ":" + sql);
	}
	
}
