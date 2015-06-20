package com.an.sfs.crawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.an.sfs.crawler.cwfx.CwfxLoader;
import com.an.sfs.crawler.cwfx.CwfxSortVo;
import com.an.sfs.crawler.cwfx.CwfxVo;
import com.an.sfs.crawler.fhrz.FhrzLoader;
import com.an.sfs.crawler.tfp.TfpLoader;

/**
 * Search all stocks which net profit is increasing for last three years.
 * 
 * @author Anthony
 *
 */
public class CwfxMain {
    private static final String START_DATE = "2012-01-01";

    public static void main(String[] args) {
        searchProfit();
        searchRona();
    }

    private static void searchProfit() {
        List<String> stockList = new ArrayList<>();

        Map<String, List<CwfxVo>> cwfxMap = CwfxLoader.getInst().getCwfxMap();

        for (String stock : cwfxMap.keySet()) {

            List<CwfxVo> voList = cwfxMap.get(stock);

            boolean invalid = false;
            for (int i = 0; i < voList.size() - 1; i++) {
                CwfxVo vo = voList.get(i);
                if (vo.getDate().compareTo(START_DATE) > 0) {
                    if (vo.getProfit() < 0 || vo.getProfitChangeRate() < 1f) {
                        invalid = true;
                        break;
                    }
                }
            }

            if (!invalid) {
                stockList.add(stock);
            }
        }

        Collections.sort(stockList);

        List<String> zfmxStockList = new ArrayList<>();
        FhrzLoader.getInst().getZfmxCodes(stockList, zfmxStockList);

        List<String> tfpStockList = new ArrayList<>();
        TfpLoader.getInst().getTfpCodes(stockList, tfpStockList);

        Map<String, String> zfMap = new HashMap<>();
        FhrzLoader.getInst().getZfmxMap(zfMap);
        List<Map<String, String>> zfmxAppendInfoList = new ArrayList<Map<String, String>>();
        zfmxAppendInfoList.add(zfMap);

        Map<String, String> tfpMap = new HashMap<>();
        TfpLoader.getInst().getTfpMap(tfpMap);
        List<Map<String, String>> tfpAppendInfoList = new ArrayList<Map<String, String>>();
        tfpAppendInfoList.add(tfpMap);

        FileUtil.exportTxt(stockList, "Stock_Cwfx_Profit.txt");
        FileUtil.exportTxt(zfmxStockList, "Stock_Cwfx_Profit_Zf.txt");
        FileUtil.exportTxt(tfpStockList, "Stock_Cwfx_Profit_Tfp.txt");

        FileUtil.exportHtml(zfmxStockList, zfmxAppendInfoList, "Stock_Cwfx_Profit_Zf.html");
        FileUtil.exportHtml(tfpStockList, tfpAppendInfoList, "Stock_Cwfx_Profit_Tfp.html");
    }

    private static void searchRona() {
        Map<String, List<CwfxVo>> cwfxMap = CwfxLoader.getInst().getCwfxMap();

        List<CwfxSortVo> voList = new ArrayList<>();
        for (String code : cwfxMap.keySet()) {
            List<CwfxVo> list = cwfxMap.get(code);
            if (list.size() > 2) {// At least have 3 year's data
                float totalRona = 0f;
                for (int i = 0; i < 3; i++) {
                    totalRona += list.get(i).getRona();
                }
                float avgRona = totalRona / 3f;
                CwfxSortVo vo = new CwfxSortVo(code, avgRona);
                voList.add(vo);
            }
        }

        Collections.sort(voList);

        Map<String, String> ronaMap = new HashMap<>();
        List<String> stockList = new ArrayList<>();
        for (CwfxSortVo vo : voList) {
            stockList.add(vo.getCode());
            float rona = vo.getRona() / 100f;
            ronaMap.put(vo.getCode(), AppUtil.FLOAT_DF.format(rona));
        }

        List<Map<String, String>> appendList = new ArrayList<>();
        appendList.add(ronaMap);

        FileUtil.exportTxt(stockList, "Stock_Cwfx_Rona.txt");

        FileUtil.exportHtml(stockList, appendList, "Stock_Cwfx_Rona.html");
    }
}