package cn.zhiyuan.ces.contract.entity;

import javax.persistence.*;


@Table(name="project_construction_scheme")
public class ProjectConstructionScheme {
	/**
	*审核人
	*/
	@Column(name = "auditor_id",nullable=false)
	private Integer auditorId;

	/**
	*炮口个数
	*/
	@Column(name = "gun_muzzle_count")
	private Integer gunMuzzleCount;

	/**
	*设计人
	*/
	@Column(name = "designer_id",nullable=false)
	private Integer designerId;

	/**
	*所属项目
	*/
	@Column(name = "project_id",nullable=false)
	private Integer projectId;

	/**
	*批准人
	*/
	@Column(name = "approver_id",nullable=false)
	private Integer approverId;

	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "scheme_id",nullable=false)
	private Integer schemeId;

	/**
	*雷管数量
	*/
	@Column(name = "detonator_dosage")
	private Integer detonatorDosage;

	/**
	*一次性最大起爆药量
	*/
	@Column(name = "max_explosive_dosage")
	private Integer maxExplosiveDosage;

	/**
	*炸药数量
	*/
	@Column(name = "explosive_dosage")
	private Integer explosiveDosage;



	public Integer getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Integer auditorId) {
		this.auditorId=auditorId;
	}

	public Integer getGunMuzzleCount() {
		return gunMuzzleCount;
	}

	public void setGunMuzzleCount(Integer gunMuzzleCount) {
		this.gunMuzzleCount=gunMuzzleCount;
	}

	public Integer getDesignerId() {
		return designerId;
	}

	public void setDesignerId(Integer designerId) {
		this.designerId=designerId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId=projectId;
	}

	public Integer getApproverId() {
		return approverId;
	}

	public void setApproverId(Integer approverId) {
		this.approverId=approverId;
	}

	public Integer getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(Integer schemeId) {
		this.schemeId=schemeId;
	}

	public Integer getDetonatorDosage() {
		return detonatorDosage;
	}

	public void setDetonatorDosage(Integer detonatorDosage) {
		this.detonatorDosage=detonatorDosage;
	}

	public Integer getMaxExplosiveDosage() {
		return maxExplosiveDosage;
	}

	public void setMaxExplosiveDosage(Integer maxExplosiveDosage) {
		this.maxExplosiveDosage=maxExplosiveDosage;
	}

	public Integer getExplosiveDosage() {
		return explosiveDosage;
	}

	public void setExplosiveDosage(Integer explosiveDosage) {
		this.explosiveDosage=explosiveDosage;
	}

}
