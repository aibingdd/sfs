package com.an.sfs.crawler.gdrs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.AppUtil;
import com.an.sfs.crawler.FileUtil;

public class GdrsFetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(GdrsFetcher.class);
    // Gu dong yan jiu
    // sh600000 or sz000001
    private static final String GDYJ_URL = "http://f10.eastmoney.com/f10_v2/ShareholderResearch.aspx?code=%s%s";

    private List<String> shCodes = new ArrayList<>();
    private List<String> szCodes = new ArrayList<>();

    public void run() {
        init();
        fetchHtml(GDYJ_URL, AppFilePath.getInputGdyjDir());
    }

    private void fetchHtml(String url, String fileDir) {
        for (String code : shCodes) {
            String httpUrl = String.format(url, "sh", code);
            String filePath = fileDir + File.separator + code + ".html";
            if (!FileUtil.isFileExist(filePath)) {
                AppUtil.download(httpUrl, filePath);
            }
        }

        for (String code : szCodes) {
            String httpUrl = String.format(url, "sz", code);
            String filePath = fileDir + File.separator + code + ".html";
            if (!FileUtil.isFileExist(filePath)) {
                AppUtil.download(httpUrl, filePath);
            }
        }
    }

    private void init() {
        String shEbk = AppFilePath.getConfFile("SH.EBK");
        try (BufferedReader br = new BufferedReader(new FileReader(shEbk));) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    shCodes.add(line.trim().substring(1));
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while parsing SH.EBK.");
        }

        String szEbk = AppFilePath.getConfFile("SZ.EBK");
        try (BufferedReader br = new BufferedReader(new FileReader(szEbk));) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    szCodes.add(line.trim().substring(1));
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while parsing SZ.EBK.");
        }
    }
}
