package com.an.sfs.crawler.gdrs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.AppUtil;
import com.an.sfs.crawler.FileUtil;
import com.an.sfs.crawler.code.StockCodeLoader;

public class GdrsFetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(GdrsFetcher.class);
    // Gu dong yan jiu
    // sh600000 or sz000001
    private static final String GDYJ_URL = "http://f10.eastmoney.com/f10_v2/ShareholderResearch.aspx?code=%s%s";

    public void run() {
        fetchHtml(GDYJ_URL, AppFilePath.getInputGdyjDir());
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
