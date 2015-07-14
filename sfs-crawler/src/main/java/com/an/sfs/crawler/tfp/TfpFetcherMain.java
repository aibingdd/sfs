package com.an.sfs.crawler.tfp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.util.AppFile;

public class TfpFetcherMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(TfpFetcherMain.class);

    public static void main(String[] args) {
        LOGGER.info("Start application.");
        AppFile.initDirs();
        new TfpFetcher().run();
        LOGGER.info("Exit application.");
    }
}
