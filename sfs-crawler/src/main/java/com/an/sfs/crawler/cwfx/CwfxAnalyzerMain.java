package com.an.sfs.crawler.cwfx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;

public class CwfxAnalyzerMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(CwfxAnalyzerMain.class);

    public static void main(String[] args) {
        LOGGER.info("Start application.");
        AppFilePath.initDirs();
        new CwfxAnalyzer().run();
        LOGGER.info("Exit application.");
    }
}
