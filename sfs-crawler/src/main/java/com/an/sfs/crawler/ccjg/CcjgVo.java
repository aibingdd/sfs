package com.an.sfs.crawler.ccjg;

public class CcjgVo {
    private String date;
    private String stock;
    private String jg;
    private long count;

    public CcjgVo(String date, String stock, String jg, long count) {
        this.date = date;
        this.stock = stock;
        this.jg = jg;
        this.count = count;
    }

    @Override
    public String toString() {
        return "CcjgVo [date=" + date + ", stock=" + stock + ", jg=" + jg + ", count=" + count + "]";
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getJg() {
        return jg;
    }

    public void setJg(String jg) {
        this.jg = jg;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
