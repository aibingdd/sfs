package com.an.sfs.crawler.yjbb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.util.AppFile;

public class YjbbFetcherMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(YjbbFetcherMain.class);

    public static void main(String[] args) {
        LOGGER.info("Start application.");
        AppFile.initDirs();
        new YjbbFetcher().run();
        LOGGER.info("Exit application.");
    }
}
