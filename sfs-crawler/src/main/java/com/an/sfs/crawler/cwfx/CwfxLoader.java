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

    public void getStockCodeList(List<String> codeList) {
        codeList.addAll(cwfxMap.keySet());
        Collections.sort(codeList);
    }

    private void init() {
        String dir = AppFilePath.getOutputCwfxYearDir();
        List<File> outFileList = new ArrayList<File>();
        FileUtil.getFilesUnderDir(dir, outFileList);

        for (File f : outFileList) {
            String code = FileUtil.getFileName(f.toString());
            cwfxMap.put(code, new ArrayList<>());

            try (BufferedReader br = new BufferedReader(new FileReader(f));) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        String[] strs = line.split(";");
                        String date = "20" + strs[0];

                        long income = 0;
                        if (!strs[1].equals(AppUtil.INVALID)) {
                            int idxWan = strs[1].indexOf(AppUtil.UNIT_WAN);
                            int idxYi = strs[1].indexOf(AppUtil.UNIT_YI);
                            if (idxWan != -1) {
                                float tmp = Float.parseFloat(strs[1].substring(0, idxWan));
                                income = (long) (tmp * 10000f);
                            } else if (idxYi != -1) {
                                float tmp = Float.parseFloat(strs[1].substring(0, idxYi));
                                income = (long) (tmp * 100000000f);
                            } else {
                                income = Long.parseLong(strs[1]);
                            }
                        }

                        long profit = 0;
                        if (!strs[2].equals(AppUtil.INVALID)) {
                            int idxWan = strs[2].indexOf(AppUtil.UNIT_WAN);
                            int idxYi = strs[2].indexOf(AppUtil.UNIT_YI);
                            if (idxWan != -1) {
                                float tmp = Float.parseFloat(strs[2].substring(0, idxWan));
                                profit = (long) (tmp * 10000f);
                            } else if (idxYi != -1) {
                                float tmp = Float.parseFloat(strs[2].substring(0, idxYi));
                                profit = (long) (tmp * 100000000f);
                            } else {
                                profit = Long.parseLong(strs[2]);
                            }
                        }

                        CwfxVo vo = new CwfxVo();
                        vo.setCode(code);
                        vo.setDate(date);
                        vo.setIncome(income);
                        vo.setProfit(profit);
                        cwfxMap.get(code).add(vo);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error, code {}", code, e);
                break;
            }
        }
    }

    private void initRate() {
        for (String code : cwfxMap.keySet()) {
            List<CwfxVo> list = cwfxMap.get(code);
            Collections.sort(list);
            if (list.size() > 1) {
                for (int i = 0; i < list.size() - 1; i++) {
                    CwfxVo cur = list.get(i);
                    CwfxVo last = list.get(i + 1);
                    float incomeRate = ((float) cur.getIncome()) / ((float) last.getIncome());
                    float profitRate = ((float) cur.getProfit()) / ((float) last.getProfit());
                    cur.setIncomeRate(incomeRate);
                    cur.setProfitRate(profitRate);
                }
            }
        }
    }

    private CwfxLoader() {
    }

    private static CwfxLoader inst;

    public static CwfxLoader getInst() {
        if (inst == null) {
            inst = new CwfxLoader();
            inst.init();
            inst.initRate();
        }
        return inst;
    }
}
