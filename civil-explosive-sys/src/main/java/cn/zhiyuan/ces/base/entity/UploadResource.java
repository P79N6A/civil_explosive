package cn.zhiyuan.ces.base.entity;

import javax.persistence.*;


@Table(name="upload_resource")
public class UploadResource {
	/**
	*资源宿主
	*/
	@Column(name = "host_id")
	private Integer hostId;

	/**
	*资源路径
	*/
	@Column(name = "path",nullable=false)
	private String path;

	/**
	*资源大小
	*/
	@Column(name = "size",nullable=false)
	private Integer size;

	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*
	*/
	@Column(name = "remark")
	private String remark;

	/**
	*名称
	*/
	@Column(name = "name")
	private String name;

	/**
	*宿主类型
	*/
	@Column(name = "host_genre",nullable=false)
	private Integer hostGenre;

	/**
	*
	*/
	@Column(name = "file_type")
	private String fileType;



	public Integer getHostId() {
		return hostId;
	}

	public void setHostId(Integer hostId) {
		this.hostId=hostId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path=path;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size=size;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark=remark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType=fileType;
	}

	public Integer getHostGenre() {
		return hostGenre;
	}

	public void setHostGenre(Integer hostGenre) {
		this.hostGenre = hostGenre;
	}

}
