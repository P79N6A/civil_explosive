package cn.zhiyuan.ces.sys.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name="sys_menu")
public class SysMenu {
	
	/**
	*过滤规则
	*/
	@Column(name = "filter_rule")
	private String filterRule;

	/**
	*
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_id",nullable=false)
	private Integer menuId;

	/**
	*地址
	*/
	@Column(name = "menu_url")
	private String menuUrl;

	/**
	*名称
	*/
	@Column(name = "menu_name",nullable=false)
	private String menuName;

	/**
	*窗口高度
	*/
	@Column(name = "menu_height")
	private Integer menuHeight;

	/**
	*父级菜单
	*/
	@Column(name = "parent_id")
	private Integer parentId;

	/**
	*菜单类型
	*/
	@Column(name = "menu_type")
	private String menuType;

	/**
	*顺序号
	*/
	@Column(name = "order_num")
	private Integer orderNum;

	/**
	*窗口宽度
	*/
	@Column(name = "menu_width")
	private Integer menuWidth;

	/**
	*
	*/
	@Column(name = "remark")
	private String remark;

	public SysMenu() {
		
	}

	public SysMenu(Integer id, Integer order) {
		this.menuId = id;
		this.orderNum = order;
	}

	public String getFilterRule() {
		return filterRule;
	}

	public void setFilterRule(String filterRule) {
		this.filterRule=filterRule;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId=menuId;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl=menuUrl;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName=menuName;
	}

	public Integer getMenuHeight() {
		return menuHeight;
	}

	public void setMenuHeight(Integer menuHeight) {
		this.menuHeight=menuHeight;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId=parentId;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType=menuType;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum=orderNum;
	}

	public Integer getMenuWidth() {
		return menuWidth;
	}

	public void setMenuWidth(Integer menuWidth) {
		this.menuWidth=menuWidth;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark=remark;
	}

}
