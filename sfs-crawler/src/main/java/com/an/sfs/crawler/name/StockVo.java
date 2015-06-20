package com.an.sfs.crawler.name;

public class StockVo {
    // 1-SH, 2-SZ, 3-ChuangYe
    private int type;
    private String code;
    private String name;

    public String getCodeSuffix() {
        String codeSuffix = "";
        if (isSh()) {
            codeSuffix = "01";
        } else if (isSz()) {
            codeSuffix = "02";
        }
        return codeSuffix;
    }

    public String getTypeStr() {
        if (isSh()) {
            return "sh";
        } else if (isSz()) {
            return "sz";
        }
        return "";
    }

    public boolean isSh() {
        return type == 1;
    }

    public boolean isSz() {
        return type == 2 || type == 3;
    }

    public StockVo() {
    }

    public StockVo(int type, String code, String name) {
        this.type = type;
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return "StockVo [type=" + type + ", code=" + code + ", name=" + name + "]";
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
