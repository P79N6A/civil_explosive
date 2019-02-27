package cn.zhiyuan.frame.web;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import cn.zhiyuan.frame.BaseDao;
import cn.zhiyuan.frame.CommonUtils;
import cn.zhiyuan.frame.MsgBean;
import cn.zhiyuan.frame.orm.OrmDBType;
import cn.zhiyuan.frame.orm.OrmFactory;

@SuppressWarnings("rawtypes")
@Service
public class OrmBiz extends BaseDao {

	@Autowired
	private OrmFactory ormFactory;
	
	JSONObject ResultSet2Json(ResultSet rs) throws SQLException{
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
	
	//获取所有表
	public List<String> getTables() {
		List<String> tablelist = new ArrayList<String>();
		Connection conn = null;
		OrmDBType ormdb = ormFactory.createOrmDB(super.getDataSource());
		if(ormdb == null) return new ArrayList<>();
		DataSource ds = ormdb.getDataSource();
		try {
			conn = ds.getConnection();
			DatabaseMetaData databaseMetaData = conn.getMetaData();
			ResultSet tableSet = null;
			if(ormdb.isOracle()){
				String schem = ormdb.getSchem();			
				tableSet = databaseMetaData.getTables(null, schem, "%", 
						new String[]{"TABLE"});
			}else{
				tableSet = databaseMetaData.getTables(null, "%", "%",
						new String[]{"TABLE"});
			}
			while(tableSet.next()){
				String tableName = tableSet.getString("TABLE_NAME");
				if(ormdb.isOracle()){
					if(tableName.indexOf("==$0") != -1) continue;
				}
				tablelist.add(tableName);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(null != conn){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return tablelist;
	}
	
	//转换
	public int convert(List<ConvertInfo> infolist){
		//遍历表
		for(ConvertInfo info : infolist){
			info.convert();//表转换
			List<ConvertColumn> collist = getColumnInfo(info.getTableName());
			info.setColList(collist);
			log.debug("包名：" + info.getPackName() + ",表名：" + info.getTableName()
					+ ",类名：" + info.getClassName());
		}
		saveOrmFile(infolist);
		return 0;
	}
			
	//保存
	private void saveOrmFile(List<ConvertInfo> infolist){
		//遍历表
		for(ConvertInfo info : infolist){
			AutoCreateClass acc = new AutoCreateClass(info);
			acc.saveEntityClass();
			acc.saveIBizClass();
			acc.saveBizClass();
			//acc.saveIDaoClass();
			//acc.saveDaoClass();
			acc.saveActionClass();

			//acc.saveJspView();
			//acc.saveEditJspView();
			acc.saveJavaScript();
			//acc.saveLayoutXml();
		}
	}
		
	//获取表中所有列信息
	private List<ConvertColumn> getColumnInfo(String tableName) {
		List<ConvertColumn> colList = new ArrayList<>();
		OrmDBType ormdb = ormFactory.createOrmDB(super.getDataSource());
		if(ormdb == null) return colList;
		DatabaseMetaData databaseMetaData = null;
		Connection conn = null;
		DataSource ds = ormdb.getDataSource();
		try {
			conn = ds.getConnection();
			databaseMetaData = conn.getMetaData();
			ResultSet columnSet = null;
			if(ormdb.isOracle()){
				columnSet = databaseMetaData.getColumns(null, ormdb.getSchem(), 
						tableName, "%");
			}else{
				columnSet = databaseMetaData.getColumns(null, "%", tableName, "%");
			}
			//获取所有列
			while(columnSet.next()){
				//log.debug(columnSet.getString("TABLE_CAT") +","+ columnSet.getString("TABLE_SCHEM"));
				ConvertColumn col = new ConvertColumn();
				String colName = columnSet.getString("COLUMN_NAME");
				col.setColName(colName);
				col.setComment(columnSet.getString("REMARKS"));
				String typeName = columnSet.getString("TYPE_NAME").toLowerCase();
				col.setColType(typeName);
				//log.debug(colName + "=" +columnSet.getInt("DATA_TYPE") +"="+ typeName);
				//是否为空
				col.setNullable("YES".equals(columnSet.getString("IS_NULLABLE")));
				if(ormdb.isOracle()){
					col.setAutoIncrement(false);
				}else if(ormdb.isMssql()){//判断是否主键
					if(typeName.endsWith(" identity"))
						col.setAutoIncrement(true);//自增
				}else{
					col.setAutoIncrement("YES".equals(columnSet.getString("IS_AUTOINCREMENT")));
				}
				col.convert();//表列--》 字段的转换
				colList.add(col);
			}
			//获取主键
			ResultSet pkCols = null;
			if(ormdb.isOracle()){
				pkCols = databaseMetaData.getPrimaryKeys(null, ormdb.getSchem(),
						tableName);
			}else if(ormdb.isMssql()){
				pkCols = databaseMetaData.getPrimaryKeys(null, "dbo", tableName);
			}else{
				pkCols = databaseMetaData.getPrimaryKeys(null, "%", tableName);
			}
			if(pkCols == null) return colList;
			while(pkCols.next()){
				//test(pkCols);
				String colName = pkCols.getString("COLUMN_NAME");
				log.debug(colName + "主键");
				for(ConvertColumn col : colList){
					if(colName.equals(col.getColName())){
						col.setID(true);//设置为主键
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(null != conn){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return colList;
	}
	
	/*重启服务*/
	public void restart(String warPath,String appName,String servName,
			String winCmd,String linuxCmd) {
		Runtime r = Runtime.getRuntime();
		String tomcatRoot = CommonUtils.getAppPath(null);
		String cmd = null;
		String[] cmds = null;
		if(CommonUtils.isWindows()) {//windows系统
			cmd = winCmd;
			cmds = new String[]{"wscript", cmd,tomcatRoot,warPath,servName,appName};
		}else {
			cmd = "/bin/sh " + linuxCmd;
			cmd += " \"" + tomcatRoot + "\" " + warPath + " " + appName;
			cmds = new String[]{"/bin/sh","-c", cmd + " > /dev/null&"};
		}
		log.debug(cmd + "," + tomcatRoot + "," + warPath 
				+ "," + servName 
				+ "," + appName);
		try {
			r.exec(cmds);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//转存war文件
	public  String writeWarFile(MsgBean mbean,MultipartFile warfile) {
		String fileName = warfile.getOriginalFilename();
		String extName = CommonUtils.getExtName(fileName);
		if(".war".equals(extName) == false) {
			mbean.setRetInfo(-2, "文件格式不对");
			return null;
		}
		//生成文件名称
		fileName = fileName.replaceAll(extName, "");
		String newFileName = fileName 
				+ CommonUtils.date2str(new Date(), "_yyyyMMdd_HHmmss")
				+ extName;
		String warpath = File.separator + "temp" + File.separator  + newFileName;
		String filePath = CommonUtils.getAppPath(warpath);
		log.debug(filePath);
		try {
			warfile.transferTo(new File(filePath));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return warpath;
	}
	
	// 首字母大写,且当第二个字母为小写时。Yuanwen 20150719 20:58
	public static String captureName(String name) {
		if(StringUtils.isEmpty(name)) return "";
		char[] cs = name.toCharArray();
		int strlen = cs.length;
		if (strlen == 0)
			return name;
		else if (1 == strlen) {
			if (Character.isLowerCase(cs[0]))
				cs[0] -= 32;
		} else {
			if (Character.isLowerCase(cs[0]) && Character.isLowerCase(cs[1]))
				cs[0] -= 32;
		}
		return String.valueOf(cs);
	}

	// 首字母小写
	public static String firstToLowerCase(String name) {
		char[] cs = name.toCharArray();
		if (Character.isUpperCase(cs[0]))
			cs[0] += 32;
		return String.valueOf(cs);
	}

	// 名字转换
	public static String changeName(String srcName, boolean flag) {
		if (StringUtils.isEmpty(srcName))
			return srcName;
		StringBuilder strBuild = new StringBuilder();
		for (String name : srcName.toLowerCase().split("_")) {
			if (flag) {// 首单词中的首字母，不需要转换大写 如 类属性
				flag = false;
				strBuild.append(name);
				continue;
			}
			strBuild.append(captureName(name));
		}
		return strBuild.toString();
	}
}
