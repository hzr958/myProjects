package com.smate.center.searcher.util;

import java.util.Collections;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.SolrResultPage;

/**
 * Solr 分页结果类
 * 
 * @author 斌少
 * 
 */
public class SolrPage<T> {

	public static final int MIN_PAGESIZE = 10;
	protected Integer pageNo = 1;// 页号
	protected Integer pageSize = MIN_PAGESIZE;// 每页大小
	protected Integer pageCount = 0;// 总页数
	protected Long total = 0L;// 总记录数
	protected Page<T> result = new SolrResultPage<T>(Collections.<T> emptyList());

	public SolrPage(Integer pageNo, Integer pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Page<T> getResult() {
		return result;
	}

	public void setResult(Page<T> result) {
		this.result = result;
	}

}
