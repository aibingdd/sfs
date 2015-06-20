package com.an.sfs.crawler.gsgk;

public class GsgkVo {
    private String code;
    private String createDate;
    private String publicDate;

    /**
     * @param code
     * @param createDate
     * @param publicDate
     */
    public GsgkVo(String code, String createDate, String publicDate) {
        this.code = code;
        this.createDate = createDate;
        this.publicDate = publicDate;
    }

    @Override
    public String toString() {
        return "GsgkVo [code=" + code + ", createDate=" + createDate + ", publicDate=" + publicDate + "]";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
