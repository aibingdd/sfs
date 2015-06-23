package com.an.sfs.crawler.gdyj;

import com.an.sfs.crawler.jjmc.JgTypeLoader;

public class SdltgdVo {
    private String jgName;
    private String jgType;
    private String stockType;
    private long stockCount;
    private float circulationPer;

    public boolean isJg() {
        if (JgTypeLoader.isJg(jgName)) {
            return true;
        }
        return false;
    }

    public SdltgdVo(String jgName, String jgType, String stockType, long stockCount, float circulationPer) {
        this.jgName = jgName;
        this.jgType = jgType;
        this.stockType = stockType;
        this.stockCount = stockCount;
        this.circulationPer = circulationPer;
    }

    @Override
    public String toString() {
        return "SdltgdVo [jgName=" + jgName + ", jgType=" + jgType + ", stockType=" + stockType + ", stockCount="
                + stockCount + ", circulationPer=" + circulationPer + "]";
    }

    public String getJgName() {
        return jgName;
    }

    public void setJgName(String jgName) {
        this.jgName = jgName;
    }

    public String getJgType() {
        return jgType;
    }

    public void setJgType(String jgType) {
        this.jgType = jgType;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public long getStockCount() {
        return stockCount;
    }

    public void setStockCount(long stockCount) {
        this.stockCount = stockCount;
    }

    public float getCirculationPer() {
        return circulationPer;
    }

    public void setCirculationPer(float circulationPer) {
        this.circulationPer = circulationPer;
    }
}
