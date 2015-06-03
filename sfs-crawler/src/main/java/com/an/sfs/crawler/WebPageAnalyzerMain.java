package com.an.sfs.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebPageAnalyzerMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebPageAnalyzerMain.class);

    public static void main(String[] args) {
        LOGGER.info("Start application.");
        AppFilePath.initDirs();
        new GdrsAnalyzer().run();
        LOGGER.info("Exit application.");
    }
}
