package com.an.sfs.crawler.yjyg;

import com.an.sfs.crawler.SfsConf;

public class YjygVo implements Comparable<YjygVo> {
    private String code;
    private String name;
    private String date;
    // 1-预增,2-预盈,3-预减,4-持平,5-预警,6-预亏,7-减亏
    private int type;
    private String range;
    private String note;

    private int sign;
    private float rangeVal;

    public String getDisp() {
        return code + ", " + this.date + ", " + this.getTypeStr() + ", " + this.range + ", " + name + ", " + this.note;
    }

    public boolean isZeng() {
        return type == 1;
    }

    public boolean isYing() {
        return type == 2;
    }

    public boolean isJian() {
        return type == 3;
    }

    public boolean isChiping() {
        return type == 4;
    }

    public boolean isJing() {
        return type == 5;
    }

    public boolean iskui() {
        return type == 6 || type == 7;
    }

    private void intRangeVal() {
        if (this.range.trim().isEmpty()) {
            this.sign = 0;
            this.rangeVal = -9999;
        } else {
            String str = this.range;
            if (this.range.startsWith("-")) {
                this.sign = -1;
                str = str.substring(1);
            }
            int idx = str.indexOf("%");
            if (idx != -1) {
                str = str.substring(0, idx);
                if (!str.isEmpty()) {
                    try {
                        this.rangeVal = Float.parseFloat(str);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @param typeStr
     * @return
     */
    public static int getType(String typeStr) {
        if (typeStr.equals("预增")) {
            return 1;
        } else if (typeStr.equals("预盈")) {
            return 2;
        } else if (typeStr.equals("预减")) {
            return 3;
        } else if (typeStr.equals("持平")) {
            return 4;
        } else if (typeStr.equals("预警")) {
            return 5;
        } else if (typeStr.equals("预亏")) {
            return 6;
        } else if (typeStr.equals("减亏")) {
            return 7;
        }
        System.out.println("Unknown type: " + typeStr);
        return -1;
    }

    public String getTypeStr() {
        if (type == 1) {
            return "预增";
        } else if (type == 2) {
            return "预盈";
        } else if (type == 3) {
            return "预减";
        } else if (type == 4) {
            return "持平";
        } else if (type == 5) {
            return "预警";
        } else if (type == 6) {
            return "预亏";
        } else if (type == 7) {
            return "减亏";
        }
        return "Unknown";
    }

    public YjygVo() {
    }

    /**
     * @param code
     * @param name
     * @param date
     * @param type
     * @param range
     * @param note
     */
    public YjygVo(String code, String name, String date, int type, String range, String note) {
        this.code = code;
        this.name = name;
        this.date = date;
        this.type = type;
        this.range = range;
        this.note = note;

        intRangeVal();
    }

    @Override
    public int compareTo(YjygVo o) {
        if (SfsConf.YJYG_COMPARE_BY_DATE) {
            return this.date.compareTo(o.date) * -1;
        } else {
            if (this.type < o.type) {
                return -1;
            } else if (this.type > o.type) {
                return 1;
            } else {
                if (this.sign < o.sign) {
                    return 1;
                } else if (this.sign > o.sign) {
                    return -1;
                } else {
                    if (this.rangeVal < o.rangeVal) {
                        return 1;
                    } else if (this.rangeVal > o.rangeVal) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "YjygVo [code=" + code + ", name=" + name + ", date=" + date + ", type=" + type + ", range=" + range
                + ", note=" + note + ", sign=" + sign + ", rangeVal=" + rangeVal + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public float getRangeVal() {
        return rangeVal;
    }

    public void setRangeVal(float rangeVal) {
        this.rangeVal = rangeVal;
    }
}
