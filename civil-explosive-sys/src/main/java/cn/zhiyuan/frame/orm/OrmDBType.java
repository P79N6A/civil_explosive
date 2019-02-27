package cn.zhiyuan.frame.orm;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.zhiyuan.frame.CommonUtils;

public class OrmDBType {
	protected  Log log = LogFactory.getLog(this.getClass());

	private static String mysql = "MySQL";
	private static String h2 = "H2";
	private static String oracle = "Oracle";
	private static String mssqlserver = "Microsoft SQL Server";

	private String dbName;
	private String dbVersion;
	
	private DataSource dataSource;
	
	//数据库类型
	private DBType  dbType;
	private String  schem = "%";
	//oracle
	public boolean isOracle(){
		return DBType.oracle == dbType;
	}
	
	//mysql
	public boolean isMysql(){
		return DBType.mysql == dbType;
	}
	
	//mssql
	public boolean isMssql(){
		return DBType.sqlserver == dbType;
	}
	
	public OrmDBType(DataSource dataSource){
		Connection conn = null;
		if(dbType != null) return;
		this.dataSource = dataSource;
		//log.debug("判断数据库类型:" + dataSource);
		try {
			conn = dataSource.getConnection();
			DatabaseMetaData meta = conn.getMetaData();
			dbName = meta.getDatabaseProductName();
			dbVersion = meta.getDatabaseProductVersion();
			if(mssqlserver.equals(dbName)) dbType = DBType.sqlserver;
			else if(mysql.equals(dbName) || h2.equals(dbName)) {
				dbType = DBType.mysql;//mysql h2语法基本一致
			}
			else if(oracle.equals(dbName)){
				dbType = DBType.oracle;
				dbVersion = CommonUtils.regx(dbVersion, "\\s(\\d.[^\\s]+\\d)\\s");
			}else{
				log.error("暂时不支持"+dbName+"数据库");
			}
			log.debug("数据库:" + dbName + ",版本:" + dbVersion + ",识别:" + dbType);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public String getSchem() {
		return schem;
	}

	enum DBType {
		
		oracle,
		
		mysql,
		
		sqlserver
		
	}
	
}
