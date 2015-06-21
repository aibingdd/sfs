package com.an.sfs.crawler.name;

public class IndustryVo {
    private String code;
    private String industry;

    /**
     * @param code
     * @param industry
     */
    public IndustryVo(String code, String industry) {
        this.code = code;
        this.industry = industry;
    }

    @Override
    public String toString() {
        return "IndustryVo [code=" + code + ", industry=" + industry + "]";
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

}
