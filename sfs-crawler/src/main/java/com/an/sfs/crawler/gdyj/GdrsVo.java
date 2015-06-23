package com.an.sfs.crawler.gdyj;

public class GdrsVo implements Comparable<GdrsVo> {
    private String date;
    private String code;
    private int shareholderCount;
    private int circulationStockCountPer;
    private float stockPrice;
    private float sdgdRate;
    private float sdltgdRate;

    private float shareholderCountChangeRate;
    private float circulationStockCountPerChangeRate;

    @Override
    public String toString() {
        return "GdrsVo [date=" + date + ", code=" + code + ", shareholderCount=" + shareholderCount
                + ", circulationStockCountPer=" + circulationStockCountPer + ", stockPrice=" + stockPrice
                + ", sdgdRate=" + sdgdRate + ", sdltgdRate=" + sdltgdRate + ", shareholderCountChangeRate="
                + shareholderCountChangeRate + ", circulationStockCountPerChangeRate="
                + circulationStockCountPerChangeRate + "]";
    }

    @Override
    public int compareTo(GdrsVo o) {
        if (this.date.equals(o.date)) {
            return this.code.compareTo(o.code);
        }
        return this.date.compareTo(o.date) * -1;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getShareholderCount() {
        return shareholderCount;
    }

    public void setShareholderCount(int shareholderCount) {
        this.shareholderCount = shareholderCount;
    }

    public int getCirculationStockCountPer() {
        return circulationStockCountPer;
    }

    public void setCirculationStockCountPer(int circulationStockCountPer) {
        this.circulationStockCountPer = circulationStockCountPer;
    }

    public float getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(float stockPrice) {
        this.stockPrice = stockPrice;
    }

    public float getSdgdRate() {
        return sdgdRate;
    }

    public void setSdgdRate(float sdgdRate) {
        this.sdgdRate = sdgdRate;
    }

    public float getSdltgdRate() {
        return sdltgdRate;
    }

    public void setSdltgdRate(float sdltgdRate) {
        this.sdltgdRate = sdltgdRate;
    }

    public float getShareholderCountChangeRate() {
        return shareholderCountChangeRate;
    }

    public void setShareholderCountChangeRate(float shareholderCountChangeRate) {
        this.shareholderCountChangeRate = shareholderCountChangeRate;
    }

    public float getCirculationStockCountPerChangeRate() {
        return circulationStockCountPerChangeRate;
    }

    public void setCirculationStockCountPerChangeRate(float circulationStockCountPerChangeRate) {
        this.circulationStockCountPerChangeRate = circulationStockCountPerChangeRate;
    }
}
