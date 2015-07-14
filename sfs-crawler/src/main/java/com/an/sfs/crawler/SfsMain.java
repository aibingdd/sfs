package com.an.sfs.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.cwfx.CwfxFetcher;
import com.an.sfs.crawler.util.AppFile;

public class SfsMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(SfsMain.class);

    public static void main(String[] args) {
        LOGGER.info("Start SfsMain.");
        AppFile.initDirs();

        LOGGER.info("Cwfx fetch ...");
        new CwfxFetcher().run();

        LOGGER.info("Exit SfsMain.");
    }
}
