package com.an.sfs.crawler.cwfx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.AppUtil;
import com.an.sfs.crawler.FileUtil;
import com.an.sfs.crawler.StockCodeLoader;

public class CwfxFetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(CwfxFetcher.class);
    private static final String CWFX_URL = "http://f10.eastmoney.com/f10_v2/FinanceAnalysis.aspx?code=%s%s";

    // Cai wu fen xi
    private static final int num = 4;// Year count
    private static final int n = 1;// For year
    // 60000001 or 00000102
    // num=6 year count or season count
    // n=1 for year, n=2 for season
    // http://f10.eastmoney.com/f10_v2/BackOffice.aspx?command=RptF10MainTarget&code=00254302&num=9&code1=sz002543&n=1
    private static final String CWFX_YEAR_URL = "http://f10.eastmoney.com/f10_v2/BackOffice.aspx?command=RptF10MainTarget&code=%s%s&num=%s&code1=%s%s&n=%s";

    // type=0&stockcode=300181&pn=1
    private static final String XWGG_QBGG_PAGE = "http://data.eastmoney.com/Notice/NoticeStock.aspx?type=0&stockcode=%s&pn=%s";

    public void run() {
        fetchHtml(CWFX_URL, AppFilePath.getInputCwfxDir());

        fetchCwfxData(CWFX_YEAR_URL, AppFilePath.getInputCwfxYearDir());

        fetchGgdqData(XWGG_QBGG_PAGE, AppFilePath.getInputGgdqDir());
    }

    private void fetchGgdqData(String url, String fileDir) {
        List<String> shCodes = new ArrayList<>();
        StockCodeLoader.getInst().getShCodes(shCodes);
        for (String code : shCodes) {
            downloadGgdq(url, 1, fileDir, code);
        }

        List<String> szCodes = new ArrayList<>();
        StockCodeLoader.getInst().getSzCodes(szCodes);
        for (String code : szCodes) {
            downloadGgdq(url, 1, fileDir, code);
        }
    }

    private void downloadGgdq(String url, int pageNum, String fileDir, String code) {
        String httpUrl = String.format(url, code, pageNum);
        String filePath = fileDir + File.separator + code + ".html";
        if (!FileUtil.isFileExist(filePath)) {
            AppUtil.download(httpUrl, filePath);
        }
    }

    private void fetchCwfxData(String url, String fileDir) {
        List<String> shCodes = new ArrayList<>();
        StockCodeLoader.getInst().getShCodes(shCodes);
        for (String code : shCodes) {
            String httpUrl = String.format(url, code, "01", num, "sh", code, n);
            String filePath = fileDir + File.separator + code + ".txt";
            if (!FileUtil.isFileExist(filePath)) {
                AppUtil.download(httpUrl, filePath);
            }
            FileUtil.formatHtmlFile(filePath);
        }

        List<String> szCodes = new ArrayList<>();
        StockCodeLoader.getInst().getSzCodes(szCodes);
        for (String code : szCodes) {
            String httpUrl = String.format(url, code, "02", num, "sz", code, n);
            String filePath = fileDir + File.separator + code + ".txt";
            if (!FileUtil.isFileExist(filePath)) {
                AppUtil.download(httpUrl, filePath);
                FileUtil.formatHtmlFile(filePath);
            }
        }
    }

    private void fetchHtml(String url, String fileDir) {
        List<String> shCodes = new ArrayList<>();
        StockCodeLoader.getInst().getShCodes(shCodes);
        for (String code : shCodes) {
            String httpUrl = String.format(url, "sh", code);
            String filePath = fileDir + File.separator + code + ".html";
            if (!FileUtil.isFileExist(filePath)) {
                AppUtil.download(httpUrl, filePath);
            }
        }

        List<String> szCodes = new ArrayList<>();
        StockCodeLoader.getInst().getSzCodes(szCodes);
        for (String code : szCodes) {
            String httpUrl = String.format(url, "sz", code);
            String filePath = fileDir + File.separator + code + ".html";
            if (!FileUtil.isFileExist(filePath)) {
                AppUtil.download(httpUrl, filePath);
            }
        }
    }
}
