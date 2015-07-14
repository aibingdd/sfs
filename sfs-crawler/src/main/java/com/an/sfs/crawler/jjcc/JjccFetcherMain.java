package com.an.sfs.crawler.jjcc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.util.AppFile;

/**
 * Ji ji chi chang
 *
 */
public class JjccFetcherMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(JjccFetcherMain.class);

    public static void main(String[] args) {
        LOGGER.info("Start application.");
        AppFile.initDirs();
        new JjccFetcher().run();
        LOGGER.info("Exit application.");
    }
}
