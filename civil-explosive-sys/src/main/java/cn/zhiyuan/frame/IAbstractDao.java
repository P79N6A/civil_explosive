package cn.zhiyuan.frame;

import java.util.List;

import cn.zhiyuan.frame.orm.builder.IWhereSql;

public interface IAbstractDao<T> {

	/*
	 * 保存
	 * 根据主键判断新增 or 更新
	 */
	int save(T obj);
	
	/*
	 * 新建,null字段忽略
	 * 返回改变的行数
	 */
	int add(T obj);
	
	/*
	 * 新建,null字段有效
	 * 返回改变的行数
	 */
	int addWithNull(T obj);
	
	/*
	 * 新建,null字段忽略,自增主键写入实体类中
	 * 返回改变的行数
	 */
	int addAndId(T obj);
	
	/*
	 * 新建,null字段有效,自增主键写入实体类中
	 * 返回改变的行数
	 */
	int addAndIdWithNull(T obj);
	
	/*
	 * 更新，根据主键修改
	 * null字段忽略
	 * 返回改变的行数
	 */
	int update(T obj);
	
	/*
	 * 更新，根据主键修改
	 * null字段同样修改
	 * 返回改变的行数
	 */
	int updateWithNull(T obj);
	
	/*
	 * 更新，根据field条件进行修改,field条件不能为空 !
	 * null字段不修改
	 * 返回改变的行数
	 */
	int update(T obj,String field);
	
	/*
	 * 更新，根据field条件进行修改,field条件不能为空 !
	 * null字段同样修改
	 * 返回改变的行数
	 */
	int updateWithNull(T obj,String field);
	
	/*
	 * 实体删除，不为null的字段做为条件且条件不能为空 !
	 * 返回改变的行数
	 */
	int delete(T obj);
	
	/*
	 * 通过主键删除
	 * 返回改变的行数
	 */
	int delete(Integer id);
	
	/*
	 * 查询个体,主键作为条件，且不能为空
	 */
	T get(Integer id);
	
	/*
	 * 查询个体,不为Null的字段作为条件且条件不能为空 !
	 * 若查询出来多条，则默认取第一条 !
	 * 若无记录，返回null
	 */
	T get(T obj);
	
	/*=================无分页查询列表==================*/
	/*
	 * 功能继承:List<T> getList(T obj);
	 * 加入排序参数
	 */
	List<T> getList(T obj,String order);
		
	/*
	 * 功能继承:List<T> getList(T obj);
	 * 加入排序参数
	 */
	List<T> getList(T obj,String order,IWhereSql whereSql);
	/*=================分页查询列表==================*/
	/*
	 * 分页查询列表,不为Null的字段作为条件且条件可以为空,默认排序
	 * 若无记录，返回长度为0的List
	 */
	List<T> getPageList(PageBean<T> pb,T obj,String order);
	/*
	 * 分页查询列表,不为Null的字段作为条件且条件可以为空,默认排序
	 * 若无记录，返回长度为0的List
	 */
	List<T> getPageList(PageBean<T> pb,T obj,String order,IWhereSql whereSql);

}
