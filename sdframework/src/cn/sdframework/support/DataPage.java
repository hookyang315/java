package cn.sdframework.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataPage<T> {
	private int currentPage;
	private int pageSize;
	private int totalRecords;
	private int totalPages;
	private List<Map<String,Object>> origData = new ArrayList<Map<String,Object>>();
	
	private List<T> pageData = new ArrayList<T>();
	
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalRecords() {
		return totalRecords;
	}
	public List<Map<String, Object>> getOrigData() {
		return origData;
	}
	public void setOrigData(List<Map<String, Object>> origData) {
		this.origData = origData;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
	public List<T> getPageData() {
		return pageData;
	}
	public void setPageData(List<T> pageData) {
		this.pageData = pageData;
	}
}
