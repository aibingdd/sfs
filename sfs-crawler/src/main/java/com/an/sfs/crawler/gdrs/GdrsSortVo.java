package com.an.sfs.crawler.gdrs;

import java.text.DecimalFormat;

public class GdrsSortVo implements Comparable<GdrsSortVo> {
    private String code;
    private float countRate;
    // float * 100000000L
    private long countRateL;
    private static final DecimalFormat df = new DecimalFormat("##.0%");

    public String getCountRateStr() {
        return df.format(countRate);
    }

    /**
     * @param code
     * @param countRate
     */
    public GdrsSortVo(String code, float countRate) {
        this.code = code;
        this.countRate = countRate;
        this.countRateL = (long) (countRate * 100000000L);
    }

    @Override
    public String toString() {
        return "GdrsSortVo [code=" + code + ", countRate=" + countRate + ", countRateL=" + countRateL + "]";
    }

    @Override
    public int compareTo(GdrsSortVo o) {
        if (this.countRate > o.countRate) {
            return 1;
        } else if (this.countRate < o.countRate) {
            return -1;
        } else {
            return 0;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getCountRate() {
        return countRate;
    }

    public void setCountRate(float countRate) {
        this.countRate = countRate;
    }

    public long getCountRateL() {
        return countRateL;
    }

    public void setCountRateL(long countRateL) {
        this.countRateL = countRateL;
    }
}
