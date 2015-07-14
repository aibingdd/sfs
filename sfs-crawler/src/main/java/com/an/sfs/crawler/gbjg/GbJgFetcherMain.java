package com.an.sfs.crawler.gbjg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.util.AppFile;

public class GbJgFetcherMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(GbJgFetcherMain.class);

    public static void main(String[] args) {
        LOGGER.info("Start application.");
        AppFile.initDirs();
        new GbJgFetcher().run();
        LOGGER.info("Exit application.");
    }
}
