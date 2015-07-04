package com.an.sfs.crawler.cwfx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.AppUtil;
import com.an.sfs.crawler.FileUtil;

public class CwfxLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CwfxLoader.class);

    private Map<String, List<CwfxVo>> cwfxMap = new HashMap<>();

    public Map<String, List<CwfxVo>> getCwfxMap() {
        return cwfxMap;
    }

    /**
     * @param stockCode
     * @return
     */
    public CwfxVo getCwfxVo(String stockCode) {
        List<CwfxVo> list = cwfxMap.get(stockCode);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    private void init() {
        String dir = AppFilePath.getOutputCwfxDir();
        List<File> files = new ArrayList<File>();
        FileUtil.getFilesUnderDir(dir, files);

        for (File f : files) {
            String code = FileUtil.getFileName(f.toString());
            cwfxMap.put(code, new ArrayList<>());

            try (BufferedReader br = new BufferedReader(new FileReader(f));) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        line = line.replaceAll(",", "");
                        String[] strs = line.split(";");

                        float peps = 0f;
                        if (!AppUtil.INVALID.equals(strs[0])) {
                            peps = Float.parseFloat(strs[0]);
                        }
                        float naps = 0f;
                        if (!AppUtil.INVALID.equals(strs[1])) {
                            naps = Float.parseFloat(strs[1]);
                        }

                        String date = "20" + strs[2];

                        long income = 0;
                        if (!AppUtil.INVALID.equals(strs[3])) {
                            int idxWan = strs[3].indexOf(AppUtil.UNIT_WAN);
                            int idxYi = strs[3].indexOf(AppUtil.UNIT_YI);
                            if (idxWan != -1) {
                                float tmp = Float.parseFloat(strs[3].substring(0, idxWan));
                                income = (long) (tmp * 10000f);
                            } else if (idxYi != -1) {
                                float tmp = Float.parseFloat(strs[3].substring(0, idxYi));
                                income = (long) (tmp * 100000000f);
                            } else {
                                income = Long.parseLong(strs[3]);
                            }
                        }

                        long profit = 0;
                        if (!AppUtil.INVALID.equals(strs[4])) {
                            int idxWan = strs[4].indexOf(AppUtil.UNIT_WAN);
                            int idxYi = strs[4].indexOf(AppUtil.UNIT_YI);
                            if (idxWan != -1) {
                                float tmp = Float.parseFloat(strs[4].substring(0, idxWan));
                                profit = (long) (tmp * 10000f);
                            } else if (idxYi != -1) {
                                float tmp = Float.parseFloat(strs[4].substring(0, idxYi));
                                profit = (long) (tmp * 100000000f);
                            } else {
                                profit = Long.parseLong(strs[4]);
                            }
                        }

                        float rona = 0f;
                        if (!AppUtil.INVALID.equals(strs[5])) {
                            rona = Float.parseFloat(strs[5]);
                        }
                        float rota = 0f;
                        if (!AppUtil.INVALID.equals(strs[6])) {
                            rota = Float.parseFloat(strs[6]);
                        }
                        float dtar = 0f;
                        if (!AppUtil.INVALID.equals(strs[7])) {
                            dtar = Float.parseFloat(strs[7]);
                        }

                        CwfxVo vo = new CwfxVo();
                        vo.setCode(code);
                        vo.setDate(date);
                        vo.setIncome(income);
                        vo.setProfit(profit);
                        vo.setRona(rona);
                        vo.setRota(rota);
                        vo.setDtar(dtar);
                        vo.setPeps(peps);
                        vo.setNaps(naps);
                        cwfxMap.get(code).add(vo);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error, code {}", code, e);
                break;
            }
        }
    }

    public static final int INVALID_CHANGE_RATE = -999;

    private void initChangeRate() {
        for (String code : cwfxMap.keySet()) {
            List<CwfxVo> list = cwfxMap.get(code);
            Collections.sort(list);
            if (list.size() > 1) {
                for (int i = 0; i < list.size() - 1; i++) {
                    CwfxVo cur = list.get(i);
                    CwfxVo last = list.get(i + 1);

                    float incomeRate = 0f;
                    if (cur.getIncome() < 0 || last.getIncome() < 0) {
                        incomeRate = INVALID_CHANGE_RATE;
                    } else {
                        incomeRate = Math.abs((float) cur.getIncome()) / Math.abs((float) last.getIncome());
                        incomeRate = incomeRate - 1;
                    }
                    float profitChangeRate = 0f;
                    profitChangeRate = profitChangeRate - 1;
                    if (cur.getProfit() < 0 || last.getProfit() < 0) {
                        profitChangeRate = INVALID_CHANGE_RATE;
                    } else {
                        profitChangeRate = Math.abs((float) cur.getProfit()) / Math.abs((float) last.getProfit());
                        profitChangeRate = profitChangeRate - 1;
                    }
                    float ronaChangeRate = 0f;
                    if (cur.getRona() < 0 || last.getRona() < 0) {
                        ronaChangeRate = INVALID_CHANGE_RATE;
                    } else {
                        ronaChangeRate = Math.abs((float) cur.getRona()) / Math.abs((float) last.getRona());
                        ronaChangeRate = ronaChangeRate - 1;
                    }
                    float rotaChangeRate = 0f;
                    if (cur.getRota() < 0 || last.getRota() < 0) {
                        rotaChangeRate = INVALID_CHANGE_RATE;
                    } else {
                        rotaChangeRate = Math.abs((float) cur.getRota()) / Math.abs((float) last.getRota());
                        rotaChangeRate = rotaChangeRate - 1;
                    }
                    float dtarChangeRate = 0f;
                    if (cur.getDtar() < 0 || last.getDtar() < 0) {
                        dtarChangeRate = INVALID_CHANGE_RATE;
                    } else {
                        dtarChangeRate = Math.abs((float) cur.getDtar()) / Math.abs((float) last.getDtar());
                        dtarChangeRate = dtarChangeRate - 1;
                    }

                    cur.setIncomeChangeRate(incomeRate);
                    cur.setProfitChangeRate(profitChangeRate);
                    cur.setRonaChangeRate(ronaChangeRate);
                    cur.setRotaChangeRate(rotaChangeRate);
                    cur.setDtarChangeRate(dtarChangeRate);
                }
            }

            cwfxMap.put(code, list);
        }
    }

    private CwfxLoader() {
    }

    private static CwfxLoader inst;

    public static CwfxLoader getInst() {
        if (inst == null) {
            inst = new CwfxLoader();
            inst.init();
            inst.initChangeRate();
        }
        return inst;
    }
}
