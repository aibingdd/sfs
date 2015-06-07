package com.an.sfs.crawler.gdrs;

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

public class GdrsLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GdrsLoader.class);
    private Map<String, List<GdrsVo>> gdrsMap = new HashMap<>();

    public Map<String, List<GdrsVo>> getGdrsMap() {
        return gdrsMap;
    }

    public void getStockCodeList(List<String> codeList) {
        codeList.addAll(gdrsMap.keySet());
        Collections.sort(codeList);
    }

    private void init() {
        String dir = AppFilePath.getOutputGdyjGdrsDir();
        List<File> outFileList = new ArrayList<File>();
        FileUtil.getFilesUnderDir(dir, outFileList);
        for (File f : outFileList) {
            String code = FileUtil.getFileName(f.toString());
            gdrsMap.put(code, new ArrayList<>());
            try (BufferedReader br = new BufferedReader(new FileReader(f));) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        String[] strs = line.split(";");
                        String date = "20" + strs[0];
                        if (!AppUtil.isValidDate(date)) {
                            continue;
                        }
                        int count = 0;
                        if (!strs[1].equals(AppUtil.INVALID)) {
                            int idx = strs[1].indexOf(AppUtil.UNIT_WAN);
                            if (idx != -1) {
                                count = (int) (Float.parseFloat(strs[1].substring(0, idx)) * 10000f);
                            } else {
                                count = Integer.parseInt(strs[1]);
                            }
                        }

                        int floatStock = 0;
                        if (!strs[3].equals(AppUtil.INVALID)) {
                            int idx = strs[3].indexOf(AppUtil.UNIT_WAN);
                            if (idx != -1) {
                                floatStock = (int) (Float.parseFloat(strs[3].substring(0, idx)) * 10000f);
                            } else {
                                floatStock = Integer.parseInt(strs[3]);
                            }
                        }

                        float stockPrice = 0f;
                        if (!strs[6].equals(AppUtil.INVALID)) {
                            stockPrice = Float.parseFloat(strs[6]);
                        }

                        long holdStockPrice = 0;
                        if (!strs[7].equals(AppUtil.INVALID)) {
                            int idxWan = strs[7].indexOf(AppUtil.UNIT_WAN);
                            int idxYi = strs[7].indexOf(AppUtil.UNIT_YI);
                            if (idxWan != -1) {
                                holdStockPrice = (long) (Float.parseFloat(strs[7].substring(0, idxWan)) * 10000f);
                            } else if (idxYi != -1) {
                                holdStockPrice = (long) (Float.parseFloat(strs[7].substring(0, idxYi)) * 100000000f);
                            } else {
                                holdStockPrice = Long.parseLong(strs[7]);
                            }
                        }

                        GdrsVo vo = new GdrsVo();
                        vo.setCode(code);
                        vo.setDate(date);
                        vo.setCount(count);
                        vo.setFloatStock(floatStock);
                        vo.setStockPrice(stockPrice);
                        vo.setHoldStockPrice(holdStockPrice);
                        gdrsMap.get(code).add(vo);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error, code {}", code, e);
                break;
            }
        }
    }

    private void initRate() {
        for (String code : gdrsMap.keySet()) {
            List<GdrsVo> list = gdrsMap.get(code);
            Collections.sort(list);

            if (list.size() > 1) {
                for (int i = 0; i < list.size() - 1; i++) {
                    GdrsVo cur = list.get(i);
                    GdrsVo last = list.get(i + 1);
                    float countChangeRate = ((float) cur.getCount()) / ((float) last.getCount());
                    float floatStockChangeRate = ((float) cur.getFloatStock()) / ((float) last.getFloatStock());
                    cur.setCountChangeRate(countChangeRate);
                    cur.setFloatStockChangeRate(floatStockChangeRate);
                }
            }
        }
    }

    private static GdrsLoader inst;

    private GdrsLoader() {
    }

    public static GdrsLoader getInst() {
        if (inst == null) {
            inst = new GdrsLoader();
            inst.init();
            inst.initRate();
        }
        return inst;
    }
}
