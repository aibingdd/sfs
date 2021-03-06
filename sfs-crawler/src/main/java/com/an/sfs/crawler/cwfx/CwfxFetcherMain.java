package com.an.sfs.crawler.cwfx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.util.AppFile;

public class CwfxFetcherMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(CwfxFetcherMain.class);

    public static void main(String[] args) {
        LOGGER.info("Start application.");
        AppFile.initDirs();
        new CwfxFetcher().run();
        LOGGER.info("Exit application.");
    }
}
