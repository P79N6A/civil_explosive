package cn.zhiyuan.ces.store.entity;

import java.util.Date;

import javax.persistence.*;

import cn.zhiyuan.ces.base.entity.AuditBean;
import cn.zhiyuan.frame.orm.Formula;


@Table(name="explosive_store")
public class ExplosiveStore extends AuditBean{
	
	/**
	*所属单位名称
	*/
	@Formula(name = "unit_name",value="select tt.short_name from working_unit tt "
			+ " where tt.id= t.unit_id")
	private String unitName;
	
	//安全评估单位名称
	@Formula(name="safe_grade_name",value="select tt.name from working_unit tt where tt.id = t.safe_grade_id")
	private String safeGradeName;
	
	//仓库类型名称
	@Formula(name="store_type_name",value="select tt.code_name from sys_code tt"
			+ " where tt.code_value = t.store_type and tt.code_type='f' ")
	private String storeTypeName;
		
	/**
	*主键
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable=false)
	private Integer id;

	/**
	*仓库名称
	*/
	@Column(name = "name",nullable=false)
	private String name;
		
	/**
	*所属单位
	*/
	@Column(name = "unit_id",nullable=false)
	private Integer unitId;
	/**
	*核定容量
	*/
	@Column(name = "standard_capacity",nullable=false)
	private Integer standardCapacity;

	/**
	*监控探头数量
	*/
	@Column(name = "monitor_camera_count")
	private Integer monitorCameraCount;

	/**
	*是否入侵报警(0否1是)
	*/
	@Column(name = "alarm_inbreak")
	private String alarmInbreak;

	/**
	*安全评级单位
	*/
	@Column(name = "safe_grade_id",nullable=false)
	private Integer safeGradeId;

	/**
	*安全评价有效期
	*/
	@Column(name = "safe_grade_end_date",nullable=false)
	@com.alibaba.fastjson.annotation.JSONField(format="yyyy-MM-dd")
	private Date safeGradeEndDate;

	/**
	*1炸药类2雷管类
	*/
	@Column(name = "store_type",nullable=false)
	private String storeType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id=id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name=name;
	}

	public Integer getStandardCapacity() {
		return standardCapacity;
	}

	public void setStandardCapacity(Integer standardCapacity) {
		this.standardCapacity=standardCapacity;
	}

	public Integer getMonitorCameraCount() {
		return monitorCameraCount;
	}

	public void setMonitorCameraCount(Integer monitorCameraCount) {
		this.monitorCameraCount=monitorCameraCount;
	}

	public String getAlarmInbreak() {
		return alarmInbreak;
	}
	
	public String getAlarmInbreakName() {
		if (null == alarmInbreak)return null;
		else if("1".equals(alarmInbreak)) return "是";
		else  if("0".equals(alarmInbreak)) return "否";
		else return null;
	}

	public void setAlarmInbreak(String alarmInbreak) {
		this.alarmInbreak=alarmInbreak;
	}

	public Integer getSafeGradeId() {
		return safeGradeId;
	}

	public void setSafeGradeId(Integer safeGradeId) {
		this.safeGradeId=safeGradeId;
	}

	public Date getSafeGradeEndDate() {
		return safeGradeEndDate;
	}

	public void setSafeGradeEndDate(Date safeGradeEndDate) {
		this.safeGradeEndDate = safeGradeEndDate;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType=storeType;
	}

	public String getSafeGradeName() {
		return safeGradeName;
	}

	public void setSafeGradeName(String safeGradeName) {
		this.safeGradeName = safeGradeName;
	}

	public String getStoreTypeName() {
		return storeTypeName;
	}

	public void setStoreTypeName(String storeTypeName) {
		this.storeTypeName = storeTypeName;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

}
