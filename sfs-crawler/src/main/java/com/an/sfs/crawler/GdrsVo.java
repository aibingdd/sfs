package com.an.sfs.crawler;

public class GdrsVo implements Comparable<GdrsVo> {
    private String code;
    private String date;
    private int count;
    private float countChangeRate;
    private int floatStock;
    private float fsChangeRate;
    private float stockPrice;
    private int holdStockPrice;
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
                + ", floatStock=" + floatStock + ", fsChangeRate=" + fsChangeRate + ", stockPrice=" + stockPrice
                + ", holdStockPrice=" + holdStockPrice + ", top10StockRate=" + top10StockRate
                + ", top10FloatStockRate=" + top10FloatStockRate + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + count;
        result = prime * result + Float.floatToIntBits(countChangeRate);
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + floatStock;
        result = prime * result + Float.floatToIntBits(fsChangeRate);
        result = prime * result + holdStockPrice;
        result = prime * result + Float.floatToIntBits(stockPrice);
        result = prime * result + Float.floatToIntBits(top10FloatStockRate);
        result = prime * result + Float.floatToIntBits(top10StockRate);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GdrsVo other = (GdrsVo) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (count != other.count)
            return false;
        if (Float.floatToIntBits(countChangeRate) != Float.floatToIntBits(other.countChangeRate))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (floatStock != other.floatStock)
            return false;
        if (Float.floatToIntBits(fsChangeRate) != Float.floatToIntBits(other.fsChangeRate))
            return false;
        if (holdStockPrice != other.holdStockPrice)
            return false;
        if (Float.floatToIntBits(stockPrice) != Float.floatToIntBits(other.stockPrice))
            return false;
        if (Float.floatToIntBits(top10FloatStockRate) != Float.floatToIntBits(other.top10FloatStockRate))
            return false;
        if (Float.floatToIntBits(top10StockRate) != Float.floatToIntBits(other.top10StockRate))
            return false;
        return true;
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

    public float getFsChangeRate() {
        return fsChangeRate;
    }

    public void setFsChangeRate(float fsChangeRate) {
        this.fsChangeRate = fsChangeRate;
    }

    public float getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(float stockPrice) {
        this.stockPrice = stockPrice;
    }

    public int getHoldStockPrice() {
        return holdStockPrice;
    }

    public void setHoldStockPrice(int holdStockPrice) {
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
