package com.an.sfs.crawler.cwfx;

public class CwfxVo implements Comparable<CwfxVo> {
    private String code;
    private String date;
    private long income;
    private long profit;
    // Return on net assets
    private float rona;
    // Return on total assets
    private float rota;
    // debt to assets ratio
    private float dtar;
    // Primary Earnings Per Share
    private float peps;
    // Net assets per share
    private float naps;

    //
    private float incomeChangeRate;
    private float profitChangeRate;
    private float ronaChangeRate;
    private float rotaChangeRate;
    private float dtarChangeRate;

    @Override
    public int compareTo(CwfxVo o) {
        if (!code.equals(code)) {
            return this.getCode().compareTo(o.getCode());
        }
        return this.getDate().compareTo(o.getDate()) * -1;
    }

    @Override
    public String toString() {
        return "CwfxVo [code=" + code + ", date=" + date + ", income=" + income + ", profit=" + profit + ", rona="
                + rona + ", rota=" + rota + ", dtar=" + dtar + ", peps=" + peps + ", naps=" + naps
                + ", incomeChangeRate=" + incomeChangeRate + ", profitChangeRate=" + profitChangeRate
                + ", ronaChangeRate=" + ronaChangeRate + ", rotaChangeRate=" + rotaChangeRate + ", dtarChangeRate="
                + dtarChangeRate + "]";
    }

    public float getRonaChangeRate() {
        return ronaChangeRate;
    }

    public void setRonaChangeRate(float ronaChangeRate) {
        this.ronaChangeRate = ronaChangeRate;
    }

    public float getRotaChangeRate() {
        return rotaChangeRate;
    }

    public void setRotaChangeRate(float rotaChangeRate) {
        this.rotaChangeRate = rotaChangeRate;
    }

    public float getDtarChangeRate() {
        return dtarChangeRate;
    }

    public void setDtarChangeRate(float dtarChangeRate) {
        this.dtarChangeRate = dtarChangeRate;
    }

    public float getPeps() {
        return peps;
    }

    public void setPeps(float peps) {
        this.peps = peps;
    }

    public float getNaps() {
        return naps;
    }

    public void setNaps(float naps) {
        this.naps = naps;
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

    public float getRona() {
        return rona;
    }

    public void setRona(float rona) {
        this.rona = rona;
    }

    public float getRota() {
        return rota;
    }

    public void setRota(float rota) {
        this.rota = rota;
    }

    public float getDtar() {
        return dtar;
    }

    public void setDtar(float dtar) {
        this.dtar = dtar;
    }

    public float getIncomeChangeRate() {
        return incomeChangeRate;
    }

    public void setIncomeChangeRate(float incomeChangeRate) {
        this.incomeChangeRate = incomeChangeRate;
    }

    public float getProfitChangeRate() {
        return profitChangeRate;
    }

    public void setProfitChangeRate(float profitChangeRate) {
        this.profitChangeRate = profitChangeRate;
    }
}
