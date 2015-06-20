package com.an.sfs.crawler.gsgk;

public class GsgkVo {
    private String code;
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
     * @param industry
     * @param createDate
     * @param publicDate
     */
    public GsgkVo(String code, String industry, String createDate, String publicDate) {
        this.code = code;
        this.industry = industry;
        this.createDate = createDate;
        this.publicDate = publicDate;
    }

    @Override
    public String toString() {
        return "GsgkVo [code=" + code + ", industry=" + industry + ", createDate=" + createDate + ", publicDate="
                + publicDate + "]";
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
