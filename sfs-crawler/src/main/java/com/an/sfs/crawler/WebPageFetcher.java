package com.an.sfs.crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WebPageFetcher {
    // sh600000 or sz000001
    private static final String GDYJ_URL_PATTERN = "http://f10.eastmoney.com/f10_v2/ShareholderResearch.aspx?code=%s%s";
    private static final String CWFX_URL_PATTERN = "http://f10.eastmoney.com/f10_v2/FinanceAnalysis.aspx?code=%s%s";

    private static final int num = 4;// Year count
    private static final int n = 1;// For year
    // 60000001 or 00000102
    // num=6 year count or season count
    // n=1 for year, n=2 for season
    // http://f10.eastmoney.com/f10_v2/BackOffice.aspx?command=RptF10MainTarget&code=00254302&num=9&code1=sz002543&n=1
    private static final String CWFX_YEAR_URL_PATTERN = "http://f10.eastmoney.com/f10_v2/BackOffice.aspx?command=RptF10MainTarget&code=%s%s&num=%s&code1=%s%s&n=%s";

    public void run() {
        fetchHtml(GDYJ_URL_PATTERN, AppFilePath.getInputGdyjDir());
        fetchHtml(CWFX_URL_PATTERN, AppFilePath.getInputCwfxDir());

        fetchCwfxData(CWFX_YEAR_URL_PATTERN, AppFilePath.getInputCwfxYearDir());
    }

    private void fetchCwfxData(String urlPattern, String inputDir) {
        List<String> shCodes = new ArrayList<>();
        CodeLoader.getInst().getShCodes(shCodes);
        for (String code : shCodes) {
            String url = String.format(urlPattern, code, "01", num, "sh", code, n);
            String filePath = inputDir + File.separator + code + ".txt";
            if (!FileUtil.isFileExist(filePath)) {
                AppUtil.download(url, filePath);
            }
            FileUtil.formatHtmlFile(filePath);
        }

        List<String> szCodes = new ArrayList<>();
        CodeLoader.getInst().getSzCodes(szCodes);
        for (String code : szCodes) {
            String url = String.format(urlPattern, code, "02", num, "sz", code, n);
            String filePath = inputDir + File.separator + code + ".txt";
            if (!FileUtil.isFileExist(filePath)) {
                AppUtil.download(url, filePath);
                FileUtil.formatHtmlFile(filePath);
            }
        }
    }

    private void fetchHtml(String urlPattern, String inputDir) {
        List<String> shCodes = new ArrayList<>();
        CodeLoader.getInst().getShCodes(shCodes);
        for (String code : shCodes) {
            String url = String.format(urlPattern, "sh", code);
            String filePath = inputDir + File.separator + code + ".html";
            if (!FileUtil.isFileExist(filePath)) {
                AppUtil.download(url, filePath);
            }
        }

        List<String> szCodes = new ArrayList<>();
        CodeLoader.getInst().getSzCodes(szCodes);
        for (String code : szCodes) {
            String url = String.format(urlPattern, "sz", code);
            String filePath = inputDir + File.separator + code + ".html";
            if (!FileUtil.isFileExist(filePath)) {
                AppUtil.download(url, filePath);
            }
        }
    }
}
