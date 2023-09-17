package com.yjntc.excelexporter.req;

/**
 *  分页查询
 * @author wangkangsheng
 * @email 1164461842@qq.com
 * @create 2022-04-11 09:02
 */
public class PageReq {
    private static final int MAX_PAGE_SIZE = 1_000;
    /**
     * 当前页
     */
    private Integer currentPage;
    /**
     * 每页数据量
     */
    private Integer pageSize;

    /**
     * 预留 模糊搜索字段
     */
    private String keyWords;

    // 导出不限制查询页数
    private Boolean noLimiter = true;


    public PageReq(Integer currentPage, Integer pageSize, String keyWords) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.keyWords = keyWords;
    }

    public PageReq() {
    }

    public Integer getCurrentPage() {
        if (null == this.currentPage || this.currentPage < 1){
            return 1;
        }
        return this.currentPage;
    }

    public Integer getPageSize(boolean limiter) {
        if (null == this.pageSize || this.pageSize < 1){
            return 10;
        }
        if (!Boolean.TRUE.equals(noLimiter) && limiter && pageSize > MAX_PAGE_SIZE){
            return MAX_PAGE_SIZE;
        }
        return this.pageSize;
    }

    public Integer getPageSize() {
        return getPageSize(true);
    }

    public String getKeyWords() {
        return this.keyWords;
    }

    public PageReq setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public PageReq setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PageReq setKeyWords(String keyWords) {
        this.keyWords = keyWords;
        return this;
    }

    public void setNoLimiter(Boolean noLimiter) {
        this.noLimiter = noLimiter;
    }
}
