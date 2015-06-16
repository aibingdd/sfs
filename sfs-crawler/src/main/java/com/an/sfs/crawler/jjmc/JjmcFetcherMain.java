package com.an.sfs.crawler.jjmc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;

public class JjmcFetcherMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(JjmcFetcherMain.class);

    public static void main(String[] args) {
        LOGGER.info("Start application.");
        AppFilePath.initDirs();
        new JjmcFetcher().run();
        LOGGER.info("Exit application.");
    }
}