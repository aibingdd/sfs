package com.an.sfs.crawler.gdyj;

import com.an.sfs.crawler.AppUtil;

public class GdyjVo implements Comparable<GdyjVo> {
    private String code;
    private long jgTotal;
    private float jgRatio;
    private long circulation;

    public String getRatioStr() {
        if (jgRatio == 0) {
            return "0%";
        }
        return AppUtil.FLOAT_DF.format(jgRatio);
    }

    public GdyjVo(String code, long jgTotal, long circulation) {
        this.code = code;
        this.jgTotal = jgTotal;
        this.circulation = circulation;

        if (circulation > 0) {
            this.jgRatio = (float) ((float) jgTotal / (float) circulation);
        } else {
            this.jgRatio = 0f;
        }
    }

    @Override
    public int compareTo(GdyjVo o) {
        if (Math.abs(this.jgRatio - o.jgRatio) < 0.0001f) {
            return 0;
        } else if (this.jgRatio > o.jgRatio) {
            return -1;
        } else if (this.jgRatio < o.jgRatio) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "GdyjVo [code=" + code + ", jgTotal=" + jgTotal + ", jgRatio=" + jgRatio + ", circulation="
                + circulation + "]";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getJgTotal() {
        return jgTotal;
    }

    public void setJgTotal(long jgTotal) {
        this.jgTotal = jgTotal;
    }

    public float getJgRatio() {
        return jgRatio;
    }

    public void setJgRatio(float jgRatio) {
        this.jgRatio = jgRatio;
    }

    public long getCirculation() {
        return circulation;
    }

    public void setCirculation(long circulation) {
        this.circulation = circulation;
    }
}
