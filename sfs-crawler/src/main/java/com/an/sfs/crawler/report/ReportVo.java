package com.an.sfs.crawler.report;

public class ReportVo {
    private String index;
    private String code;// Stock Code or Industry Code
    private String name;// Stock Name or Industry Name

    // Return on Net Assets
    private String rona;

    // Return on Total Assets
    private String rota;

    // debt to assets ratio
    private String dtar;

    // Price / Earning Per Share
    private String pe;

    // Book Value Per share
    private String pb;

    // JiGouChiCang
    private String jgcc;

    private String note;

    public ReportVo() {
    }

    @Override
    public String toString() {
        return "ReportVo [index=" + index + ", code=" + code + ", name=" + name + ", rona=" + rona + ", rota=" + rota
                + ", dtar=" + dtar + ", pe=" + pe + ", pb=" + pb + ", jgcc=" + jgcc + ", note=" + note + "]";
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
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

    public String getRona() {
        return rona;
    }

    public void setRona(String rona) {
        this.rona = rona;
    }

    public String getRota() {
        return rota;
    }

    public void setRota(String rota) {
        this.rota = rota;
    }

    public String getDtar() {
        return dtar;
    }

    public void setDtar(String dtar) {
        this.dtar = dtar;
    }

    public String getPe() {
        return pe;
    }

    public void setPe(String pe) {
        this.pe = pe;
    }

    public String getPb() {
        return pb;
    }

    public void setPb(String pb) {
        this.pb = pb;
    }

    public String getJgcc() {
        return jgcc;
    }

    public void setJgcc(String jgcc) {
        this.jgcc = jgcc;
    }
}
