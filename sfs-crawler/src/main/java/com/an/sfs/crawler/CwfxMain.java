package com.an.sfs.crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.an.sfs.crawler.cwfx.CwfxLoader;
import com.an.sfs.crawler.cwfx.CwfxProfitUpLoader;
import com.an.sfs.crawler.cwfx.CwfxSortVo;
import com.an.sfs.crawler.cwfx.CwfxVo;
import com.an.sfs.crawler.cwfx.InvalidCwfxLoader;
import com.an.sfs.crawler.fhrz.FhrzLoader;
import com.an.sfs.crawler.name.IndustryLoader;
import com.an.sfs.crawler.report.ReportVo;
import com.an.sfs.crawler.report.ReportVoLoader;
import com.an.sfs.crawler.tdx.StockLoader;
import com.an.sfs.crawler.tfp.TfpLoader;

/**
 * Search all stocks which net profit is increasing for last three years.
 * 
 * @author Anthony
 *
 */
public class CwfxMain {
    public static void main(String[] args) {
        AppFilePath.initDirs();
        exportProfitUp();
        searchRona();
        searchRonaIndustry();
    }

    private static void exportProfitUp() {
        List<String> stockList = CwfxProfitUpLoader.getInst().getStockList();

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

        String txt = AppFilePath.getOutputDir() + File.separator + "Stock_Cwfx_Profit.txt";
        String txtZf = AppFilePath.getOutputDir() + File.separator + "Stock_Cwfx_Profit_Zf.txt";
        String txtTfp = AppFilePath.getOutputDir() + File.separator + "Stock_Cwfx_Profit_Tfp.txt";
        String html = AppFilePath.getOutputDir() + File.separator + "Stock_Cwfx_Profit_Zf.html";
        String htmlTfp = AppFilePath.getOutputDir() + File.separator + "Stock_Cwfx_Profit_Tfp.html";

        FileUtil.exportStock(stockList, txt);
        FileUtil.exportStock(zfmxStockList, txtZf);
        FileUtil.exportStock(tfpStockList, txtTfp);
        FileUtil.exportHtml(zfmxStockList, zfmxAppendInfoList, html);
        FileUtil.exportHtml(tfpStockList, tfpAppendInfoList, htmlTfp);
    }

    private static void searchRonaIndustry() {
        Map<String, List<CwfxVo>> cwfxMap = CwfxLoader.getInst().getCwfxMap();

        Set<String> invalidCwfxSet = InvalidCwfxLoader.getInst().getStockSet();

        // Industry Code -> [CwfxSortVo]
        Map<String, List<CwfxSortVo>> industryCwfxMap = new HashMap<>();
        for (String stock : cwfxMap.keySet()) {
            if (invalidCwfxSet.contains(stock)) {
                continue;
            }
            String publicDate = StockLoader.getInst().getPublicDate(stock);
            if (publicDate.compareTo("2012-12-31") > 0) {
                continue;
            }

            String industryName = StockLoader.getInst().getIndustryName(stock);
            String industryCode = IndustryLoader.getInst().getIndustryCode(industryName);
            if (!industryCwfxMap.containsKey(industryCode)) {
                industryCwfxMap.put(industryCode, new ArrayList<>());
            }

            List<CwfxVo> list = cwfxMap.get(stock);
            if (list.size() > 2) {// At least have 3 year's data
                float totalRona = 0f;
                float totalRota = 0f;
                float totalDtar = 0f;
                for (int i = 0; i < 3; i++) {
                    totalRona += list.get(i).getRona();
                    totalRota += list.get(i).getRota();
                    totalDtar += list.get(i).getDtar();
                }
                float avgRona = totalRona / 3f;
                float avgRota = totalRota / 3f;
                float avgDtar = totalDtar / 3f;

                CwfxSortVo vo = new CwfxSortVo(stock, avgRona, avgRota, avgDtar);
                industryCwfxMap.get(industryCode).add(vo);
            }
        }

        List<CwfxSortVo> avgIndustryVoList = new ArrayList<>();

        for (String industryCode : industryCwfxMap.keySet()) {
            List<CwfxSortVo> voList = industryCwfxMap.get(industryCode);

            float totalRona = 0f;
            float totalRota = 0f;
            float totalDtar = 0f;
            for (CwfxSortVo vo : voList) {
                totalRona += vo.getRona();
                totalRota += vo.getRota();
                totalDtar += vo.getDtar();
            }
            float avgRona = totalRona / (float) voList.size();
            float avgRota = totalRota / (float) voList.size();
            float avgDtar = totalDtar / (float) voList.size();

            // Add average values this industry
            CwfxSortVo avgVo = new CwfxSortVo(industryCode, avgRona, avgRota, avgDtar);
            avgIndustryVoList.add(avgVo);
            voList.add(avgVo);

            Collections.sort(voList);

            Map<String, String> ronaMap = new HashMap<>();
            Map<String, String> rotaMap = new HashMap<>();
            Map<String, String> dtarMap = new HashMap<>();
            List<String> stockList = new ArrayList<>();
            for (CwfxSortVo vo : voList) {
                stockList.add(vo.getCode());
                float rona = vo.getRona() / 100f;
                float rota = vo.getRota() / 100f;
                float dtar = vo.getDtar() / 100f;
                ronaMap.put(vo.getCode(), FileUtil.PERCENT_FORMAT.format(rona));
                rotaMap.put(vo.getCode(), FileUtil.PERCENT_FORMAT.format(rota));
                dtarMap.put(vo.getCode(), FileUtil.PERCENT_FORMAT.format(dtar));
            }

            String name = IndustryLoader.getInst().getIndustryName(industryCode);
            String txt = AppFilePath.getOutputCwfxRonaDir() + File.separator + "Stock_Cwfx_Rona_" + industryCode + name
                    + ".txt";
            String html = AppFilePath.getOutputCwfxRonaDir() + File.separator + "Stock_Cwfx_Rona_" + industryCode
                    + name + ".html";

            FileUtil.exportStock(stockList, txt);

            List<ReportVo> reportVoList = new ArrayList<>();
            ReportVoLoader.loadReportVo(stockList, ronaMap, rotaMap, dtarMap, reportVoList);
            FileUtil.exportReport(reportVoList, html);
        }

        exportIndustryRona(avgIndustryVoList);
    }

