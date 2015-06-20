package com.an.sfs.crawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.an.sfs.crawler.fhrz.FhrzLoader;
import com.an.sfs.crawler.gdrs.GdrsLoader;
import com.an.sfs.crawler.gdrs.GdrsSortVo;
import com.an.sfs.crawler.gdrs.GdrsVo;
import com.an.sfs.crawler.tfp.TfpLoader;

/**
 * Search all stocks which holder's count is decreasing from START_DATE.
 * 
 * @author Anthony
 *
 */
public class GdrsDownMain {
    private static final String START_SEASON = "2014-06-30";
    private static final String CURRENT_SEASON = "2015-03-31";

    private static final String HTML_FILE = "Stock_Gdrs_Down.html";
    private static final String ZF_HTML_FILE = "Stock_Gdrs_Down_Zf.html";
    private static final String TFP_HTML_FILE = "Stock_Gdrs_Down_Tfp.html";

    private static final String TXT_FILE = "Stock_Gdrs_Down.txt";
    private static final String ZF_TXT_FILE = "Stock_Gdrs_Down_Zf.txt";
    private static final String TFP_TXT_FILE = "Stock_Gdrs_Down_Tfp.txt";

    public static void main(String[] args) {
        List<String> stockList = new ArrayList<>();
        Map<String, String> appendInfoMap = new HashMap<>();
        find(stockList, appendInfoMap);
        List<Map<String, String>> appendInfoList = new ArrayList<Map<String, String>>();
        appendInfoList.add(appendInfoMap);

        List<String> zfmxStockList = new ArrayList<>();
        FhrzLoader.getInst().getZfmxCodes(stockList, zfmxStockList);

        List<String> tfpStockList = new ArrayList<>();
        TfpLoader.getInst().getTfpCodes(stockList, tfpStockList);

        Map<String, String> zfMap = new HashMap<>();
        FhrzLoader.getInst().getZfmxMap(zfMap);
        List<Map<String, String>> zfList = new ArrayList<Map<String, String>>();
        zfList.add(zfMap);

        Map<String, String> tfpMap = new HashMap<>();
        TfpLoader.getInst().getTfpMap(tfpMap);
        List<Map<String, String>> tfpList = new ArrayList<Map<String, String>>();
        tfpList.add(tfpMap);

        AppUtil.exportTxt(stockList, TXT_FILE);
        AppUtil.exportTxt(zfmxStockList, ZF_TXT_FILE);
        AppUtil.exportTxt(tfpStockList, TFP_TXT_FILE);

        AppUtil.exportHtml(stockList, appendInfoList, HTML_FILE);
        AppUtil.exportHtml(zfmxStockList, zfList, ZF_HTML_FILE);
        AppUtil.exportHtml(tfpStockList, tfpList, TFP_HTML_FILE);
    }

    private static final boolean SORT_BY_COUNT_DIFFERENCE = true;

    private static void find(List<String> outStockList, Map<String, String> appendInfoMap) {
        List<String> targetStocklist = new ArrayList<>();
        Map<String, List<GdrsVo>> gdrsMap = GdrsLoader.getInst().getGdrsMap();
        for (String code : gdrsMap.keySet()) {
            List<GdrsVo> list = gdrsMap.get(code);
            if (list.size() < 4) {
                continue; // At least has 4 seasons' data
            }

            boolean invalid = false;
            for (GdrsVo vo : list) {
                if (vo.getDate().compareTo(START_SEASON) >= 0) {
                    if (vo.getCountChangeRate() > 1) {
                        invalid = true;
                        break;
                    }
                }
            }

            if (!invalid) {
                targetStocklist.add(code);
            }
        }
        if (!SORT_BY_COUNT_DIFFERENCE) {
            outStockList.addAll(targetStocklist);
            return;
        }

        List<GdrsSortVo> gdrsList = new ArrayList<>();
        for (String code : targetStocklist) {
            List<GdrsVo> list = gdrsMap.get(code);
            int startCount = 0;
            int currentCount = 0;
            for (GdrsVo vo : list) {
                if (vo.getDate().equals(START_SEASON)) {
                    startCount = vo.getCount();
                }
                if (vo.getDate().equals(CURRENT_SEASON)) {
                    currentCount = vo.getCount();
                }
            }
            if (startCount != 0 && currentCount != 0) {
                float diff = (float) (currentCount) / (float) startCount;
                GdrsSortVo vo = new GdrsSortVo(code, diff);
                gdrsList.add(vo);
            }
        }

        Collections.sort(gdrsList);
        for (GdrsSortVo vo : gdrsList) {
            System.out.println(vo);
            outStockList.add(vo.getCode());
            String countRateStr = vo.getCountRateStr();
            appendInfoMap.put(vo.getCode(), countRateStr);
        }
    }
}
