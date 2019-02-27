package cn.zhiyuan.frame.orm;

import java.util.List;

import cn.zhiyuan.frame.PageBean;

public class OracleOrmTableImpl extends AbstractOrmTable {

	@Override
	public OrmBean autoIdSQL() {
		OrmBean ormbean = new OrmBean();
		ormbean.setSql("select "  + super.getIdSequence() + ".nextval from dual ");
		return ormbean;
	}

	@Override
	protected StringBuilder wrapPageSql(PageBean<?> pb, StringBuilder sb, String order) {
		sb.insert(0, "SELECT * FROM ( SELECT A.*, ROWNUM RN FROM ( ");
		sb.append(" ) A WHERE ROWNUM <=  " + (pb.getPageSize() 
				+ pb.getOffset())  +" ) WHERE RN >= " + pb.getOffset());
		return sb;
	}
	
	//主键sql语句
	protected void wrapIdSql(List<String> fieldList,List<String> valueSql,String colName) {
		fieldList.add(colName);
		valueSql.add(super.getIdSequence() + ".nextval");
		return;
	}

	@Override
	protected String wrapFormulaSql(String formulaSQL, String colName) {
		return "( " + formulaSQL.replace("where", "where rownum = 1 and ") + " ) as " + colName;
	}

}
