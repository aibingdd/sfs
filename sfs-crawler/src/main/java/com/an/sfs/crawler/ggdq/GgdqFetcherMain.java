package com.an.sfs.crawler.ggdq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.util.AppFile;

/**
 * Gong Gao Da Quan
 */
public class GgdqFetcherMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(GgdqFetcherMain.class);

    // http://data.eastmoney.com/Notice/NoticeStock.aspx?type=0&stockcode=000001&pn=1
    public static void main(String[] args) {
        LOGGER.info("Start application.");
        AppFile.initDirs();
        new GgdqFetcher().run();
        LOGGER.info("Exit application.");
    }
}