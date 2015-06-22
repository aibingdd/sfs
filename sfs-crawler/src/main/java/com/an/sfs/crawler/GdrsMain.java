package com.an.sfs.crawler;

import java.io.File;
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
import com.an.sfs.crawler.tfp.TfpLoader;

/**
 * Search all stocks which holder's count is decreasing from START_DATE.
 * 
 * @author Anthony
 *
 */
public class GdrsMain {
    public static final String START_SEASON = "2014-06-30";

    public static void main(String[] args) {
        AppFilePath.initDirs();

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

        List<String> list = new ArrayList<>();
        for (String stock : stockList) {
            if (!stock.startsWith("3")) { // Filter ChuangYeBan
                list.add(stock);
            }
        }

        String txt = AppFilePath.getOutputDir() + File.separator + "Stock_Gdrs_Down.txt";
        String txtZf = AppFilePath.getOutputDir() + File.separator + "Stock_Gdrs_Down_Zf.txt";
        String tfpTxt = AppFilePath.getOutputDir() + File.separator + "Stock_Gdrs_Down_Tfp.txt";
        String html = AppFilePath.getOutputDir() + File.separator + "Stock_Gdrs_Down.html";
        String htmlZf = AppFilePath.getOutputDir() + File.separator + "Stock_Gdrs_Down_Zf.html";
        String tfpHtml = AppFilePath.getOutputDir() + File.separator + "Stock_Gdrs_Down_Tfp.html";

        FileUtil.exportStock(list, txt);
        FileUtil.exportStock(zfmxStockList, txtZf);
        FileUtil.exportStock(tfpStockList, tfpTxt);
        FileUtil.exportHtml(list, appendInfoList, html);
        FileUtil.exportHtml(zfmxStockList, zfList, htmlZf);
        FileUtil.exportHtml(tfpStockList, tfpList, tfpHtml);
    }

    private static final boolean SORT_BY_COUNT_DIFFERENCE = true;

    private static void find(List<String> outStockList, Map<String, String> appendInfoMap) {
        List<String> targetStocklist = GdrsDownLoader.getInst().getStockList();

        if (!SORT_BY_COUNT_DIFFERENCE) {
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
                if (vo.getDate().equals(START_SEASON)) {
                    startCount = vo.getCount();
                }
                if (vo.getDate().equals(AppUtil.CUR_SEASON)) {
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
            outStockList.add(vo.getCode());
            String countRateStr = vo.getCountRateStr();
            appendInfoMap.put(vo.getCode(), countRateStr);
        }
    }
}
