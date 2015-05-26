package com.an.sfs.crawler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebUtil.class);

    /**
     * Crawl URL and save content to file.
     * 
     * @param url
     *            the URL to be crawled.
     * @param filePath
     *            saved file path.
     */
    public static void crawl(String url, String filePath) {
        Path path = new File(filePath).toPath();
        try (InputStream is = new URL(url).openStream();) {
            Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("Save file {}", path.toString());
        } catch (IOException e) {
            LOGGER.error("Error while crawling URL {}", url, e);
        }
    }
}
