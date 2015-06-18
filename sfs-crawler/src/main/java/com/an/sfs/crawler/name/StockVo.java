package com.an.sfs.crawler.name;

public class StockVo {
    // 1-SH, 2-SZ, 3-ChuangYe
    private int type;
    private String code;
    private String name;

    public boolean isSh() {
        return type == 1;
    }

    public boolean isSz() {
        return type == 2 || type == 3;
    }

    /**
     * @return
     */
    public String getTypeStr() {
        if (type == 1) {
            return "sh";
        } else if (type == 2 || type == 3) {
            return "sz";
        }
        return null;
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
