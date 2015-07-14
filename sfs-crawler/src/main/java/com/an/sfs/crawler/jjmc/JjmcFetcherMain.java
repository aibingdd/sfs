package com.an.sfs.crawler.jjmc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.util.AppFile;

public class JjmcFetcherMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(JjmcFetcherMain.class);

    public static void main(String[] args) {
        LOGGER.info("Start application.");
        AppFile.initDirs();
        new JjmcFetcher().run();
        LOGGER.info("Exit application.");
    }
}