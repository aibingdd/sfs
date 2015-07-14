package com.an.sfs.crawler.gdyj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.util.AppFile;

public class GdyjFetcherMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(GdyjFetcherMain.class);

    public static void main(String[] args) {
        LOGGER.info("Start application.");
        AppFile.initDirs();
        new GdyjFetcher().run();
        LOGGER.info("Exit application.");
    }
}
