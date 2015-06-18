package com.an.sfs.crawler.ccjg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;

public class CcjgFetcherMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(CcjgFetcherMain.class);

    public static void main(String[] args) {
        LOGGER.info("Start application.");
        AppFilePath.initDirs();
        new CcjgFetcher().run();
        LOGGER.info("Exit application.");
    }
}
