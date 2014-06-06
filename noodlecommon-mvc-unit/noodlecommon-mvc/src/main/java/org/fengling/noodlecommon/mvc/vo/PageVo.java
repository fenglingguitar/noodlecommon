package org.fengling.noodlecommon.mvc.vo;

import java.util.ArrayList;
import java.util.List;

public class PageVo<T> {

    /**
     * 默认分页
     */
    public static final long DEFAULT_PAGE_SIZE = 20;

    /**
     * 本页容量
     */
    private long pageSize = DEFAULT_PAGE_SIZE;

    /**
     * 起始位置
     */
    private long start;

    /**
     * 数据List
     */
    private List<T> data;

    /**
     * 总记录数
     */
    private long totalCount;

    /**
     * 
     */
    public PageVo() {
        this(0, 0, DEFAULT_PAGE_SIZE, new ArrayList<T>());
    }

    /**
     * 
     * @param start    		起始位置
     * @param totalCount	总记录数
     * @param pageSize   	本页容量
     * @param data     		本页条数
     */
    public PageVo(long start, long totalCount, long pageSize, List<T> data) {
        this.start 			= start;
        this.totalCount 	= totalCount;
        this.pageSize 		= pageSize;
        this.data 			= data;
    }
    
    /**
     * 
     * @param totalCount	总记录数
     * @param data     		本页条数
     */
    public PageVo(long totalCount, List<T> data) {
        this.totalCount 	= totalCount;
        this.data 			= data;
    }

    /**
     * 起始位置
     */
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}

	/**
     * 总记录数
     */
    public long getTotalCount() {
        return totalCount;
    }
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
    
	/**
     * 本页容量
     */
    public long getPageSize() {
        return pageSize;
    }
	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}
	
	/**
     * 数据List
     */
	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}	
	
	/**
     * @return 总页数
     */
    public long getTotalPageCount() {
        if ((totalCount % pageSize) == 0) {
            return totalCount / pageSize;
        } else {
            return (totalCount / pageSize) + 1;
        }
    }

    /**
     * @return 当前页数
     */
    public long getCurrentPageNo() {
        return (start / pageSize) + 1;
    }


    /**
     * @return 是否有后页
     */
    public boolean hasNextPage() {
        return this.getCurrentPageNo() < this.getTotalPageCount();
    }

    /**
     * @return 是否有前页
     */
    public boolean hasPreviousPage() {
        return this.getCurrentPageNo() > 1;
    }

    /**
     * @return 起始位置(默认容量)
     */
    protected static long getStartOfPage(long pageNo) {
        return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
    }

    /**
     * @return 起始位置
     */
    public static long getStartOfPage(long pageNo, long pageSize) {
        return (pageNo - 1) * pageSize;
    }

    /**
     * @return 最后一条起始位置(默认容量)
     */
    public long getEndOfPage(long pageNo, long pageSize){
    	if(this.getTotalCount()==0)
    		return 0;
    	if(pageNo == this.getTotalPageCount()){
    		return (long)this.getTotalCount()-1;
    	}else{
    		return getStartOfPage(pageNo+1,pageSize)-1;
    	}
    }
}
