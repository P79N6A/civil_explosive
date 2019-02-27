package cn.zhiyuan.frame;

import java.util.ArrayList;
import java.util.List;

/*
 * 前台后台分页参数类
 */
public class PageBean<T> extends MsgBean {

	public PageBean(){}
	
	public PageBean(int pageSize){
		this.pageSize = pageSize;
	}
	
	private List<T> rows = new ArrayList<>();
	
		
	//记录总数
	private Integer total;
	//页数
	private Integer pageNum;
	//行数
	private Integer pageSize = 20;
	
	//是否分页
	private Boolean isPage = Boolean.TRUE;
	//排序字段
	private String sort;
	//升序降序
	private String order;
	
	//获取偏移量
    public Integer getOffset() {
    	if(pageNum == null || pageSize == null)
    		return 0;
		if(pageNum < 1) pageNum = 1;
		return (pageNum - 1)* pageSize;
	}

  //获取总页数
  	public Integer getTotalPage() {
  		if(total == null || pageSize == null) return null;
  		int num = total / pageSize;
  		return total % pageSize == 0?num:num+1;
  	}
    
	public Integer getTotal() {
		return total;
	}


	public void setTotal(Integer total) {
		this.total = total;
	}


	public Integer getPageNum() {
		return pageNum;
	}


	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}


	public Integer getPageSize() {
		return pageSize;
	}


	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}


	public List<T> getRows() {
		return rows;
	}


	public void setRows(List<T> rows) {
		this.rows = rows;
	}


	public Boolean getIsPage() {
		return isPage;
	}


	public void setIsPage(Boolean isPage) {
		this.isPage = isPage;
	}

	public String getSort() {
		return sort;
	}


	public void setSort(String sort) {
		this.sort = sort;
	}


	public String getOrder() {
		return order;
	}


	public void setOrder(String order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "PageBean [rows=" + rows + ", total=" + total + ", pageNum=" + pageNum + ", pageSize=" + pageSize
				+ ", isPage=" + isPage + ", sort=" + sort + ", order=" + order + "]";
	}

	
}
