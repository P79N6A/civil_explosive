package cn.zhiyuan.frame.web;

import java.util.*;


/*
 * 表转换类信息
 */
public class ConvertInfo {

	//包名
	private  String packName;
	
	//类名
	private String className; 
	//表名
	private String tableName;
	//注解
	private String comment;
	
	//保存路径
	private String savePath;
	
	/*
	 * 表名转换为类名  Yuanwen  
	 */
	public void convert(){
		//字段名
		className = OrmBiz.changeName(tableName,false);
	}
	
	//或者主键名称，若组合主键，则随机返回一个
	public String getIdName(){
		for(ConvertColumn col : colList){
			if(col.isID()) return col.getFieldName();
		}
		return null;
	}
	
	//字段列表
	private List<ConvertColumn> colList;

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public List<ConvertColumn> getColList() {
		return colList;
	}

	public void setColList(List<ConvertColumn> colList) {
		this.colList = colList;
	}
	
}
