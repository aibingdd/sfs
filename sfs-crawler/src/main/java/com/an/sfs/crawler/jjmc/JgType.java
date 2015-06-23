package com.an.sfs.crawler.jjmc;

public class JgType {
    private int value;
    private String name;
    private int pageCount;

    public JgType() {
    }

    public JgType(int value, String name, int pageCount) {
        this.value = value;
        this.name = name;
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        return "JjType [value=" + value + ", name=" + name + ", pageCount=" + pageCount + "]";
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
