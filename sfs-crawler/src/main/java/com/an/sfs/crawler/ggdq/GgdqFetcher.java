package com.an.sfs.crawler.ggdq;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.AppUtil;
import com.an.sfs.crawler.FileUtil;
import com.an.sfs.crawler.tdx.StockLoader;

public class GgdqFetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(GgdqFetcher.class);
    // http://data.eastmoney.com/Notice/NoticeStock.aspx?type=0&stockcode=000001&pn=1
    private static final String XWGG_QBGG_PAGE = "http://data.eastmoney.com/Notice/NoticeStock.aspx?type=0&stockcode=%s&pn=%s";

    public void run() {
        LOGGER.info("Fetch GGDQ.");
        fetchGgdqData(XWGG_QBGG_PAGE, AppFilePath.getInputGgdqDir());
    }

    private void fetchGgdqData(String url, String fileDir) {
        List<String> stockCodeList = StockLoader.getInst().getStockCodeList();
        for (String stock : stockCodeList) {
            downloadGgdq(url, 1, fileDir, stock);
        }
    }

    private void downloadGgdq(String url, int pageNum, String fileDir, String code) {
        String httpUrl = String.format(url, code, pageNum);
        String filePath = fileDir + File.separator + code + ".html";
        if (!FileUtil.isFileExist(filePath)) {
            AppUtil.download(httpUrl, filePath);
        }
    }
}
