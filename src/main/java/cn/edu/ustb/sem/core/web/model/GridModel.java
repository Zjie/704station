package cn.edu.ustb.sem.core.web.model;

import java.util.List;

public class GridModel<T> {
	private List<T> items;
	private int totalNum;
	//起始页
	private int page;
	//页大小 默认20个
	private int limit = 20;
	//起始下标
	private int start;
	public List<T> getItems() {
		return items;
	}
	public void setItems(List<T> items) {
		this.items = items;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	
}
