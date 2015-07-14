package com.an.sfs.crawler.cninfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.util.AppFile;

public class CninfoFetcherMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(CninfoFetcherMain.class);

    public static void main(String[] args) {
        LOGGER.info("Start application.");
        AppFile.initDirs();
        new CninfoFetcher().run();
        LOGGER.info("Exit application.");
    }
}