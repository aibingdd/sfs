package com.an.sfs.crawler.fhrz;

/**
 * Zeng fang ming xi
 */
public class ZfmxVo implements Comparable<ZfmxVo> {
    private String code;
    private String increaseTime;
    private String onSaleTime;
    private long num;
    private long amount;
    private float price;
    private String type;

    public String getDisplayStr() {
        return increaseTime + " " + type + " ￥" + price + " " + (amount / 10000) + "万";
    }

    @Override
    public int compareTo(ZfmxVo o) {
        if (this.increaseTime.equals(o.increaseTime)) {
            return this.code.compareTo(o.code);
        }
        return this.increaseTime.compareTo(o.increaseTime) * -1;
    }

    @Override
    public String toString() {
        return "ZfmxVo [code=" + code + ", increaseTime=" + increaseTime + ", onSaleTime=" + onSaleTime + ", num="
                + num + ", amount=" + amount + ", price=" + price + ", type=" + type + "]";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIncreaseTime() {
        return increaseTime;
    }

    public void setIncreaseTime(String increaseTime) {
        this.increaseTime = increaseTime;
    }

    public String getOnSaleTime() {
        return onSaleTime;
    }

    public void setOnSaleTime(String onSaleTime) {
        this.onSaleTime = onSaleTime;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
