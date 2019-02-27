package cn.zhiyuan.frame.orm;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/*
 * 关联子查询
 */
@Target({METHOD, FIELD}) 
@Retention(RUNTIME)
public @interface Formula {

	//子查询语句
	String value();
	
	//子查询字段别名
	String name();
	
	//是否强制加入获取第一条
	boolean onlyOne() default true;
	
}
