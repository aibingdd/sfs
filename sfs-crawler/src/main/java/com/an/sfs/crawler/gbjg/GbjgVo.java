package com.an.sfs.crawler.gbjg;

public class GbjgVo {
    private String code;
    private String date;
    private long total;
    private long circulation;
    private long restriction;

    /**
     * @param code
     * @param date
     * @param total
     * @param circulation
     * @param restriction
     */
    public GbjgVo(String code, String date, long total, long circulation, long restriction) {
        this.code = code;
        this.date = date;
        this.total = total;
        this.circulation = circulation;
        this.restriction = restriction;
    }

    @Override
    public String toString() {
        return "GbjgVo [code=" + code + ", date=" + date + ", total=" + total + ", circulation=" + circulation
                + ", restriction=" + restriction + "]";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getCirculation() {
        return circulation;
    }

    public void setCirculation(long circulation) {
        this.circulation = circulation;
    }

    public long getRestriction() {
        return restriction;
    }

    public void setRestriction(long restriction) {
        this.restriction = restriction;
    }
}
