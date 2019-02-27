package cn.zhiyuan.frame;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.alibaba.fastjson.JSONObject;

import cn.zhiyuan.frame.orm.IOrmTable;
import cn.zhiyuan.frame.orm.OrmBean;
import cn.zhiyuan.frame.orm.OrmFactory;
import cn.zhiyuan.frame.orm.builder.IWhereSql;

public abstract class AbstractDao<T> implements IAbstractDao<T>,RowMapper<T> {

	protected  Log log = LogFactory.getLog(this.getClass());
	
	protected boolean islog = true;
	
	private DataSource dataSource;
	
	private NamedParameterJdbcTemplate jdbcTemp;
	
	private IOrmTable ormtable;
	
	/*
	 * ******************************
	 * 数据源，数据库类型，实体映射初始化
	 * ******************************
	 * */
	//注入数据源
	protected void initDataSource(DataSource ds,OrmFactory ormFactory) {
		this.jdbcTemp = new NamedParameterJdbcTemplate(ds);
		ormtable = ormFactory.createORM(ds, this.getClass());
		this.dataSource = ds;
	}
	
	/*
	 * 保存
	 * 根据主键判断新增 or 更新
	 */
	public int save(T obj) {
		return ormtable.idIsNull(obj)?addAndId(obj):update(obj);
	}
	/*
	 * 新增记录
	 * T obj 实体
	 * nullFlag true字段可为null
	 * */
	private int add(T obj,boolean nullFlag) {
		return update(ormtable.addSQL(obj, nullFlag));
	}
	
	/*
	 * 新建
	 * 返回改变的行数
	 */
	@Override
	public int add(T obj){
		return add(obj,false);
	}
	/*
	 * 新建
	 * 返回改变的行数
	 */
	@Override
	public int addWithNull(T obj){
		return add(obj,true);
	}
	/*
	 * 新建,自增主键写入实体类中
	 * 返回改变的行数
	 * nullFlag true忽略为Null的字段
	 */
	@Override
	public int addAndId(T obj){
		//boolean log_tmp = this.islog;
		//this.islog = false;
		int ret = add(obj,false);
		Integer id =  queryObject(ormtable.autoIdSQL(), Integer.class);
		ormtable.setIdValue(obj, id);
		//this.islog = log_tmp;
		return ret; 
	}
	/*
	 * 新建,自增主键写入实体类中
	 * 返回改变的行数
	 * nullFlag true忽略为Null的字段
	 */
	@Override
	public int addAndIdWithNull(T obj){
		int ret = add(obj,true);
		Integer id =  queryObject(ormtable.autoIdSQL(), Integer.class);
		ormtable.setIdValue(obj, id);
		return ret;
	}
	
	//主键做为条件,更新不为null字段
	@Override
	public int update(T obj) {
		return update(ormtable.updateSQL(obj,false));
	}
	
	@Override
	public int updateWithNull(T obj) {
		return update(ormtable.updateSQL(obj,true));
	}
	
	//field字段不为Null 做为where条件
	@Override
	public int update(T obj, String field) {
		return update(ormtable.updateSQL(obj,false,field));
	}
	
	/*
	 * field字段不为Null 做为where条件
	 * 主键作为条件,不为null时起作用
	 * */
	@Override
	public int updateWithNull(T obj, String field) {
		return update(ormtable.updateSQL(obj,true,field));
	}
	
	@Override
	public int delete(T obj) {
		return update(ormtable.deleteSQL(obj));
	}
	
	@Override
	public int delete(Integer id) {
		return update(ormtable.deleteByIdSQL(id));
	}
	
	/*
	 * 查询
	 * */
	@Override
	public T get(Integer id) {
		List<T> list =  queryList(ormtable.selectByIdSQL(id));
		return list.isEmpty()?null:list.get(0);
	}

	@Override
	public T get(T obj) {
		List<T> list = queryList(ormtable.selectSQL(obj,null,null));
		return list.isEmpty()?null:list.get(0);
	}
	/*
	 * 查询列表
	 */
	@Override
	public List<T> getList(T obj,String order){
		return queryList(ormtable.selectSQL(obj,order,null));
	}
	
	/*
	 * 查询列表
	 */
	@Override
	public List<T> getList(T obj,String order,IWhereSql whereSql){
		return queryList(ormtable.selectSQL(obj,order,whereSql));
	}
	/*
	 * 查询列表
	 */
	@Override
	public List<T> getPageList(PageBean<T> pb,T obj,String order){
		return getPageList(pb,obj,order,null);
	}
	/*
	 * 查询列表
	 */
	@Override
	public List<T> getPageList(PageBean<T> pb,T obj,String order,IWhereSql whereSql){
		if(pb.getIsPage()){
			OrmBean ormbean = ormtable.selectPageSQL(pb,obj,order,whereSql);
			pb.setTotal(queryObject(ormbean.getPageSql(), ormbean.getParams(),Integer.class));
			pb.setRows(queryList(ormbean));
		}else {//不分页
			pb.setRows(getList(obj, order));
			pb.setTotal(pb.getRows().size());
		}
		return  pb.getRows();
	}
	
