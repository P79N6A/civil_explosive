package cn.zhiyuan.frame;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.zhiyuan.frame.orm.OrmFactory;

public class BaseDao<T> extends AbstractDao<T> {

	/*
	 * 注入数据源
	 * */
	@Autowired  
	public void setDS(@Qualifier("dataSource") DataSource ds,OrmFactory ormFactory){
		super.initDataSource(ds,ormFactory);
	}
	
}
