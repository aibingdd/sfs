package com.an.sfs.crawler.yjyg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;

public class YjygFetcherMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(YjygFetcherMain.class);

    public static void main(String[] args) {
        LOGGER.info("Start application.");
        AppFilePath.initDirs();
        new YjygFetcher().run();
        LOGGER.info("Exit application.");
    }
}
