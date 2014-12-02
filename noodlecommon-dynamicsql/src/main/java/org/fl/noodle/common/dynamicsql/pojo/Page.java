package org.fl.noodle.common.dynamicsql.pojo;

import java.util.List;

public class Page<T> {

    private long start;
    private long totalCount;
    private long pageSize;
    private List<T> data;

    public Page(long start, long totalCount, long pageSize, List<T> data) {
        this.start 			= start;
        this.totalCount 	= totalCount;
        this.pageSize 		= pageSize;
        this.data 			= data;
    }

	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}

    public long getTotalCount() {
        return totalCount;
    }
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

    public long getPageSize() {
        return pageSize;
    }
	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}	
}
