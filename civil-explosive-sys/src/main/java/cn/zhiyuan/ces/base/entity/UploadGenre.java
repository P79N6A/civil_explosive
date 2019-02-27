package cn.zhiyuan.ces.base.entity;

import javax.persistence.*;

import cn.zhiyuan.frame.orm.Formula;


@Table(name="upload_genre")
public class UploadGenre {
	
	//文档类型名称
	@Formula(name="type_code_name",value="select tt.code_name from sys_code tt "
			+ " where tt.code_value=t.type_code and tt.code_type= '9' ")
	private String typeCodeName;
		
	/**
	*文档类型
	*/
	@Column(name = "type_code",nullable=false)
	private String typeCode;

	/**
	*名称
	*/
	@Column(name = "name",nullable=false)
	private String name;

	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;



	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode=typeCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public String getTypeCodeName() {
		return typeCodeName;
	}

	public void setTypeCodeName(String typeCodeName) {
		this.typeCodeName = typeCodeName;
	}

}
