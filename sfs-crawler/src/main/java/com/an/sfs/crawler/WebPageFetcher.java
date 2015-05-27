package com.an.sfs.crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WebPageFetcher {
    private static final String gdyj = "http://f10.eastmoney.com/f10_v2/ShareholderResearch.aspx?code=%s%s";

    public WebPageFetcher() {
    }

    public void run() {
        fetchGdyj();
    }

    private void fetchGdyj() {
        List<String> shCodes = new ArrayList<>();
        CodeLoader.getInst().getShCodes(shCodes);
        for (String code : shCodes) {
            String url = String.format(gdyj, "sh", code);
            String gdyjFilePath = getGdyjFilePath(code);
            if (!FileUtil.isFileExist(gdyjFilePath)) {
                AppUtil.download(url, gdyjFilePath);
            }
        }

        List<String> szCodes = new ArrayList<>();
        CodeLoader.getInst().getSzCodes(szCodes);
        for (String code : szCodes) {
            String url = String.format(gdyj, "sz", code);
            String gdyjFilePath = getGdyjFilePath(code);
            if (!FileUtil.isFileExist(gdyjFilePath)) {
                AppUtil.download(url, gdyjFilePath);
            }
        }
    }

    private String getGdyjFilePath(String code) {
        return AppFilePath.getInputGdyjDir() + File.separator + code + ".html";
    }
}
