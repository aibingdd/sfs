package com.an.sfs.crawler.fhrz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;

public class FhrzFetcherMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(FhrzFetcherMain.class);

    public static void main(String[] args) {
        LOGGER.info("Start application.");
        AppFilePath.initDirs();
        new FhrzFetcher().run();
        LOGGER.info("Exit application.");
    }
}
