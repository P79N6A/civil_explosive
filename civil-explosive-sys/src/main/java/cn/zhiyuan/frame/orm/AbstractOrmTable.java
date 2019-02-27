package cn.zhiyuan.frame.orm;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.zhiyuan.frame.CommonUtils;
import cn.zhiyuan.frame.PageBean;
import cn.zhiyuan.frame.orm.builder.IWhereSql;

/*对象关系抽象类*/
public abstract class AbstractOrmTable implements IOrmTable{

	protected final static Log log = LogFactory.getLog(AbstractOrmTable.class);
	
	private String schema;
	
	//表名
	private String tableName;
	
	//实体类型
	private Class<?> entityClass;
	
	//复合主键
	private boolean cpkey;
	
	//关联的字段信息
	private List<OrmColumn> collist= new ArrayList<OrmColumn>();
		
	//主键sql语句
	protected void wrapIdSql(List<String> fieldList,List<String> valueSql,String colName) {}
	
	public static Field[] getClassFields(Class<?> tClass) {
		if(Object.class.equals(tClass)) return null;
		Field[] fields1 = tClass.getDeclaredFields();
		Field[] fields2 = getClassFields(tClass.getSuperclass());
		if(fields2 != null) {
			fields1 = ArrayUtils.addAll(fields1,fields2);
		}
		return fields1;
	}
	
