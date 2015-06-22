package com.an.sfs.crawler.gsgk;

public class GsgkVo {
    private String code;
    private String name;
    private String industry;
    private String createDate;
    private String publicDate;

    /**
     * @param date
     * @return
     */
    public boolean isPublicAfter(String date) {
        return publicDate.compareTo(date) > 0;
    }

    /**
     * @param code
     * @param name
     * @param industry
     * @param createDate
     * @param publicDate
     */
    public GsgkVo(String code, String name, String industry, String createDate, String publicDate) {
        this.code = code;
        this.name = name;
        this.industry = industry;
        this.createDate = createDate;
        this.publicDate = publicDate;
    }

    @Override
    public String toString() {
        return "GsgkVo [code=" + code + ", name=" + name + ", industry=" + industry + ", createDate=" + createDate
                + ", publicDate=" + publicDate + "]";
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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPublicDate() {
        return publicDate;
    }

    public void setPublicDate(String publicDate) {
        this.publicDate = publicDate;
    }
}
