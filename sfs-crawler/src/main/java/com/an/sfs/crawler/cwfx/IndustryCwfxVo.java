package com.an.sfs.crawler.cwfx;

public class IndustryCwfxVo {
    private String code;
    private String industry;
    // Return on net assets
    private float rona;
    // Return on total assets
    private float rota;

    public IndustryCwfxVo() {
    }

    @Override
    public String toString() {
        return "IndustryCwfxVo [code=" + code + ", industry=" + industry + ", rona=" + rona + ", rota=" + rota + "]";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
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
}