	public boolean init(OrmDBType ormdb,Class<?> tClass) {
		Table table = (Table) tClass.getAnnotation(Table.class);
		if (table == null) return false;
		this.entityClass = tClass;//设置实体类型
		this.tableName = table.name();
		this.schema = table.schema();
		// 初始化字段
		//递归获取所有变量
		Field[] fields = getClassFields(tClass);//tClass.getDeclaredFields();
		boolean pk = false;
		for (Field field : fields) {
			try {// 异常继续，故异常在循环内
				OrmColumn ormCol = new OrmColumn();
				Column col = (Column) field.getAnnotation(Column.class);
				//判断是否子查询
				Formula formula = (Formula) field.getAnnotation(Formula.class);
				if(formula != null){//不需要继续遍历别的注解了
					ormCol.setColName(formula.name());
					ormCol.setFormulaSQL(formula.value());
					ormCol.setFieldName(field.getName());
					ormCol.setOnlyOne(formula.onlyOne());
					ormCol.setField(field);
					ormCol.setNullable(false);
					ormCol.setExtCol(true);
					collist.add(ormCol);// 添加
					continue;
				}
				if (col == null)  continue;
				ormCol.setField(field);
				ormCol.setColName(col.name());
				ormCol.setFieldName(field.getName());
				ormCol.setNullable(col.nullable());//是否为空
				ormCol.setExtCol(!col.insertable());
				// 是否为主键
				Id id = (Id) field.getAnnotation(Id.class);
				if (id != null) {//主键
					GeneratedValue gv = (GeneratedValue) field.getAnnotation(GeneratedValue.class);
					if(gv != null){
						if (gv.strategy() == GenerationType.IDENTITY)
							ormCol.setAutoFlag(true);//设置为自增
						else if(gv.strategy() == GenerationType.SEQUENCE){//oracle序列模式
							ormCol.setSequence(gv.generator());//设置模式名称
						}
					}
					ormCol.setId(true);// 主键
					if(pk) cpkey = true;//设置为复合主键
					else pk = true;
				}
				collist.add(ormCol);// 添加
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	/*
	 * 延迟子类实现
	 * */
	abstract protected String wrapFormulaSql(String formulaSQL,String colName);
	
	abstract protected StringBuilder wrapPageSql(PageBean<?>pb,StringBuilder sb,String order);

	//获取序列名称
	protected String getIdSequence(){
		for(OrmColumn ormCol : collist){
			if(ormCol.isId()){
				return ormCol.getSequence();
			}
		}
		return null;
	}
		
	//设置主键
	@Override
	public boolean setIdValue(Object obj,Integer id) {
		if(cpkey) return false;//复合主键不能设置值
		for(OrmColumn ormCol : collist){
			if(ormCol.isId()){
				ormCol.setValue(obj,id);
				break;
			}
		}
		return true;
	}
	
	/*实体映射新增sql语句*/
	@Override
	public OrmBean addSQL(Object obj,boolean hasNull) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		List<String> fieldList = new ArrayList<>(),valueList = new ArrayList<>();
		for(OrmColumn ormCol : collist){
			Object value = ormCol.getValue(obj);
			String colName = ormCol.getColName();
			if(ormCol.isExtCol()) {
				//log.debug("extcol :" + colName + "," + ormCol.getFieldName());
				continue;
			}
			//主键
			if(ormCol.isId()) {
				if(ormCol.isAutoFlag()) continue;//自增字段跳过
				if(value == null) wrapIdSql(fieldList, valueList,colName);
			}
			if(value == null) {
				if(!ormCol.isNullable()){//不能为空
					error(-2,colName + "字段,"+ ormCol.getFieldName()+"域不能为空 !");
					return null;
				}
				if(!hasNull) continue;//值为null跳过
			}
			fieldList.add(colName);
			valueList.add(":" + colName);
			paramMap.put(ormCol.getColName(), value);
		}
		if(fieldList.isEmpty() || valueList.isEmpty()){
			error(-2,"域不能为空 !");
			return null;
		}
		//生成sql语句
		StringBuilder sb = new StringBuilder();
		sb.append("insert into " + getSchemaVal());
		sb.append("(" + String.join(",", fieldList) + ") ");
		sb.append(" values(" + String.join(",", valueList) + ")");
		return  new OrmBean(sb.toString(),paramMap);
	}
	
	/*实体映射修改sql语句*/
	@Override
	public OrmBean updateSQL(Object obj,boolean hasNull) {
		return updateSQL(obj,hasNull,null);
	}
	
	/*实体映射修改sql语句*/
	@Override
	public OrmBean updateSQL(Object obj,boolean hasNull,String fields) {
		Set<String> fieldSet = CommonUtils.str2Set(fields);
		Map<String,Object> paramMap = new HashMap<String,Object>();
		List<String> fieldList = new ArrayList<>(),whereList = new ArrayList<>();
		for(OrmColumn ormCol : collist){
			Object value = ormCol.getValue(obj);
			String colName = ormCol.getColName();
			//外部字段跳出
			if(ormCol.isExtCol()) continue;
			if(ormCol.isId()) {//主键
				if(value == null) {
					//条件不为空，继续
					if(!fieldSet.isEmpty()) continue;
					error(-1, "更新记录，主键不能为空 !");
					return null;//主键为空，返回-1
				}
				whereList.add(colName + "=:" + colName);
			}else {//非主键
				if(CommonUtils.contains(fieldSet, ormCol.getFieldName())  //匹配则作为条件
						|| CommonUtils.contains(fieldSet, colName)){
					if(value == null) {
						if(hasNull) {//作为null条件
							whereList.add(colName + " is null ");
						}
					}else {
						whereList.add(colName + "=:" + colName);
					}
				}else {//要修改的字段
					if(value == null) {
						if(!hasNull) continue;//false不修改null
						if(!ormCol.isNullable()){//不能为空
							error(-2,colName + "字段,"+ ormCol.getFieldName()+"域不能为空 !");
							return null;
						}
					}
					fieldList.add(colName + "=:" + colName);
				}
			}
			paramMap.put(colName, value);
		}
		if(fieldList.isEmpty()){
			error(-1, "没有需要更改的字段 !");
			return null;//主键为空，返回-1
		}
		//生成sql语句
		StringBuilder sb = new StringBuilder("update " + getSchemaVal() + " set ");
		sb.append(String.join(",", fieldList));
		sb.append(" where " + String.join(" and ", whereList));
		return new OrmBean(sb.toString(),paramMap);
	}
	
	//查询语句封装
	private void wrapSelectSql(StringBuilder sb,Map<String,Object> params,Object obj) {
		List<String> fieldList = new ArrayList<>(),whereList = new ArrayList<>();
		for(OrmColumn ormCol : collist){
			Object value = ormCol.getValue(obj);
			String colName = ormCol.getColName();
			//确定为表中字段
			boolean extCol = ormCol.isExtCol();
			String formulaSQL = ormCol.getFormulaSQL();//伪列
			if(StringUtils.isEmpty(formulaSQL) && extCol) continue;//扩展列，继续
			if(StringUtils.isEmpty(formulaSQL)){
				fieldList.add("t." + colName);
				//添加where条件
				if(value != null) {
					params.put(colName, value);
					whereList.add("t." + colName + "=:" + colName);
				}
			}else{//伪列处理
				if(ormCol.isOnlyOne()){//加入一条显示
					fieldList.add(wrapFormulaSql(formulaSQL,colName));
				}else{
					fieldList.add("( " + formulaSQL + " ) as " + colName);
				}
			}
		}
		sb.append("select " + String.join(",", fieldList) 
					+ " from " + getSchemaVal() + " t ");
		if(!whereList.isEmpty()) {
			sb.append(" where " + String.join(" and ", whereList));
		}else sb.append(" where 1 = 1 ");//默认查询语句
	}
	
	/*
	 * 查询实体列表
	 * */
	@Override
	public OrmBean selectSQL(Object obj, String order,IWhereSql whereSql) {
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuilder sb = new StringBuilder();
		wrapSelectSql(sb, params, obj);
		if(whereSql != null) {
			whereSql.where(sb,params);
		}
		sb.append(" order by " + addOrderSql(order));
		return new OrmBean(sb.toString(),params);
	}

	//加入排序语句
	private String addOrderSql(String order) {
		if(StringUtils.isEmpty(order)) {
			for(OrmColumn ormCol : collist){
				if(ormCol.isId()){
					return "t." + ormCol.getColName() + " asc ";
				}
			}
		}
		return order;
	}
	
	/*实体映射删除sql语句*/
	@Override
	public OrmBean deleteSQL(Object obj) {
		//主键列表
		Map<String,Object> params = new HashMap<>();
		List<String> whereList = new ArrayList<>();
		for(OrmColumn ormCol : collist){
			//外部字段跳出
			if(ormCol.isExtCol()) continue;
			Object value = ormCol.getValue(obj);
			String colName = ormCol.getColName();
			if(value == null) continue;
			//不为Null的字段为条件
			params.put(colName, value);
			whereList.add(colName + "=:" + colName);
		}
		if(params.isEmpty()){
			error(-1,"删除条件不能为空 !");
			return null;
		}
		//生成sql语句
		StringBuilder sb = new StringBuilder();
		sb.append("delete from  " + getSchemaVal());
		sb.append(" where " + String.join(" and ", whereList));
		return new OrmBean(sb.toString(),params);
	}
	
	@Override
	public OrmBean deleteByIdSQL(Integer id) {
		try {
			Object obj = entityClass.newInstance();
			setIdValue(obj,id);
			return deleteSQL(obj);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 判断主键是否为Null
	 * */
	@Override
	public boolean idIsNull(Object obj) {
		boolean isnull = false;
		for(OrmColumn ormCol : collist){
			if(ormCol.isId()){
				if(null == ormCol.getValue(obj)){
					isnull = true;
					if(!cpkey) break;//不是复合主键则跳出
				}
			}
		}
		return isnull;
	}

	@Override
	public OrmBean selectPageSQL(PageBean<?> pb, Object obj, String order) {
		return selectPageSQL(pb,obj,order,null);
	}

	@Override
	public OrmBean selectPageSQL(PageBean<?> pb, Object obj, String order,IWhereSql whereSql) {
		Map<String,Object> params = new HashMap<String,Object>();
		StringBuilder sb = new StringBuilder();
		wrapSelectSql(sb, params, obj);
		if(whereSql != null) {
			whereSql.where(sb,params);
		}
		//总记录
		String pageSql = OrmFactory.parsePageSql(sb.toString());
		//加入分页
		wrapPageSql(pb, sb, addOrderSql(order));
		return new OrmBean(sb.toString(),pageSql,params);
	}
	/*
	 * 自定义语句分页查询
	 * */
	@Override
	public OrmBean pageSql(PageBean<?> pb, String sql, String order) {
		String pageSql = OrmFactory.parsePageSql(sql);
		StringBuilder sb = new StringBuilder(sql);
		wrapPageSql(pb, sb, order);
		return new OrmBean(sb.toString(),pageSql);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T ormBean(ResultSet rs) {
		T rowObj = null;
		String colName = null;
		try {
			Set<String> colset = getResultSetColumns(rs);
			rowObj = (T) entityClass.newInstance();
			for(OrmColumn ormcol : collist){
				if(CommonUtils.contains(colset,ormcol.getColName())){
					colName = ormcol.getColName();
					Object value = rs.getObject(colName);
					ormcol.setValue(rowObj, value);
				}
			}
		} catch (InstantiationException | IllegalAccessException | SQLException e) {
			log.error(colName + ",error:" + e.getMessage());
		} 
		return rowObj;
	}

	/*
	 * 查询个体
	 * */
	@Override
	public OrmBean selectByIdSQL(Integer id) {
		try {
			Object obj = entityClass.newInstance();
			setIdValue(obj,id);
			return selectSQL(obj,null,null);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 错误统一处理
	 * */
	private void error(int id,String info) throws RuntimeException {
		throw new RuntimeException(getSchemaVal() + ":"+ info);
	}
	
	//获取数据集合中的列
	public static  Set<String> getResultSetColumns(ResultSet rs ) {
		Set<String> colset = new HashSet<>();
    	ResultSetMetaData meta;
		try {
			meta = rs.getMetaData();
			int colCount = meta.getColumnCount();
	    	//log.debug("开始");
	    	for(int i = 1;i <= colCount;i++){//获取所有列
	    		colset.add(meta.getColumnLabel(i));
	    		//log.debug(i + "," + meta.getColumnLabel(i));
	    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	//log.debug("结束");
    	return colset;
	}
		
	/*
	 * 获取前缀表名
	 * */
	private String getSchemaVal() {
		String str = StringUtils.isEmpty(schema)?"":schema + ".";
		return str + tableName;
	}
	
}
