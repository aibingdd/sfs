package com.an.sfs.crawler.gsgk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;

public class GsgkFetcherMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(GsgkFetcherMain.class);

    public static void main(String[] args) {
        LOGGER.info("Start application.");
        AppFilePath.initDirs();
        new GsgkFetcher().run();
        LOGGER.info("Exit application.");
    }
}
