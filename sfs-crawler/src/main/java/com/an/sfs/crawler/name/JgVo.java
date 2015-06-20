package com.an.sfs.crawler.name;

public class JgVo {
    private String code;
    private String name;
    //
    private int type;

    /**
     * @param code
     * @param typeStr
     * @param name
     */
    public JgVo(String code, int type, String name) {
        this.code = code;
        this.name = name;
        this.type = type;
    }

    /**
     * @param typeStr
     * @return
     */
    public static int getType(String typeStr) {
        if (typeStr.equals("基金")) {
            return 1;
        } else if (typeStr.equals("QFII")) {
            return 2;
        } else if (typeStr.equals("社保")) {
            return 3;
        } else if (typeStr.equals("保险")) {
            return 4;
        } else if (typeStr.equals("券商")) {
            return 5;
        } else if (typeStr.equals("信托")) {
            return 6;
        }
        System.out.println("Unknown type " + typeStr);
        return -1;
    }

    @Override
    public String toString() {
        return "JgVo [code=" + code + ", name=" + name + ", type=" + type + "]";
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
