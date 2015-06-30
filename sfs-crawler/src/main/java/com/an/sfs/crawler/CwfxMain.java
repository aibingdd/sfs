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
import com.an.sfs.crawler.cwfx.CwfxUpLoader;
import com.an.sfs.crawler.cwfx.CwfxVo;
import com.an.sfs.crawler.cwfx.InvalidCwfxLoader;
import com.an.sfs.crawler.cwfx.ReportVo;
import com.an.sfs.crawler.fhrz.FhrzLoader;
import com.an.sfs.crawler.gdyj.GdrsDownLoader;
import com.an.sfs.crawler.name.IgnoreStockLoader;
import com.an.sfs.crawler.name.IndustryLoader;
import com.an.sfs.crawler.name.WhiteHorseStockLoader;
import com.an.sfs.crawler.tdx.StockLoader;
import com.an.sfs.crawler.tdx.StockVo;
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

        String txt = AppFilePath.getOutputCwfxRonaDir() + File.separator + "Stock_Cwfx.txt";
        String html = AppFilePath.getOutputCwfxRonaDir() + File.separator + "Stock_Cwfx.html";
        exportCwfx(txt, html, 0);

        txt = AppFilePath.getOutputCwfxRonaDir() + File.separator + "Stock_Cwfx__Rona_Rota.txt";
        html = AppFilePath.getOutputCwfxRonaDir() + File.separator + "Stock_Cwfx__Rona_Rota.html";
        exportCwfx(txt, html, 1);

        exportRonaByIndustry();
    }

    private static void exportProfitUp() {
        List<String> stockList = CwfxUpLoader.getInst().getProfitUpStockList();

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
                float totalProfit = 0f;
                for (int i = 0; i < 3; i++) {
                    CwfxVo cwfxVo = list.get(i);
                    totalRona += cwfxVo.getRona();
                    totalRota += cwfxVo.getRota();
                    totalDtar += cwfxVo.getDtar();
                    totalProfit += cwfxVo.getProfit();
                }
                float avgRona = totalRona / 3f;
                float avgRota = totalRota / 3f;
                float avgDtar = totalDtar / 3f;
                float avgProfit = totalProfit / 3f;

                CwfxVo latestCwfxVo = list.get(0);
                ReportVo avgReportVo = new ReportVo(stock, avgRona, avgRota, avgDtar, avgProfit, latestCwfxVo.getPe(),
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
            float totalProfitChange = 0f;
            float totalPe = 0f;
            float totalPb = 0f;
            for (ReportVo vo : reportVoList) {
                totalRona += vo.getRona();
                totalRota += vo.getRota();
                totalDtar += vo.getDtar();
                totalProfitChange += vo.getProfitChange();
                totalPe += vo.getPe();
                totalPb += vo.getPb();
            }
            int count = reportVoList.size();
            float avgRona = totalRona / (float) count;
            float avgRota = totalRota / (float) count;
            float avgDtar = totalDtar / (float) count;
            float avgProfitChange = totalProfitChange / (float) count;
            float avgPe = totalPe / (float) count;
            float avgPb = totalPb / (float) count;

            // Add average values this industry
            ReportVo avgIndustryVo = new ReportVo(industryCode, avgRona, avgRota, avgDtar, avgProfitChange, avgPe,
                    avgPb);
            avgIndustryVoList.add(avgIndustryVo);
            reportVoList.add(avgIndustryVo);

            initReportVo(reportVoList);
            Collections.sort(reportVoList);
            initReportIndex(reportVoList);

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
            FileUtil.exportReport(reportVoList, html, false);
        }

        exportIndustryRona(avgIndustryVoList);
    }

    private static void exportIndustryRona(List<ReportVo> avgIndustryVoList) {
        initReportVo(avgIndustryVoList);
        Collections.sort(avgIndustryVoList);
        initReportIndex(avgIndustryVoList);
        String html = AppFilePath.getOutputCwfxRonaDir() + File.separator + "Stock_Cwfx_Industry" + ".html";
        FileUtil.exportReport(avgIndustryVoList, html, false);
    }

    /**
     * @param txtFile
     * @param htmlFile
     * @param exportType
     *            0: all <br>
     *            1: rona_rota
     */
    private static void exportCwfx(String txtFile, String htmlFile, int exportType) {
        Map<String, List<CwfxVo>> cwfxMap = CwfxLoader.getInst().getCwfxMap();

        Set<String> invalidSet = InvalidCwfxLoader.getInst().getStockSet();

        List<ReportVo> reportVoList = new ArrayList<>();
        for (String code : cwfxMap.keySet()) {
            if (invalidSet.contains(code)) {
                continue;
            }

            if (StockLoader.getInst().getStockVo(code).isSuspend()) {
                continue;
            }

            String publicDate = StockLoader.getInst().getPublicDate(code);
            if (publicDate.compareTo("2012-12-31") > 0) {
                continue;
            }

            if (exportType == 1 && !(CwfxUpLoader.getInst().isRonaUp(code) && CwfxUpLoader.getInst().isRotaUp(code))) {
                continue;
            }

            List<CwfxVo> list = cwfxMap.get(code);
            if (list.size() > 2) {// At least have 3 year's data
                float totalRona = 0f;
                float totalRota = 0f;
                float totalDtar = 0f;
                float totalProfitChange = 0f;
                String ronaStr = "";
                String rotaStr = "";
                String dtarStr = "";
                String profitChangeStr = "";
                for (int i = 0; i < 3; i++) {
                    CwfxVo cwfxVo = list.get(i);
                    totalRona += cwfxVo.getRona();
                    totalRota += cwfxVo.getRota();
                    totalDtar += cwfxVo.getDtar();
                    totalProfitChange += cwfxVo.getProfitChangeRate() - 1;
                    ronaStr += cwfxVo.getRona() + "% ";
                    rotaStr += cwfxVo.getRota() + "% ";
                    dtarStr += cwfxVo.getDtar() + "% ";
                    profitChangeStr += FileUtil.PERCENT_FORMAT.format((cwfxVo.getProfitChangeRate() - 1)) + " ";
                }
                float avgRona = totalRona / 3f;
                float avgRota = totalRota / 3f;
                float avgDtar = totalDtar / 3f;
                float avgProfitChange = totalProfitChange / 3f;

                if (avgProfitChange < 0f) {
                    continue;
                }

                CwfxVo latestCwfxVo = list.get(0);
                ReportVo vo = new ReportVo(code, avgRona, avgRota, avgDtar, avgProfitChange, latestCwfxVo.getPe(),
                        latestCwfxVo.getPb());
                vo.setRonaStr(ronaStr);
                vo.setRotaStr(rotaStr);
                vo.setDtarStr(dtarStr);
                vo.setProfitChangeStr(profitChangeStr);
                reportVoList.add(vo);
            }
        }

        initReportVo(reportVoList);
        Collections.sort(reportVoList);
        initReportIndex(reportVoList);

        List<String> stockCodeList = new ArrayList<>();
        for (ReportVo vo : reportVoList) {
            stockCodeList.add(vo.getCode());
        }

        FileUtil.exportStock(stockCodeList, txtFile);
        FileUtil.exportReport(reportVoList, htmlFile, true);
    }

    public static void initReportVo(List<ReportVo> reportVoList) {
        for (ReportVo vo : reportVoList) {
            String code = vo.getCode();
            StockVo stockVo = StockLoader.getInst().getStockVo(code);

            String name = null;
            if (stockVo != null) {
                name = stockVo.getName();
            } else {
                name = IndustryLoader.getInst().getIndustryName(code);
            }
            vo.setName(name);

            if (stockVo != null) {
                vo.setRegion(stockVo.getRegion());
            }

            long jgcc = CcjgLoader.getInst().getTotal(code);
            vo.setJgcc(FileUtil.FLOAT_FORMAT.format((float) jgcc / 10000f) + "万");

            String note = "";
            if (GdrsDownLoader.getInst().isGdrsDown(code)) {
                note = note + " | GDRS";
            }
            if (CwfxUpLoader.getInst().isRonaUp(code)) {
                note = note + " | RONA_UP";
            }
            if (CwfxUpLoader.getInst().isRotaUp(code)) {
                note = note + " | ROTA_UP";
            }
            if (CwfxUpLoader.getInst().isProfitUp(code)) {
                note = note + " | PROFIT_UP";
            }
            if (IgnoreStockLoader.getInst().isIgnore(code)) {
                note = note + " | IGNORE";
            } else if (WhiteHorseStockLoader.getInst().isWhiteHorse(code)) {
                note = note + " | WHITEHORSE";
            }

            if (stockVo != null && stockVo.isSuspend()) {
                note = note + " | 停盘";
            }
            vo.setNote(note);
        }
    }

    public static void initReportIndex(List<ReportVo> reportVoList) {
        int i = 1;
        for (ReportVo vo : reportVoList) {
            vo.setIndex(String.format("%04d", i++));
        }
    }
}