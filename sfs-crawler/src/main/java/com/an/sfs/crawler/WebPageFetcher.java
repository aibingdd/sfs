package com.an.sfs.crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WebPageFetcher {
    private static final String gdyj = "http://f10.eastmoney.com/f10_v2/ShareholderResearch.aspx?code=%s%s";

    public WebPageFetcher() {
    }

    public void run() {
        List<String> shCodes = new ArrayList<>();
        CodeLoader.getInst().getShCodes(shCodes);
        for (String code : shCodes) {
            String url = String.format(gdyj, "sh", code);
            WebUtil.crawl(url, FileDirUtil.getGdyjDir() + File.separator + code + ".html");
        }

        List<String> szCodes = new ArrayList<>();
        CodeLoader.getInst().getSzCodes(szCodes);
        for (String code : szCodes) {
            String url = String.format(gdyj, "sz", code);
            WebUtil.crawl(url, FileDirUtil.getGdyjDir() + File.separator + code + ".html");
        }
    }
}
