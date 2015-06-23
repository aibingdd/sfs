package com.an.sfs.crawler.tdx;

public class StockVo {
    private String code;
    private float price;
    private String name;
    // XiFenHangYe
    private String industry;
    private String region;
    private String publicDate;
    private long outstandingShare;
    private long floatShare;
    private long bShare;
    private long hShare;

    public StockVo() {
    }

    @Override
    public String toString() {
        return "TStockVo [code=" + code + ", price=" + price + ", name=" + name + ", industry=" + industry
                + ", region=" + region + ", publicDate=" + publicDate + ", outstandingShare=" + outstandingShare
                + ", floatShare=" + floatShare + ", bShare=" + bShare + ", hShare=" + hShare + "]";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(String publicDate) {
        this.publicDate = publicDate;
    }

    public long getOutstandingShare() {
        return outstandingShare;
    }

    public void setOutstandingShare(long outstandingShare) {
        this.outstandingShare = outstandingShare;
    }

    public long getFloatShare() {
        return floatShare;
    }

    public void setFloatShare(long floatShare) {
        this.floatShare = floatShare;
    }

    public long getbShare() {
        return bShare;
    }

    public void setbShare(long bShare) {
        this.bShare = bShare;
    }

    public long gethShare() {
        return hShare;
    }

    public void sethShare(long hShare) {
        this.hShare = hShare;
    }
}
