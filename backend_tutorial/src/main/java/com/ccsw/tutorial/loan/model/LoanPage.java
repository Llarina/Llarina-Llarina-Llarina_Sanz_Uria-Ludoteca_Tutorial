package com.ccsw.tutorial.loan.model;

import java.util.List;

public class LoanPage {

    private List<LoanDto> content;
    private long totalElements;
    private int pageNumber;
    private int pageSize;

    public LoanPage(List<LoanDto> content, long totalElements, int pageNumber, int pageSize) {
        this.content = content;
        this.totalElements = totalElements;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public List<LoanDto> getContent() {
        return content;
    }

    public void setContent(List<LoanDto> content) {
        this.content = content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}