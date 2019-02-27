package cn.zhiyuan.frame.orm;

import java.sql.ResultSet;

import cn.zhiyuan.frame.PageBean;
import cn.zhiyuan.frame.orm.builder.IWhereSql;

/*
 * 实体关系映射接口
 * */
public interface IOrmTable {

	/*
	 * 判断主键是否为空
	 * */
	boolean idIsNull(Object obj);
	/*
	 * 获取自增值sql
	 * */
	OrmBean autoIdSQL();
	
	/*
	 * 主键赋值
	 * */
	boolean setIdValue(Object obj,Integer id);
	
	/*实体映射新增sql语句*/
	OrmBean addSQL(Object obj, boolean nullFlag);
	
	/*实体映射修改sql语句*/
	OrmBean updateSQL(Object obj,boolean hasNull);
	
	/*实体映射修改sql语句*/
	OrmBean updateSQL(Object obj,boolean hasNull,String field);
	
	/*实体映射删除sql语句*/
	OrmBean deleteSQL(Object obj);
	
	/*实体映射删除sql语句，主键为int类型且不是复合主键*/
	OrmBean deleteByIdSQL(Integer id);
	
	/*实体映射查询个体sql语句，主键为int类型且不是复合主键*/
	OrmBean selectByIdSQL(Integer id);
	
	/*实体映射sql语句*/
	OrmBean selectSQL(Object obj,String orderBy,IWhereSql whereSql);
	
	/*实体映射分页sql语句*/
	OrmBean selectPageSQL(PageBean<?>  pb, Object obj,String orderBy);
	//扩展查询条件
	OrmBean selectPageSQL(PageBean<?> pb, Object obj, String order,IWhereSql whereSql);
	
	//创建分页语句
	OrmBean pageSql(PageBean<?>  pb,String sql, String order);

	//创建实例并赋值
	<T> T ormBean(ResultSet rs);
	
}
