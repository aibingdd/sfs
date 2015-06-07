package com.an.sfs.crawler.fhrz;

public class RzVo {
    private String code;
    private String increasetime;
    private String onSaleTime;
    private long num;
    private long amount;
    private float price;
    private String type;

    public String getDisplayStr() {
        return increasetime + " " + type + " ￥" + price + " " + (amount / 10000) + "万";
    }

    public RzVo() {
    }

    @Override
    public String toString() {
        return "RzVo [code=" + code + ", increasetime=" + increasetime + ", onSaleTime=" + onSaleTime + ", num=" + num
                + ", amount=" + amount + ", price=" + price + ", type=" + type + "]";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIncreasetime() {
        return increasetime;
    }

    public void setIncreasetime(String increasetime) {
        this.increasetime = increasetime;
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
