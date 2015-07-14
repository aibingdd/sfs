package com.an.sfs.crawler.tdx;

import com.an.sfs.crawler.util.FileUtil;

public class StockVo {
    private String code;
    private float price;
    private long totalVolume;
    private String name;
    private float pe;
    // XiFenHangYe
    private String industry;
    private String region;
    private String publicDate;
    private long outstandingShare;
    private long floatShare;

    public String getPeDisplayStr() {
        return FileUtil.FLOAT_FORMAT.format(pe);
    }

    public String getIndustryDisplay() {
        String displayName = industry;
        if (displayName.length() < 4) {
            String prefix = "";
            for (int j = 0; j < 4 - displayName.length(); j++) {
                prefix += "&nbsp&nbsp&nbsp&nbsp";
            }
            displayName = prefix + displayName;
        }
        return displayName;
    }

    public boolean isSuspend() {
        return totalVolume == 0;
    }

    @Override
    public String toString() {
        return "StockVo [code=" + code + ", price=" + price + ", totalVolume=" + totalVolume + ", name=" + name
                + ", pe=" + pe + ", industry=" + industry + ", region=" + region + ", publicDate=" + publicDate
                + ", outstandingShare=" + outstandingShare + ", floatShare=" + floatShare + "]";
    }

    public float getPe() {
        return pe;
    }

    public void setPe(float pe) {
        this.pe = pe;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(long totalVolume) {
        this.totalVolume = totalVolume;
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
}
