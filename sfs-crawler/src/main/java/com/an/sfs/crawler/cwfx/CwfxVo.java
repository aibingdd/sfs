package com.an.sfs.crawler.cwfx;

public class CwfxVo implements Comparable<CwfxVo> {
    private String code;
    private String date;
    private long income;
    private long profit;
    //
    private float incomeRate;
    private float profitRate;

    public CwfxVo() {
    }

    @Override
    public int compareTo(CwfxVo o) {
        if (!code.equals(code)) {
            return this.getCode().compareTo(o.getCode());
        }
        return this.getDate().compareTo(o.getDate()) * -1;
    }

    @Override
    public String toString() {
        return "CwfxVo [code=" + code + ", date=" + date + ", income=" + income + ", profit=" + profit
                + ", incomeRate=" + incomeRate + ", profitRate=" + profitRate + "]";
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

    public long getIncome() {
        return income;
    }

    public void setIncome(long income) {
        this.income = income;
    }

    public long getProfit() {
        return profit;
    }

    public void setProfit(long profit) {
        this.profit = profit;
    }

    public float getIncomeRate() {
        return incomeRate;
    }

    public void setIncomeRate(float incomeRate) {
        this.incomeRate = incomeRate;
    }

    public float getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(float profitRate) {
        this.profitRate = profitRate;
    }
}
