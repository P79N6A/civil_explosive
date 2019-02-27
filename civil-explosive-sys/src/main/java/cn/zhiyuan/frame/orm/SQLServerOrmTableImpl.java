package cn.zhiyuan.frame.orm;

import cn.zhiyuan.frame.PageBean;

public class SQLServerOrmTableImpl extends AbstractOrmTable {

	@Override
	public OrmBean autoIdSQL() {
		OrmBean ormbean = new OrmBean();
		ormbean.setSql("SELECT @@IDENTITY ");
		return ormbean;
	}

	@Override
	protected StringBuilder wrapPageSql(PageBean<?> pb, StringBuilder sb, String order) {
		sb.insert(0,"Select * From (Select ROW_NUMBER() Over (order By "
				+ order +") As rowNum, * From ( ");
			sb.append(" ) As TT ) As N Where rowNum > " + pb.getOffset() 
			+" And rowNum <= "+ (pb.getPageSize() + pb.getOffset()) );
		return sb;
	}

	@Override
	protected String wrapFormulaSql(String formulaSQL, String colName) {
		return "( " + formulaSQL.replace("select", "select top 1 ") + " ) as " + colName;
	}
	
}
