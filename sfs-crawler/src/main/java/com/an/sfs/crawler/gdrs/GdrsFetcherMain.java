package com.an.sfs.crawler.gdrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;

public class GdrsFetcherMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(GdrsFetcherMain.class);

    public static void main(String[] args) {
        LOGGER.info("Start application.");
        AppFilePath.initDirs();
        new GdrsFetcher().run();
        LOGGER.info("Exit application.");
    }
}
