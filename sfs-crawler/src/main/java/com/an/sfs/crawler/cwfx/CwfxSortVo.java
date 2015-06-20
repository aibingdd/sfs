package com.an.sfs.crawler.cwfx;

public class CwfxSortVo implements Comparable<CwfxSortVo> {
    private String code;
    private float rona;

    /**
     * @param code
     * @param rona
     */
    public CwfxSortVo(String code, float rona) {
        this.code = code;
        this.rona = rona;
    }

    @Override
    public int compareTo(CwfxSortVo o) {
        if (Math.abs(this.rona - o.rona) < 0.001f) {
            return 0;
        } else if (this.rona > o.rona) {
            return -1;
        } else if (this.rona < o.rona) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "CwfxSortVo [code=" + code + ", rona=" + rona + "]";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getRona() {
        return rona;
    }

    public void setRona(float rona) {
        this.rona = rona;
    }
}
