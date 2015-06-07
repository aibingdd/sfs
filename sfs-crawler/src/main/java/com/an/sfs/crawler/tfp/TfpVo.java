package com.an.sfs.crawler.tfp;

public class TfpVo implements Comparable<TfpVo> {
    private String code;
    private String name;
    private String stopTime;
    private String resumeTime;
    private String period;
    private String reason;

    public String getDisplayStr() {
        String st = stopTime;
        String rt = resumeTime;
        if (!st.isEmpty()) {
            st = st.substring(2, 10);
        }
        if (!rt.isEmpty() && rt.startsWith("20")) {
            rt = rt.substring(2, 10);
        }
        if (period.equals("取消停牌")) {
            return st + ", " + period + ", " + reason;
        } else if (rt.isEmpty()) {
            return st + ", " + period + ", " + reason;
        } else {
            return st + ", " + rt + ", " + period + ", " + reason;
        }
    }

    @Override
    public int compareTo(TfpVo o) {
        int v = this.stopTime.compareTo(o.stopTime);
        if (v == 0) {
            return this.code.compareTo(o.code);
        }
        return v;
    }

    @Override
    public String toString() {
        return "TfpVo [code=" + code + ", name=" + name + ", stopTime=" + stopTime + ", resumeTime=" + resumeTime
                + ", period=" + period + ", reason=" + reason + "]";
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

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getResumeTime() {
        return resumeTime;
    }

    public void setResumeTime(String resumeTime) {
        this.resumeTime = resumeTime;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
