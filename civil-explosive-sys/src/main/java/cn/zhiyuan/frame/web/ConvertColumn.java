package cn.zhiyuan.frame.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class ConvertColumn{

	private static Map<String,String> typeMap = new HashMap<String,String>();
	
	protected  Log log = LogFactory.getLog(ConvertColumn.class);
	
	static {
		
		typeMap.put("bit", "Boolean");
		
		typeMap.put("uniqueidentifier", "String");
		
		typeMap.put("varchar", "String");
		typeMap.put("nvarchar", "String");
		typeMap.put("char", "String");
		typeMap.put("clob", "String");
		
		typeMap.put("tinytext", "String");
		typeMap.put("text", "String");
		typeMap.put("mediumtext", "String");
		typeMap.put("longtext", "String");
		
		typeMap.put("tinyint", "Integer");
		typeMap.put("smallint", "Integer");
		typeMap.put("mediumint", "Integer");
		typeMap.put("number", "Integer");
		typeMap.put("int", "Integer");
		typeMap.put("bigint", "Long");
		
		typeMap.put("float", "Float");
		typeMap.put("double", "Double");
		
		typeMap.put("decimal", "java.math.BigDecimal");
		
		typeMap.put("datetime", "java.util.Date");
		typeMap.put("date", "java.util.Date");
		typeMap.put("time", "java.util.Date");
		typeMap.put("timestamp", "java.util.Date");
		typeMap.put("year", "java.util.Date");
		
		typeMap.put("blob", "byte[]");
		typeMap.put("longblob", "byte[]");
		
	}
	/*
	 * 表列属性转换为 类域字段
	 */
	public void convert(){
		//字段名   ----------- 表中域名 转换成 属性名称
		fieldName = OrmBiz.changeName(colName,true);
		//字段类型
		String type = colType;//.replaceAll("\\(.+\\)", "").toLowerCase();

		//fieldType = typeMap.get(type);
		for(String key : typeMap.keySet()){
			if(type.startsWith(key)){
				fieldType = typeMap.get(key);
				break;
			}
		}
		if(fieldType == null){//默认类型
			fieldType = "Object";
			log.debug(colType + "映射匹配空");
		}
	}
	
	/*
	 * 列名
	 * */
	private String colName;
	
	//字段名
	private String fieldName;
	
	//字段类型
	private String fieldType;
	
	//列类型
	private String colType;
	
	//是否为主键
	private boolean isID;
	
	//是否可为null
	private boolean nullable;
	
	//默认值
	private String defaultValue;
	
	//注解
	private String comment;
	
	//自增
	private boolean autoIncrement;

	//序号
	private Integer orderNum;
	
	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getColType() {
		return colType;
	}

	public void setColType(String colType) {
		this.colType = colType;
	}

	public boolean isID() {
		return isID;
	}

	public void setID(boolean isID) {
		this.isID = isID;
	}


	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	@Override
	public String toString() {
		return "ConvertColumn [colName=" + colName + ", fieldName=" + fieldName
				+ ", fieldType=" + fieldType + ", colType=" + colType
				+ ", isID=" + isID + ", nullable=" + nullable + ", defaultValue="
				+ defaultValue + ", comment=" + comment + ", autoIncrement="
				+ autoIncrement + "]";
	}
	
	public static String test(){
		ConvertColumn col = new ConvertColumn();
		col.setColName("menu_id");
		col.setColType("int(11)");
		col.convert();
		return col.toString();
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

}
