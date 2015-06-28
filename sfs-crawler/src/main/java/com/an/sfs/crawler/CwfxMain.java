package com.an.sfs.crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.an.sfs.crawler.ccjg.CcjgLoader;
import com.an.sfs.crawler.cwfx.CwfxLoader;
import com.an.sfs.crawler.cwfx.CwfxProfitUpLoader;
import com.an.sfs.crawler.cwfx.CwfxVo;
import com.an.sfs.crawler.cwfx.InvalidCwfxLoader;
import com.an.sfs.crawler.cwfx.ReportVo;
import com.an.sfs.crawler.fhrz.FhrzLoader;
import com.an.sfs.crawler.gdyj.GdrsDownLoader;
import com.an.sfs.crawler.name.IgnoreStockLoader;
import com.an.sfs.crawler.name.IndustryLoader;
import com.an.sfs.crawler.name.WhiteHorseStockLoader;
import com.an.sfs.crawler.tdx.StockLoader;
import com.an.sfs.crawler.tfp.TfpLoader;

/**
 * Search all stocks which net profit is increasing for last three years.
 * 
 * @author Anthony
 *
 */
public class CwfxMain {
    public static final boolean SORT_BY_RONA = true;
    public static final boolean SORT_BY_ROTA = false;
    public static final boolean SORT_BY_DTAR = false;
    public static final boolean SORT_BY_PE = false;
    public static final boolean SORT_BY_PB = false;

    public static void main(String[] args) {
        AppFilePath.initDirs();
        exportProfitUp();
        exportAllRona();
        exportRonaByIndustry();
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

    private static void exportRonaByIndustry() {
        Map<String, List<CwfxVo>> cwfxMap = CwfxLoader.getInst().getCwfxMap();

        Set<String> invalidCwfxSet = InvalidCwfxLoader.getInst().getStockSet();

        // Industry Code -> [ReportVo]
        Map<String, List<ReportVo>> industryReportMap = new HashMap<>();
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
            if (!industryReportMap.containsKey(industryCode)) {
                industryReportMap.put(industryCode, new ArrayList<>());
            }

            List<CwfxVo> list = cwfxMap.get(stock);
            if (list.size() > 2) {// At least have 3 year's data
                float totalRona = 0f;
                float totalRota = 0f;
                float totalDtar = 0f;
                for (int i = 0; i < 3; i++) {
                    CwfxVo cwfxVo = list.get(i);
                    totalRona += cwfxVo.getRona();
                    totalRota += cwfxVo.getRota();
                    totalDtar += cwfxVo.getDtar();
                }
                float avgRona = totalRona / 3f;
                float avgRota = totalRota / 3f;
                float avgDtar = totalDtar / 3f;

                CwfxVo latestCwfxVo = list.get(0);
                ReportVo avgReportVo = new ReportVo(stock, avgRona, avgRota, avgDtar, latestCwfxVo.getPe(),
                        latestCwfxVo.getPb());
                industryReportMap.get(industryCode).add(avgReportVo);
            }
        }

        List<ReportVo> avgIndustryVoList = new ArrayList<>();

        for (String industryCode : industryReportMap.keySet()) {
            List<ReportVo> reportVoList = industryReportMap.get(industryCode);

            float totalRona = 0f;
            float totalRota = 0f;
            float totalDtar = 0f;
            float totalPe = 0f;
            float totalPb = 0f;
            for (ReportVo vo : reportVoList) {
                totalRona += vo.getRona();
                totalRota += vo.getRota();
                totalDtar += vo.getDtar();
                totalPe += vo.getPe();
                totalPb += vo.getPb();
            }
            int count = reportVoList.size();
            float avgRona = totalRona / (float) count;
            float avgRota = totalRota / (float) count;
            float avgDtar = totalDtar / (float) count;
            float avgPe = totalPe / (float) count;
            float avgPb = totalPb / (float) count;

            // Add average values this industry
            ReportVo avgIndustryVo = new ReportVo(industryCode, avgRona, avgRota, avgDtar, avgPe, avgPb);
            avgIndustryVoList.add(avgIndustryVo);
            reportVoList.add(avgIndustryVo);

            initReportVo(reportVoList);

            Collections.sort(reportVoList);

            List<String> stockCodeList = new ArrayList<>();
            for (ReportVo vo : reportVoList) {
                stockCodeList.add(vo.getCode());
            }

            String industryName = IndustryLoader.getInst().getIndustryName(industryCode);
            String txt = AppFilePath.getOutputCwfxRonaDir() + File.separator + "Stock_Cwfx_Rona_" + industryCode
                    + industryName + ".txt";
            FileUtil.exportStock(stockCodeList, txt);

            String html = AppFilePath.getOutputCwfxRonaDir() + File.separator + "Stock_Cwfx_Rona_" + industryCode
                    + industryName + ".html";
            FileUtil.exportReport(reportVoList, html);
        }

        exportIndustryRona(avgIndustryVoList);
    }

