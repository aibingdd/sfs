package com.an.sfs.crawler.gdyj;

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
    // Stock Code -> [GdrsVo]
    private Map<String, List<GdrsVo>> gdrsMap = new HashMap<>();

    public Map<String, List<GdrsVo>> getGdrsMap() {
        return gdrsMap;
    }

    private void init() {
        List<File> files = new ArrayList<File>();
        FileUtil.getFilesUnderDir(AppFilePath.getOutputGdyjGdrsDir(), files);
        for (File f : files) {
            String stock = FileUtil.getFileName(f.toString());
            gdrsMap.put(stock, new ArrayList<>());

            try (BufferedReader br = new BufferedReader(new FileReader(f));) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        String[] strs = line.split(";");

                        String date = "20" + strs[0];
                        if (!AppUtil.isValidDate(date)) {
                            continue;
                        }

                        int shareholderCount = 0;
                        if (!AppUtil.INVALID.equals(strs[1])) {
                            int idx = strs[1].indexOf(AppUtil.UNIT_WAN);
                            if (idx != -1) {
                                shareholderCount = (int) (Float.parseFloat(strs[1].substring(0, idx)) * 10000f);
                            } else {
                                shareholderCount = Integer.parseInt(strs[1]);
                            }
                        }

                        int circulationStockCountPer = 0;
                        if (!AppUtil.INVALID.equals(strs[3])) {
                            int idx = strs[3].indexOf(AppUtil.UNIT_WAN);
                            if (idx != -1) {
                                circulationStockCountPer = (int) (Float.parseFloat(strs[3].substring(0, idx)) * 10000f);
                            } else {
                                circulationStockCountPer = Integer.parseInt(strs[3]);
                            }
                        }

                        float stockPrice = 0f;
                        if (!AppUtil.INVALID.equals(strs[6])) {
                            stockPrice = Float.parseFloat(strs[6]);
                        }

                        float sdgdRate = 0;
                        if (!AppUtil.INVALID.equals(strs[8])) {
                            sdgdRate = Float.parseFloat(strs[8]);
                        }

                        float sdltgdRate = 0;
                        if (!AppUtil.INVALID.equals(strs[9])) {
                            sdltgdRate = Float.parseFloat(strs[9]);
                        }

                        GdrsVo vo = new GdrsVo();
                        vo.setCode(stock);
                        vo.setDate(date);
                        vo.setShareholderCount(shareholderCount);
                        vo.setCirculationStockCountPer(circulationStockCountPer);
                        vo.setStockPrice(stockPrice);
                        vo.setSdgdRate(sdgdRate);
                        vo.setSdltgdRate(sdltgdRate);
                        gdrsMap.get(stock).add(vo);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error, code {}", stock, e);
                break;
            }
        }
    }

    private void initCountChangeRate() {
        for (String code : gdrsMap.keySet()) {
            List<GdrsVo> list = gdrsMap.get(code);
            Collections.sort(list);
            gdrsMap.put(code, list);

            if (list.size() > 1) {
                for (int i = 0; i < list.size() - 1; i++) {
                    GdrsVo cur = list.get(i);
                    GdrsVo last = list.get(i + 1);
                    float shareholderCountChangeRate = ((float) cur.getShareholderCount())
                            / ((float) last.getShareholderCount());
                    float circulationStockCountPerChangeRate = ((float) cur.getCirculationStockCountPer())
                            / ((float) last.getCirculationStockCountPer());
                    cur.setShareholderCountChangeRate(shareholderCountChangeRate);
                    cur.setCirculationStockCountPerChangeRate(circulationStockCountPerChangeRate);
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
            inst.initCountChangeRate();
        }
        return inst;
    }
}
