package cn.zhiyuan.frame.orm;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.stereotype.Component;

import cn.zhiyuan.frame.AbstractDao;

/*
 * AbstractOrm类的实例工厂
 * */
@Component
public class OrmFactory {

	private Map<String,OrmDBType> dbtypeMap = new HashMap<>();
	
	@SuppressWarnings("rawtypes")
	public IOrmTable createORM(DataSource dataSource,Class<? extends AbstractDao> tClass){
		OrmDBType ormdb = createOrmDB(dataSource);
		AbstractOrmTable orm = null;
		if(ormdb.isMysql()) {
			orm = new MysqlOrmTableImpl();
			Class<?> ormClass = getOrmBean(tClass);
			if(ormClass == null) return orm;
			orm.init(ormdb, ormClass);
		}
		return orm;
	}
	
	public OrmDBType createOrmDB(DataSource dataSource){
		String code = dataSource.hashCode() + "";
		OrmDBType ormdb = dbtypeMap.get(code);
		if(ormdb == null){
			ormdb = new OrmDBType(dataSource);
			dbtypeMap.put(code,ormdb);
		}
		return ormdb;
	}
	
	
	private Class<?> getOrmBean(Class<?> tClass) {
		Type type = tClass.getGenericSuperclass();
		if(type instanceof ParameterizedType == false) return null;
		ParameterizedType pt = (ParameterizedType)type;
		Type[] tt = pt.getActualTypeArguments();
		if(tt.length > 0 && tt[0] instanceof Class) {
			return (Class<?>)tt[0];
		}
		return null;
	}
	
	
	/*
	 * sql生成解析分页语句
	 * */
	public static String parsePageSql(String sql){
		String [] sqls = sql.split("\\s+");
		int flag = 0;
		StringBuilder newSql = new StringBuilder("select count(1) from");
		for(String s :sqls){
			if(flag == -1){
				newSql.append(" " + s);
			}else{
				if("select".equalsIgnoreCase(s) || "(select".equalsIgnoreCase(s)) flag++;
				else if("from".equals(s)) {
					if(1 == flag){
						flag = -1;
					}else flag--;
				}
			}
		}
		return newSql.toString();
	}
	
}
