package cn.zhiyuan.frame.orm;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/*
 * 实体映射的字段信息
 */
public class OrmColumn {

	protected static Log log = LogFactory.getLog(OrmColumn.class);
	
	private String colName;
	
	private String fieldName;
	
	//是否主键
	private boolean  isId;
	
	//自增字段
	private boolean autoFlag;
	
	//是否伪列，即本类映射的表中没有此字段,true是,false否
	private boolean extCol;
		
	//序列名称
	private String sequence;
	
	private Field field;
	
	//是否可为空 true 可为空，false不能为空
	private boolean nullable ;

	//子查询时，是否只查询一条
	private boolean onlyOne;
	//扩展列
	private boolean extFlag;
	//内嵌SQL语句
	private String formulaSQL;
	
	//获取实体的值
	public Object getValue(Object obj){
		try {
			if(obj == null) return null;
			//log.debug(fieldName);
			if(PropertyUtils.isReadable(obj, fieldName))
				return PropertyUtils.getProperty(obj, fieldName);
		} catch (NoClassDefFoundError | Exception e) {
			log.debug(obj +","+ fieldName +","+ e.getMessage());
		} 
		return null;
	}
	
	//设置实体的值
	public void setValue(Object obj,Object value){
		try {
			if(value == null || field == null) return;
			Class<?> c = field.getType();
			//log.debug(fieldName + "," + value.getClass() 
				//+ "," + field.getType() + "," + value);
			if(value instanceof Long){//Long 转换为 Int or short
				if(c.equals(Integer.class)){
					value = ((Long)value).intValue();
				}else if(c.equals(Short.class))
					value = ((Long)value).shortValue();
			}else if(value instanceof Short){
				if(c.equals(Integer.class))
					value = ((Short)value).intValue();
			}else if(value instanceof Double){
				if(c.equals(Float.class))
					value = ((Double)value).floatValue();
				else if(c.equals(String.class)){
					value = value.toString();
				}
			}else if(value instanceof Integer){
				if(c.equals(Long.class))
					value = ((Integer)value).longValue();
				else if(c.equals(Boolean.class)) {
					value = ((Integer)value) == 0?false:true;
				}
			}else if(value instanceof BigDecimal){
				if(c.equals(Double.class)){
					value = ((BigDecimal)value).doubleValue();
				}else if(c.equals(String.class)){
					value = value.toString();
				}
			}else if(value instanceof String){
				if(c.equals(Double.class)){
					value = Double.parseDouble(value.toString());
				}
			}else if(value instanceof Boolean) {
				if(c.equals(Byte.class)){
					value = (byte)((Boolean)value?1:0);
				}else if(c.equals(Integer.class)) {
					value =  (int)((Boolean)value?1:0);
				}
			}else if(value instanceof Byte) {
				if(c.equals(Integer.class)){
					value = new Integer(value.toString());
				}
			}
			PropertyUtils.setProperty(obj, fieldName, value);
		} catch (NoClassDefFoundError | Exception e) {
			log.error(fieldName +","+ e.getMessage());
		}
	}
		
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

	public boolean isId() {
		return isId;
	}

	public void setId(boolean isId) {
		this.isId = isId;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}


	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public boolean isOnlyOne() {
		return onlyOne;
	}

	public void setOnlyOne(boolean onlyOne) {
		this.onlyOne = onlyOne;
	}

	public String getFormulaSQL() {
		return formulaSQL;
	}

	public void setFormulaSQL(String formulaSQL) {
		this.formulaSQL = formulaSQL;
	}

	public boolean isExtCol() {
		return extCol;
	}

	public void setExtCol(boolean extCol) {
		this.extCol = extCol;
	}

	public boolean isExtFlag() {
		return extFlag;
	}

	public void setExtFlag(boolean extFlag) {
		this.extFlag = extFlag;
	}

	public boolean isAutoFlag() {
		return autoFlag;
	}

	public void setAutoFlag(boolean autoFlag) {
		this.autoFlag = autoFlag;
	}

}