	/*
	 * *****自定义SQL语句***
	 * */
	//查询基本数据类型,如String,Integer,Long,Double等
	public <TT> TT queryObject(OrmBean ormbean, Class<TT> requiredType) {
		if(ormbean == null) return null;
		printLog(ormbean);
		try {
			return jdbcTemp.queryForObject(ormbean.getSql(), ormbean.getParams(), requiredType);
		}catch(EmptyResultDataAccessException ex) {}
		return null;
	}
		
	public <TT> TT queryObject(String sql,Map<String,Object> params, Class<TT> requiredType) {
		printLog(sql,params);
		try {
			return jdbcTemp.queryForObject(sql, params, requiredType);
		}catch(EmptyResultDataAccessException ex) {}
		return null;
	}
	
	//jdbc层封装
	private int update(OrmBean ormbean) {
		if(ormbean == null) return -1;
		printLog(ormbean);
		return jdbcTemp.update(ormbean.getSql(), ormbean.getParams());
	}
	
	protected int update(String sql,Map<String,Object> params) {
		printLog(sql,params);
		return jdbcTemp.update(sql,params);
	}
	
	/*
	 * 查询列表
	 * */
	private List<T> queryList(OrmBean ormbean){
		if(ormbean == null) return new ArrayList<>();
		printLog(ormbean);
		return jdbcTemp.query(ormbean.getSql(), ormbean.getParams(), this);
	}
	
	//自定义语句查询
	public T query(String sql,
			Map<String,Object> paramMap){
		printLog(sql,paramMap);
		List<T> list =  jdbcTemp.query(sql, paramMap, this);
		return list.isEmpty()?null:list.get(0);
	}
	
	//自定义语句查询
	public List<T> queryList(String sql,
			Map<String,Object> paramMap){
		printLog(sql,paramMap);
		return jdbcTemp.query(sql, paramMap, this);
	}
	
	//自定义语句分页查询
	public List<T> queryPageList(PageBean<T> pb,String sql,
				Map<String,Object> paramMap,String order){
		List<T> list = null;
		if(pb.getIsPage()) {
			OrmBean ormbean = ormtable.pageSql(pb, sql, order);
			pb.setTotal(queryObject(ormbean.getPageSql(), paramMap,Integer.class));
			list = queryList(ormbean.getSql(),paramMap);
		}else {//不分页
			list = queryList(sql,paramMap);
			pb.setTotal(list.size());
		}
		pb.setRows(list);
		return list;
	}
	
	/*
	 * ***结果集为JSON对象*********
	 * */
	//查询单个json对象
	public JSONObject  queryJson(String sql,JSONObject paramJson){
		List<JSONObject> list = queryJsons(sql,paramJson);
		return list.isEmpty()?null:list.get(0);
	}
	//查询list列表
	public List<JSONObject>  queryJsons(String sql,JSONObject paramJson){
		printLog(sql,paramJson);
		return jdbcTemp.query(sql, paramJson, new RowMapper<JSONObject>() {
			@Override
			public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
				ResultSetMetaData meta = rs.getMetaData();
				JSONObject json = new JSONObject();
		    	int colCount = meta.getColumnCount();
		    	for(int i = 1;i <= colCount;i++){//获取所有列
		    		String colName = meta.getColumnLabel(i);
		    		Object val = rs.getObject(colName);
		    		json.put(colName, val);
		    	}
				return json;
			}
		});
	}
	
	/*
	 * ***结果集为JSON对象,并分页*********
	 * */
	public List<JSONObject> queryPageJsons(PageBean<JSONObject> pb, String sql,
			JSONObject whereJson, String order) {
		OrmBean ormbean = ormtable.pageSql(pb, sql, order);
		pb.setTotal(queryObject(ormbean.getPageSql(), whereJson,Integer.class));
		List<JSONObject> list = queryJsons(ormbean.getSql(),whereJson);
		pb.setRows(list);
		return list;
	}
	
	//实现接口封装
	@Override
	public T mapRow(ResultSet rs, int arg1) throws SQLException {
		return ormtable.ormBean(rs); 
	}
	
	private void printLog(OrmBean ormbean) {
		if(!islog) return;
		log.debug(ormbean.getSql());
		if(ormbean.getParams() != null &&!ormbean.getParams().isEmpty()) 
			log.debug(ormbean.getParams());
	}
		
	private void printLog(String sql,Object params) {
		if(!islog) return;
		log.debug(sql);
		log.debug(params);
	}

	protected DataSource getDataSource() {
		return dataSource;
	}

	protected NamedParameterJdbcTemplate getJdbcTemp() {
		return jdbcTemp;
	}
	
}
