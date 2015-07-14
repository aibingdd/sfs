package com.an.sfs.crawler.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppUtil.class);
    public static final String INVALID = "--";
    public static final String UNIT_WAN = "万";
    public static final String UNIT_YI = "亿";

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
            LOGGER.info("Save web page {}", path.toString());
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

    /**
     * @param httpUrl
     * @param filePath
     */
    public static void download_rest(String httpUrl, String filePath) {
        BufferedReader br = null;
        FileWriter fw = null;
        try {
            URL url = new URL(httpUrl);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            String contentType = conn.getContentType();
            int contentLength = conn.getContentLength();
            System.out.println(contentType);
            System.out.println(contentLength);
            InputStream is = conn.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, "bg2312"));

            StringBuilder text = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                String newLine = line.replaceAll("><", ">\n<");
                text.append(newLine);
            }
            fw = new FileWriter(filePath);
            fw.write(text.toString());
        } catch (Exception e) {
            LOGGER.error("Error ", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
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
