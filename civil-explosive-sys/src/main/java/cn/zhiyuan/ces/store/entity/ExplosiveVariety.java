package cn.zhiyuan.ces.store.entity;

import javax.persistence.*;

import cn.zhiyuan.frame.orm.Formula;


@Table(name="explosive_variety")
public class ExplosiveVariety {
	
	
	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*所属类别
	*/
	@Column(name = "category_id",nullable=false)
	private Integer categoryId;

	/**
	*所属类别名称
	*/
	@Formula(name = "category_name",value="select tt.name from explosive_category tt "
			+ "where tt.id = t.category_id")
	private String categoryName;
	
	/**
	*品种规格
	*/
	@Column(name = "specification")
	private String specification;

	/**
	*品种代码
	*/
	@Column(name = "code")
	private String code;
	
	/**
	*品种名称
	*/
	@Column(name = "name",nullable=false)
	private String name;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId=categoryId;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification=specification;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
