package com.an.sfs.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebPageFetcherMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebPageFetcherMain.class);

    public static void main(String[] args) {
        LOGGER.info("Start application.");
        AppFilePath.initDirs();
        new WebPageFetcher().run();
        LOGGER.info("Exit application.");
    }
}
