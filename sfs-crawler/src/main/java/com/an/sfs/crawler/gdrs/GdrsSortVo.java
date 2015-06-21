package com.an.sfs.crawler.gdrs;

import com.an.sfs.crawler.FileUtil;

public class GdrsSortVo implements Comparable<GdrsSortVo> {
    private String code;
    private float countRatio;
    // float * 100000000L
    private long countRateL;

    public String getCountRateStr() {
        return FileUtil.PERCENT_FORMAT.format(countRatio);
    }

    /**
     * @param code
     * @param countRatio
     */
    public GdrsSortVo(String code, float countRatio) {
        this.code = code;
        this.countRatio = countRatio;
        this.countRateL = (long) (countRatio * 100000000L);
    }

    @Override
    public String toString() {
        return "GdrsSortVo [code=" + code + ", countRatio=" + countRatio + ", countRateL=" + countRateL + "]";
    }

    @Override
    public int compareTo(GdrsSortVo o) {
        if (this.countRatio > o.countRatio) {
            return 1;
        } else if (this.countRatio < o.countRatio) {
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

    public float getCountRatio() {
        return countRatio;
    }

    public void setCountRatio(float countRatio) {
        this.countRatio = countRatio;
    }

    public long getCountRateL() {
        return countRateL;
    }

    public void setCountRateL(long countRateL) {
        this.countRateL = countRateL;
    }
}
