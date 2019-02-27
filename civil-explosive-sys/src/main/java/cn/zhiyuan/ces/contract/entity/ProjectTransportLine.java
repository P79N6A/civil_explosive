package cn.zhiyuan.ces.contract.entity;

import javax.persistence.*;


@Table(name="project_transport_line")
public class ProjectTransportLine {
	/**
	*GPS类型(1国标 2百度 3高德)
	*/
	@Column(name = "gps_type")
	private String gpsType;

	/**
	*路段数
	*/
	@Column(name = "line_count")
	private Integer lineCount;

	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*路线起点
	*/
	@Column(name = "line_start")
	private String lineStart;

	/**
	*路线终点
	*/
	@Column(name = "line_end")
	private String lineEnd;

	/**
	*所属项目
	*/
	@Column(name = "project_id",nullable=false)
	private Integer projectId;



	public String getGpsType() {
		return gpsType;
	}

	public void setGpsType(String gpsType) {
		this.gpsType=gpsType;
	}

	public Integer getLineCount() {
		return lineCount;
	}

	public void setLineCount(Integer lineCount) {
		this.lineCount=lineCount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public String getLineStart() {
		return lineStart;
	}

	public void setLineStart(String lineStart) {
		this.lineStart=lineStart;
	}

	public String getLineEnd() {
		return lineEnd;
	}

	public void setLineEnd(String lineEnd) {
		this.lineEnd=lineEnd;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId=projectId;
	}

}
