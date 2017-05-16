package com.an.sfs.crawler.gdyj;

import com.an.sfs.crawler.util.FileUtil;

public class GdrsSortVo implements Comparable<GdrsSortVo> {
    public static final int SORT_BY_DOWN_RATE = 1;
    public static final int SORT_BY_GDRS_NUMBER = 2;

    private int sortType = -1;
    private String code;
    private float countRatio;
    // float * 100000000L
    private long countRateL;
    private int gdrsNum;

    public String getCountRateStr() {
        return FileUtil.PERCENT_FORMAT.format(countRatio);
    }

    public GdrsSortVo(int sortType) {
        this.sortType = sortType;
    }

    /**
     * @param code
     * @param countRatio
     */
    public void setValues(String code, float countRatio) {
        this.code = code;
        this.countRatio = countRatio;
        this.countRateL = (long) (countRatio * 100000000L);
    }

    /**
     * @param code
     * @param gdrsNum
     */
    public void setValues(String code, int gdrsNum) {
        this.code = code;
        this.gdrsNum = gdrsNum;
    }

    @Override
    public String toString() {
        return "GdrsSortVo [code=" + code + ", countRatio=" + countRatio + ", countRateL=" + countRateL + "]";
    }

    @Override
    public int compareTo(GdrsSortVo o) {
        if (sortType == SORT_BY_DOWN_RATE) {
            if (this.countRatio > o.countRatio) {
                return 1;
            } else if (this.countRatio < o.countRatio) {
                return -1;
            } else {
                return 0;
            }
        } else if (sortType == SORT_BY_GDRS_NUMBER) {
            if (this.gdrsNum > o.gdrsNum) {
                return 1;
            } else if (this.gdrsNum < o.gdrsNum) {
                return -1;
            } else {
                return 0;
            }
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

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public int getGdrsNum() {
        return gdrsNum;
    }

    public void setGdrsNum(int gdrsNum) {
        this.gdrsNum = gdrsNum;
    }
}
