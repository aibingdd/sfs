package com.an.sfs.crawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.an.sfs.crawler.gdyj.GdrsDownLoader;
import com.an.sfs.crawler.gdyj.GdrsLoader;
import com.an.sfs.crawler.gdyj.GdrsReportVo;
import com.an.sfs.crawler.gdyj.GdrsSortVo;
import com.an.sfs.crawler.gdyj.GdrsVo;
import com.an.sfs.crawler.name.IndustryLoader;
import com.an.sfs.crawler.tdx.StockLoader;
import com.an.sfs.crawler.util.AppFile;
import com.an.sfs.crawler.util.FileUtil;

public class GdrsMain {
    private static List<String> reportCodeList2 = new ArrayList<>();
    private static List<GdrsReportVo> reportVoList2 = new ArrayList<>();

    private static List<String> reportCodeList3 = new ArrayList<>();
    private static List<GdrsReportVo> reportVoList3 = new ArrayList<>();

    public static void main(String[] args) {
        AppFile.initDirs();
        find();

        FileUtil.exportStock(reportCodeList2, AppFile.getOutputFp("Stock_Gdrs_Down2.txt"));
        FileUtil.exportHtml(reportVoList2, AppFile.getOutputFp("Stock_Gdrs_Down2.html"));

        FileUtil.exportStock(reportCodeList3, AppFile.getOutputFp("Stock_Gdrs_Down3.txt"));
        FileUtil.exportHtml(reportVoList3, AppFile.getOutputFp("Stock_Gdrs_Down3.html"));
    }

    private static void find() {
        List<String> gdrsDownCodeList = GdrsDownLoader.getInst().getStockList();
        Map<String, List<GdrsVo>> gdrsMap = GdrsLoader.getInst().getGdrsMap();
        final Map<String, Integer> gdrsNumMap = new HashMap<>();

        List<GdrsSortVo> downRateSortList = new ArrayList<>();
        {
            for (String code : gdrsDownCodeList) {
                List<GdrsVo> gdrsList = gdrsMap.get(code);
                int startCount = 0;
                int currentCount = 0;
                for (GdrsVo vo : gdrsList) {
                    if (vo.getDate().equals(SfsConf.GDRS_START_SEASON)) {
                        startCount = vo.getShareholderCount();
                    }
                    if (vo.getDate().equals(SfsConf.CURRENT_SEASON)) {
                        currentCount = vo.getShareholderCount();
                        gdrsNumMap.put(code, currentCount);
                    }
                }
                if (startCount != 0 && currentCount != 0) {
                    float diff = (float) (currentCount) / (float) startCount;
                    GdrsSortVo vo = new GdrsSortVo(GdrsSortVo.SORT_BY_DOWN_RATE);
                    vo.setValues(code, diff);
                    downRateSortList.add(vo);
                }
            }

            Collections.sort(downRateSortList);
            export(downRateSortList, gdrsNumMap, reportCodeList2, reportVoList2);
        }

        {
            List<GdrsSortVo> gdrsNumSortList = new ArrayList<>();
            for (GdrsSortVo vo : downRateSortList) {
                GdrsSortVo sortVo = new GdrsSortVo(GdrsSortVo.SORT_BY_GDRS_NUMBER);
                String code = vo.getCode();
                sortVo.setValues(code, gdrsNumMap.get(code));
                sortVo.setCountRatio(vo.getCountRatio());
                gdrsNumSortList.add(sortVo);
            }
            Collections.sort(gdrsNumSortList);
            export(gdrsNumSortList, gdrsNumMap, reportCodeList3, reportVoList3);
        }
    }

    private static void export(List<GdrsSortVo> sortList, Map<String, Integer> gdrsNumMap,
            List<String> outReportCodeList, List<GdrsReportVo> outReportVoList) {
        for (GdrsSortVo vo : sortList) {
            GdrsReportVo rptVo = new GdrsReportVo();
            String code = vo.getCode();
            rptVo.setCode(code);
            String displayName = getDisplayName(code);
            if (displayName.contains("*ST")) {
                continue;
            }

            rptVo.setName(displayName);
            rptVo.setCountRateStr(vo.getCountRateStr());

            Integer gdrsNum = gdrsNumMap.get(code);
            if (gdrsNum > SfsConf.MAX_GDRS_NUMBER) {
                continue;
            }
            rptVo.setGdrsNum(" " + gdrsNum);

            int floatShare = (int) (StockLoader.getInst().getStockVo(code).getFloatShare() / 10000f);
            if (floatShare < SfsConf.MIN_FLOAT_SHARE) {
                continue;
            }
            rptVo.setNetFloatShare(" " + floatShare);

            outReportCodeList.add(code);
            outReportVoList.add(rptVo);
        }
    }

    private static String getDisplayName(String code) {
        String displayName = StockLoader.getInst().getStockName(code);
        if (displayName == null) {
            displayName = IndustryLoader.getInst().getIndustryName(code);
        }
        if (displayName.length() < 4) {
            for (int j = 0; j < 4 - displayName.length(); j++) {
                displayName += "&nbsp&nbsp&nbsp&nbsp";
            }
        }
        return displayName;
    }
}