    private static void exportIndustryRona(List<CwfxSortVo> avgIndustryVoList) {
        Collections.sort(avgIndustryVoList);
        Map<String, String> ronaMap = new HashMap<>();
        Map<String, String> rotaMap = new HashMap<>();
        Map<String, String> dtarMap = new HashMap<>();
        List<String> stockList = new ArrayList<>();
        for (CwfxSortVo vo : avgIndustryVoList) {
            stockList.add(vo.getCode());
            float rona = vo.getRona() / 100f;
            float rota = vo.getRota() / 100f;
            float dtar = vo.getDtar() / 100f;
            ronaMap.put(vo.getCode(), FileUtil.PERCENT_FORMAT.format(rona));
            rotaMap.put(vo.getCode(), FileUtil.PERCENT_FORMAT.format(rota));
            dtarMap.put(vo.getCode(), FileUtil.PERCENT_FORMAT.format(dtar));
        }

        List<Map<String, String>> appendList = new ArrayList<>();
        appendList.add(ronaMap);
        appendList.add(rotaMap);
        appendList.add(dtarMap);

        String txt = AppFilePath.getOutputCwfxRonaDir() + File.separator + "Stock_Cwfx_Industry_Rona" + ".txt";
        String html = AppFilePath.getOutputCwfxRonaDir() + File.separator + "Stock_Cwfx_Industry_Rona" + ".html";
        FileUtil.exportStock(stockList, txt);

        List<ReportVo> reportVoList = new ArrayList<>();
        ReportVoLoader.loadReportVo(stockList, ronaMap, rotaMap, dtarMap, reportVoList);
        FileUtil.exportReport(reportVoList, html);
    }

    private static void searchRona() {
        Map<String, List<CwfxVo>> cwfxMap = CwfxLoader.getInst().getCwfxMap();

        Set<String> invalidSet = InvalidCwfxLoader.getInst().getStockSet();

        List<CwfxSortVo> voList = new ArrayList<>();
        for (String stock : cwfxMap.keySet()) {
            if (invalidSet.contains(stock)) {
                continue;
            }
            String publicDate = StockLoader.getInst().getPublicDate(stock);
            if (publicDate.compareTo("2012-12-31") > 0) {
                continue;
            }

            List<CwfxVo> list = cwfxMap.get(stock);
            if (list.size() > 2) {// At least have 3 year's data
                float totalRona = 0f;
                float totalRota = 0f;
                float totalDtar = 0f;
                for (int i = 0; i < 3; i++) {
                    totalRona += list.get(i).getRona();
                    totalRota += list.get(i).getRota();
                    totalDtar += list.get(i).getDtar();
                }
                float avgRona = totalRona / 3f;
                float avgRota = totalRota / 3f;
                float avgDtar = totalDtar / 3f;

                CwfxSortVo vo = new CwfxSortVo(stock, avgRona, avgRota, avgDtar);
                voList.add(vo);
            }
        }

        Collections.sort(voList);

        Map<String, String> ronaMap = new HashMap<>();
        Map<String, String> rotaMap = new HashMap<>();
        Map<String, String> dtarMap = new HashMap<>();
        List<String> stockList = new ArrayList<>();
        for (CwfxSortVo vo : voList) {
            stockList.add(vo.getCode());
            float rona = vo.getRona() / 100f;
            float rota = vo.getRota() / 100f;
            float dtar = vo.getDtar() / 100f;
            ronaMap.put(vo.getCode(), FileUtil.PERCENT_FORMAT.format(rona));
            rotaMap.put(vo.getCode(), FileUtil.PERCENT_FORMAT.format(rota));
            dtarMap.put(vo.getCode(), FileUtil.PERCENT_FORMAT.format(dtar));
        }

        String txt = AppFilePath.getOutputCwfxRonaDir() + File.separator + "Stock_Cwfx_Rona.txt";
        String html = AppFilePath.getOutputCwfxRonaDir() + File.separator + "Stock_Cwfx_Rona.html";

        FileUtil.exportStock(stockList, txt);

        List<ReportVo> reportVoList = new ArrayList<>();
        ReportVoLoader.loadReportVo(stockList, ronaMap, rotaMap, dtarMap, reportVoList);
        FileUtil.exportReport(reportVoList, html);
    }
}