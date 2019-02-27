package cn.zhiyuan.frame.orm;

import java.lang.reflect.Field;

import cn.zhiyuan.ces.base.entity.WorkingPerson;
import cn.zhiyuan.frame.PageBean;

public class MysqlOrmTableImpl extends AbstractOrmTable {

	@Override
	public OrmBean autoIdSQL() {
		OrmBean ormbean = new OrmBean();
		ormbean.setSql("select last_insert_id()");
		return ormbean;
	}

	@Override
	protected StringBuilder wrapPageSql(PageBean<?> pb, StringBuilder sb, String order) {
		sb.append(" order by " + order);
		sb.append(" limit " + pb.getOffset() +","+ pb.getPageSize());
		return sb;
	}

	@Override
	protected String wrapFormulaSql(String formulaSQL, String colName) {
		return "( " + formulaSQL + " limit 1) as " +  colName;
	}
	
	public static void main(String[] args) {
		Field[] fields = getClassFields(WorkingPerson.class);
		System.out.println(fields.length);
		for(Field field : fields) {
			System.out.println(field.getName());
		}
	}
	
}
