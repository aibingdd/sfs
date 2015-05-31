package com.an.sfs.crawler;

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

public class GdrsLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GdrsLoader.class);
    private static GdrsLoader inst;

    private GdrsLoader() {
    }

    public static GdrsLoader getInst() {
        if (inst == null) {
            inst = new GdrsLoader();
            inst.init();
        }
        return inst;
    }

    public List<GdrsVo> getGdrsList() {
        return gdrsList;
    }

    public Map<String, List<GdrsVo>> getGdrsMap() {
        return gdrsMap;
    }

    /**
     * @param codeList
     */
    public void getCodeList(List<String> codeList) {
        codeList.addAll(gdrsMap.keySet());
        Collections.sort(codeList);
    }

    private List<GdrsVo> gdrsList = new ArrayList<>();
    private Map<String, List<GdrsVo>> gdrsMap = new HashMap<>();
    private static final String INVALID = "--";
    private static final String UNIT_WAN = "万";
    private static final String UNIT_YI = "亿";

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

                        int count = 0;
                        if (!strs[1].equals(INVALID)) {
                            int idx = strs[1].indexOf(UNIT_WAN);
                            if (idx != -1) {
                                count = (int) (Float.parseFloat(strs[1].substring(0, idx)) * 10000f);
                            } else {
                                count = Integer.parseInt(strs[1]);
                            }
                        }

                        float countChangeRate = 0f;
                        if (!strs[2].equals(INVALID)) {
                            countChangeRate = Float.parseFloat(strs[2]);
                        }

                        int floatStock = 0;
                        if (!strs[3].equals(INVALID)) {
                            int idx = strs[3].indexOf(UNIT_WAN);
                            if (idx != -1) {
                                floatStock = (int) (Float.parseFloat(strs[3].substring(0, idx)) * 10000f);
                            } else {
                                floatStock = Integer.parseInt(strs[3]);
                            }
                        }

                        float fsChangeRate = 0f;
                        if (!strs[4].equals(INVALID)) {
                            fsChangeRate = Float.parseFloat(strs[4]);
                        }

                        float stockPrice = 0f;
                        if (!strs[6].equals(INVALID)) {
                            stockPrice = Float.parseFloat(strs[6]);
                        }

                        int holdStockPrice = 0;
                        if (!strs[7].equals(INVALID)) {
                            int idxWan = strs[7].indexOf(UNIT_WAN);
                            int idxYi = strs[7].indexOf(UNIT_YI);
                            if (idxWan != -1) {
                                holdStockPrice = (int) (Float.parseFloat(strs[7].substring(0, idxWan)) * 10000f);
                            } else if (idxYi != -1) {
                                holdStockPrice = (int) (Float.parseFloat(strs[7].substring(0, idxYi)) * 100000000f);
                            } else {
                                holdStockPrice = Integer.parseInt(strs[7]);
                            }
                        }

                        float top10StockRate = 0f;
                        if (!strs[8].equals(INVALID)) {
                            top10StockRate = Float.parseFloat(strs[8]);
                        }

                        float top10FloatStockRate = 0f;
                        if (!strs[9].equals(INVALID)) {
                            top10FloatStockRate = Float.parseFloat(strs[9]);
                        }

                        GdrsVo vo = new GdrsVo();
                        vo.setCode(code);
                        vo.setDate(date);
                        vo.setCount(count);
                        vo.setCountChangeRate(countChangeRate);
                        vo.setFloatStock(floatStock);
                        vo.setFsChangeRate(fsChangeRate);
                        vo.setStockPrice(stockPrice);
                        vo.setHoldStockPrice(holdStockPrice);
                        vo.setTop10StockRate(top10StockRate);
                        vo.setTop10FloatStockRate(top10FloatStockRate);
                        gdrsList.add(vo);
                        gdrsMap.get(code).add(vo);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error, code {}", code, e);
                break;
            }
        }
    }
}
