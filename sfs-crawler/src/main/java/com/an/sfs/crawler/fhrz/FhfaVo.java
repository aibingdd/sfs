package com.an.sfs.crawler.fhrz;

public class FhfaVo {
    private String date;
    // 10送1.00转0.50派0.30元 -> 1.5
    private float increase;
    // 派0.45 -> 0.45
    private float bonus;

    public FhfaVo() {
    }

    @Override
    public String toString() {
        return "FhfaVo [date=" + date + ", increase=" + increase + ", bonus=" + bonus + "]";
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getIncrease() {
        return increase;
    }

    public void setIncrease(float increase) {
        this.increase = increase;
    }

    public float getBonus() {
        return bonus;
    }

    public void setBonus(float bonus) {
        this.bonus = bonus;
    }
}
