package com.an.sfs.crawler.jjmc;

public class JjmcVo implements Comparable<JjmcVo> {
    private int type;
    private String code;
    private String name;

    public String getStr() {
        return this.type + "," + this.code + "," + this.name;
    }

    public JjmcVo() {
    }

    public JjmcVo(int type, String code, String name) {
        this.type = type;
        this.code = code;
        this.name = name;
    }

    public String getDisplayStr() {
        return type + ", " + code + ", " + name;
    }

    @Override
    public int compareTo(JjmcVo o) {
        if (this.type == o.type) {
            return this.code.compareTo(o.code);
        }
        return this.type - o.type;
    }

    @Override
    public String toString() {
        return "JjVo [type=" + type + ", code=" + code + ", name=" + name + "]";
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
