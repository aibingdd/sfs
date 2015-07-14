package com.an.sfs.crawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.an.sfs.crawler.fhrz.FhrzLoader;
import com.an.sfs.crawler.gdyj.GdrsDownLoader;
import com.an.sfs.crawler.gdyj.GdrsLoader;
import com.an.sfs.crawler.gdyj.GdrsSortVo;
import com.an.sfs.crawler.gdyj.GdrsVo;
import com.an.sfs.crawler.tdx.StockLoader;
import com.an.sfs.crawler.tfp.TfpLoader;
import com.an.sfs.crawler.util.AppFile;
import com.an.sfs.crawler.util.FileUtil;

/**
 * Search all stocks which holder's count is decreasing from START_DATE.
 *
 */
public class GdrsMain {
    public static void main(String[] args) {
        AppFile.initDirs();

        List<String> stockList = new ArrayList<>();
        Map<String, String> appendInfoMap = new HashMap<>();
        find(stockList, appendInfoMap);
        List<Map<String, String>> appendInfoList = new ArrayList<>();
        appendInfoList.add(appendInfoMap);

        List<String> zfmxStockList = new ArrayList<>();
        FhrzLoader.getInst().getZfmxCodes(stockList, zfmxStockList);

        List<String> tfpStockList = new ArrayList<>();
        TfpLoader.getInst().getTfpCodes(stockList, tfpStockList);

        Map<String, String> zfMap = new HashMap<>();
        FhrzLoader.getInst().getZfmxMap(zfMap);
        List<Map<String, String>> zfList = new ArrayList<Map<String, String>>();
        zfList.add(zfMap);

        Map<String, String> tfpMap = TfpLoader.getInst().getTfpMap();
        List<Map<String, String>> tfpList = new ArrayList<Map<String, String>>();
        tfpList.add(tfpMap);

        List<String> list = new ArrayList<>();
        for (String stock : stockList) {
            if (stock.startsWith("3")) { // Filter ChuangYeBan
                continue;
            }
            if (StockLoader.getInst().getStockVo(stock).isSuspend()) {
                continue;
            }
            list.add(stock);
        }

        FileUtil.exportStock(list, AppFile.getOutputFp("Stock_Gdrs_Down.txt"));
        FileUtil.exportHtml(list, appendInfoList, AppFile.getOutputFp("Stock_Gdrs_Down.html"));
    }

    private static void find(List<String> outStockList, Map<String, String> appendInfoMap) {
        List<String> targetStocklist = GdrsDownLoader.getInst().getStockList();

        if (!SfsConf.GDRS_SORT_BY_COUNT_DIFF_VALUE) {
            outStockList.addAll(targetStocklist);
            return;
        }

        Map<String, List<GdrsVo>> gdrsMap = GdrsLoader.getInst().getGdrsMap();
        List<GdrsSortVo> gdrsList = new ArrayList<>();
        for (String code : targetStocklist) {
            List<GdrsVo> list = gdrsMap.get(code);
            int startCount = 0;
            int currentCount = 0;
            for (GdrsVo vo : list) {
                if (vo.getDate().equals(SfsConf.GDRS_START_SEASON)) {
                    startCount = vo.getShareholderCount();
                }
                if (vo.getDate().equals(SfsConf.CURRENT_SEASON)) {
                    currentCount = vo.getShareholderCount();
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
            outStockList.add(vo.getCode());
            String countRateStr = vo.getCountRateStr();
            appendInfoMap.put(vo.getCode(), countRateStr);
        }
    }
}