    private static void exportIndustryRona(List<ReportVo> avgIndustryVoList) {
        initReportVo(avgIndustryVoList);
        Collections.sort(avgIndustryVoList);
        String html = AppFilePath.getOutputCwfxRonaDir() + File.separator + "Stock_Cwfx_Industry" + ".html";
        FileUtil.exportReport(avgIndustryVoList, html);
    }

    private static void exportAllRona() {
        Map<String, List<CwfxVo>> cwfxMap = CwfxLoader.getInst().getCwfxMap();

        Set<String> invalidSet = InvalidCwfxLoader.getInst().getStockSet();

        List<ReportVo> reportVoList = new ArrayList<>();
        for (String code : cwfxMap.keySet()) {
            if (invalidSet.contains(code)) {
                continue;
            }
            String publicDate = StockLoader.getInst().getPublicDate(code);
            if (publicDate.compareTo("2012-12-31") > 0) {
                continue;
            }

            List<CwfxVo> list = cwfxMap.get(code);
            if (list.size() > 2) {// At least have 3 year's data
                float totalRona = 0f;
                float totalRota = 0f;
                float totalDtar = 0f;
                for (int i = 0; i < 3; i++) {
                    CwfxVo cwfxVo = list.get(i);
                    totalRona += cwfxVo.getRona();
                    totalRota += cwfxVo.getRota();
                    totalDtar += cwfxVo.getDtar();
                }
                float avgRona = totalRona / 3f;
                float avgRota = totalRota / 3f;
                float avgDtar = totalDtar / 3f;

                CwfxVo latestCwfxVo = list.get(0);
                ReportVo vo = new ReportVo(code, avgRona, avgRota, avgDtar, latestCwfxVo.getPe(), latestCwfxVo.getPb());
                reportVoList.add(vo);
            }
        }

        initReportVo(reportVoList);

        Collections.sort(reportVoList);

        List<String> stockCodeList = new ArrayList<>();
        for (ReportVo vo : reportVoList) {
            stockCodeList.add(vo.getCode());
        }
        String txt = AppFilePath.getOutputCwfxRonaDir() + File.separator + "Stock_Cwfx_Rona.txt";
        FileUtil.exportStock(stockCodeList, txt);
        String html = AppFilePath.getOutputCwfxRonaDir() + File.separator + "Stock_Cwfx_Rona.html";
        FileUtil.exportReport(reportVoList, html);
    }

    public static void initReportVo(List<ReportVo> reportVoList) {
        int i = 1;
        for (ReportVo vo : reportVoList) {
            vo.setIndex(String.format("%04d", i++));

            String code = vo.getCode();
            String name = StockLoader.getInst().getStockName(code);
            if (name == null) {
                name = IndustryLoader.getInst().getIndustryName(code);
            }
            vo.setName(name);

            vo.setRegion(StockLoader.getInst().getRegion(code));

            long jgcc = CcjgLoader.getInst().getTotal(code);
            vo.setJgcc(FileUtil.FLOAT_FORMAT.format((float) jgcc / 10000f) + "万");

            String note = "";
            if (IgnoreStockLoader.getInst().isIgnore(code)) {
                note += " | IGNORE";
            } else if (WhiteHorseStockLoader.getInst().isWhiteHorse(code)) {
                note += " | WHITEHORSE";
            }

            if (GdrsDownLoader.getInst().isGdrsDown(code)) {
                note += " | GDRS";
            }
            if (CwfxProfitUpLoader.getInst().isProfitUp(code)) {
                note += " | PROFIT";
            }
            vo.setNote(note);
        }
    }
}