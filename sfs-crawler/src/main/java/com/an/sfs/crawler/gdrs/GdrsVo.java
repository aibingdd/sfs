package com.an.sfs.crawler.gdrs;

public class GdrsVo implements Comparable<GdrsVo> {
    private String code;
    private String date;
    private int count;
    private float countChangeRate;
    private int floatStock;
    private float floatStockChangeRate;
    private float stockPrice;
    private long holdStockPrice;
    private float top10StockRate;
    private float top10FloatStockRate;

    public GdrsVo() {
    }

    @Override
    public int compareTo(GdrsVo o) {
        if (!code.equals(code)) {
            return this.getCode().compareTo(o.getCode());
        }
        return this.getDate().compareTo(o.getDate()) * -1;
    }

    @Override
    public String toString() {
        return "GdrsVo [code=" + code + ", date=" + date + ", count=" + count + ", countChangeRate=" + countChangeRate
                + ", floatStock=" + floatStock + ", floatStockChangeRate=" + floatStockChangeRate + ", stockPrice="
                + stockPrice + ", holdStockPrice=" + holdStockPrice + ", top10StockRate=" + top10StockRate
                + ", top10FloatStockRate=" + top10FloatStockRate + "]";
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getCountChangeRate() {
        return countChangeRate;
    }

    public void setCountChangeRate(float countChangeRate) {
        this.countChangeRate = countChangeRate;
    }

    public int getFloatStock() {
        return floatStock;
    }

    public void setFloatStock(int floatStock) {
        this.floatStock = floatStock;
    }

    public float getFloatStockChangeRate() {
        return floatStockChangeRate;
    }

    public void setFloatStockChangeRate(float floatStockChangeRate) {
        this.floatStockChangeRate = floatStockChangeRate;
    }

    public float getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(float stockPrice) {
        this.stockPrice = stockPrice;
    }

    public long getHoldStockPrice() {
        return holdStockPrice;
    }

    public void setHoldStockPrice(long holdStockPrice) {
        this.holdStockPrice = holdStockPrice;
    }

    public float getTop10StockRate() {
        return top10StockRate;
    }

    public void setTop10StockRate(float top10StockRate) {
        this.top10StockRate = top10StockRate;
    }

    public float getTop10FloatStockRate() {
        return top10FloatStockRate;
    }

    public void setTop10FloatStockRate(float top10FloatStockRate) {
        this.top10FloatStockRate = top10FloatStockRate;
    }
}
