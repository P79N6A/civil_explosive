package cn.zhiyuan.ces.contract.entity;

import javax.persistence.*;


@Table(name="project_alert_dist")
public class ProjectAlertDist {
	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*核定安全距离(m)
	*/
	@Column(name = "safe_distance",nullable=false)
	private Integer safeDistance;

	/**
	*方位
	*/
	@Column(name = "orientation")
	private String orientation;

	/**
	*被保护对象名称
	*/
	@Column(name = "by_protection_name")
	private String byProtectionName;

	/**
	*所属项目
	*/
	@Column(name = "project_id",nullable=false)
	private Integer projectId;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation=orientation;
	}

	public String getByProtectionName() {
		return byProtectionName;
	}

	public void setByProtectionName(String byProtectionName) {
		this.byProtectionName=byProtectionName;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId=projectId;
	}

	public Integer getSafeDistance() {
		return safeDistance;
	}

	public void setSafeDistance(Integer safeDistance) {
		this.safeDistance = safeDistance;
	}

}
