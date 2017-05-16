package com.an.sfs.crawler.gdyj;

import com.an.sfs.crawler.tdx.StockLoader;

public class GdrsReportVo {
    private String code;
    private String rank;
    private String name;
    private String countRateStr;//
    private String gdrsNum;
    private String netFloatShare;

    public GdrsReportVo() {
    }

    public String getDisplayCode() {
        String url = "<a href=\"http://f10.eastmoney.com/f10_v2/ShareholderResearch.aspx?code=%s%s\">%s</a>";
        String displayCode = String.format(url, StockLoader.getTypeStr(code), code, code);
        return displayCode;
    }

    @Override
    public String toString() {
        return "GdrsReportVo [code=" + code + ", rank=" + rank + ", name=" + name + ", countRateStr=" + countRateStr
                + ", gdrsNum=" + gdrsNum + ", netFloatShare=" + netFloatShare + "]";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountRateStr() {
        return countRateStr;
    }

    public void setCountRateStr(String countRateStr) {
        this.countRateStr = countRateStr;
    }

    public String getGdrsNum() {
        return gdrsNum;
    }

    public void setGdrsNum(String gdrsNum) {
        this.gdrsNum = gdrsNum;
    }

    public String getNetFloatShare() {
        return netFloatShare;
    }

    public void setNetFloatShare(String netFloatShare) {
        this.netFloatShare = netFloatShare;
    }
}
