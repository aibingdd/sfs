package com.an.sfs.crawler.name;

public class IndustryVo {
    private String code;
    private String name;

    /**
     * @param code
     * @param name
     */
    public IndustryVo(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return "TIndustryVo [code=" + code + ", name=" + name + "]";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
