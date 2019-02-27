package cn.zhiyuan.frame.orm;

import java.util.Map;

public class OrmBean {

	public OrmBean() {}
	
	public OrmBean(String sql,Map<String, Object> params) {
		this.sql = sql;
		this.params = params;
	}
	
	public OrmBean(String sql,String pageSql) {
		this.sql = sql;
		this.pageSql = pageSql;
	}
	
	public OrmBean(String sql,String pageSql,Map<String, Object> params) {
		this.sql = sql;
		this.params = params;
		this.pageSql = pageSql;
	}
	
	private String sql;
	
	private String pageSql;
	
	
	private Map<String,Object> params;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String getPageSql() {
		return pageSql;
	}

	public void setPageSql(String pageSql) {
		this.pageSql = pageSql;
	}
	
}
