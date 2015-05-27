package com.an.sfs.crawler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppUtil.class);

    /**
     * Crawl URL and save content to file.
     * 
     * @param httpUrl
     *            the URL to be crawled.
     * @param filePath
     *            saved file path.
     */
    /**
     * @param httpUrl
     * @param filePath
     */
    public static void download(String httpUrl, String filePath) {
        Path path = new File(filePath).toPath();
        InputStream is = null;
        try {
            URL url = new URL(httpUrl);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Nutch");
            is = url.openStream();
            Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("Save file {}", path.toString());
        } catch (IOException e) {
            LOGGER.error("Error while fetching web page by URL {}", httpUrl, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("Error.", e);
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.error("Error.", e);
            }
        }
    }
}
