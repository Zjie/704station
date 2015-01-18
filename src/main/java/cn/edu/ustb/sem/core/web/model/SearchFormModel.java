package cn.edu.ustb.sem.core.web.model;

public class SearchFormModel {
	protected int totalNum;
	//起始页
	protected int page;
	//页大小 默认20个
	protected int limit = 20;
	//起始下标
	protected int start;
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
