package com.an.sfs.crawler.cwfx;

import com.an.sfs.crawler.CwfxMain;
import com.an.sfs.crawler.FileUtil;

public class ReportVo implements Comparable<ReportVo> {
    private String index;
    private String code;// Stock Code or Industry Code
    private String name;// Stock Name or Industry Name
    // Return on Net Assets
    private float rona;
    private String ronaStr;
    // Return on Total Assets
    private float rota;
    private String rotaStr;
    // debt to assets ratio
    private float dtar;
    private String dtarStr;

    // Net profit
    private float profitChange;
    private String profitChangeStr;

    // Price / Earning Per Share
    private float pe;
    // Book Value Per share
    private float pb;

    // region
    private String region;

    // JiGouChiCang
    private String jgcc;

    private String note;

    public boolean isIndustry() {
        return code.startsWith("9");
    }

    public String getNameDisplayStr() {
        String displayName = name;
        if (displayName.length() < 4) {
            String prefix = "";
            for (int j = 0; j < 4 - displayName.length(); j++) {
                prefix += "&nbsp&nbsp&nbsp&nbsp";
            }
            displayName = prefix + displayName;
        }
        return displayName;
    }

    public String getRonaDisplayStr() {
        float val = this.rona / 100f;
        return FileUtil.PERCENT_FORMAT.format(val);
    }

    public String getRotaDisplayStr() {
        float val = this.rota / 100f;
        return FileUtil.PERCENT_FORMAT.format(val);
    }

    public String getDtarDisplayStr() {
        float val = this.dtar / 100f;
        return FileUtil.PERCENT_FORMAT.format(val);
    }

    public String getProfitChangeDisplayStr() {
        float val = this.profitChange;
        return FileUtil.PERCENT_FORMAT.format(val);
    }

    public String getPeDisplayStr() {
        return FileUtil.FLOAT_FORMAT.format(this.pe);
    }

    public String getPbDisplayStr() {
        return FileUtil.FLOAT_FORMAT.format(this.pb);
    }

    public ReportVo(String code, float rona, float rota, float dtar, float profitChange, float pe, float pb) {
        this.code = code;
        this.rona = rona;
        this.rota = rota;
        this.dtar = dtar;
        this.profitChange = profitChange;
        this.pe = pe;
        this.pb = pb;
    }

    @Override
    public int compareTo(ReportVo o) {
        if (CwfxMain.SORT_BY_RONA) {
            if (Math.abs(this.rona - o.rona) < 0.001f) {
                return 0;
            } else if (this.rona > o.rona) {
                return -1;
            } else if (this.rona < o.rona) {
                return 1;
            }
        } else if (CwfxMain.SORT_BY_ROTA) {
            if (Math.abs(this.rota - o.rota) < 0.001f) {
                return 0;
            } else if (this.rota > o.rota) {
                return -1;
            } else if (this.rota < o.rota) {
                return 1;
            }
        } else if (CwfxMain.SORT_BY_DTAR) {
            if (Math.abs(this.dtar - o.dtar) < 0.001f) {
                return 0;
            } else if (this.dtar > o.dtar) {
                return -1;
            } else if (this.dtar < o.dtar) {
                return 1;
            }
        } else if (CwfxMain.SORT_BY_PE) {
            if (Math.abs(this.pe - o.pe) < 0.001f) {
                return 0;
            } else if (this.pe > o.pe) {
                return -1;
            } else if (this.pe < o.pe) {
                return 1;
            }
        } else if (CwfxMain.SORT_BY_PB) {
            if (Math.abs(this.pb - o.pb) < 0.001f) {
                return 0;
            } else if (this.pb > o.pb) {
                return -1;
            } else if (this.pb < o.pb) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "ReportVo [index=" + index + ", code=" + code + ", name=" + name + ", rona=" + rona + ", ronaStr="
                + ronaStr + ", rota=" + rota + ", rotaStr=" + rotaStr + ", dtar=" + dtar + ", dtarStr=" + dtarStr
                + ", profitChange=" + profitChange + ", profitChangeStr=" + profitChangeStr + ", pe=" + pe + ", pb="
                + pb + ", region=" + region + ", jgcc=" + jgcc + ", note=" + note + "]";
    }

    public float getProfitChange() {
        return profitChange;
    }

    public void setProfitChange(float profitChange) {
        this.profitChange = profitChange;
    }

    public String getProfitChangeStr() {
        return profitChangeStr;
    }

    public void setProfitChangeStr(String profitChangeStr) {
        this.profitChangeStr = profitChangeStr;
    }

    public String getRonaStr() {
        return ronaStr;
    }

    public void setRonaStr(String ronaStr) {
        this.ronaStr = ronaStr;
    }

    public String getRotaStr() {
        return rotaStr;
    }

    public void setRotaStr(String rotaStr) {
        this.rotaStr = rotaStr;
    }

    public String getDtarStr() {
        return dtarStr;
    }

    public void setDtarStr(String dtarStr) {
        this.dtarStr = dtarStr;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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

    public float getRona() {
        return rona;
    }

    public void setRona(float rona) {
        this.rona = rona;
    }

    public float getRota() {
        return rota;
    }

    public void setRota(float rota) {
        this.rota = rota;
    }

    public float getDtar() {
        return dtar;
    }

    public void setDtar(float dtar) {
        this.dtar = dtar;
    }

    public float getPe() {
        return pe;
    }

    public void setPe(float pe) {
        this.pe = pe;
    }

    public float getPb() {
        return pb;
    }

    public void setPb(float pb) {
        this.pb = pb;
    }

    public String getJgcc() {
        return jgcc;
    }

    public void setJgcc(String jgcc) {
        this.jgcc = jgcc;
    }
}
