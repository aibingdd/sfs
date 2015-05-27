package com.an.sfs.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppMain.class);

    public static void main(String[] args) {
        LOGGER.info("Start application.");
        AppFilePath.initDirs();
        new WebPageFetcher().run();

        new WebPageAnalyzer().run();
        LOGGER.info("Exit application.");
    }
}
