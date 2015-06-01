package com.an.sfs.crawler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

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

    /**
     * @param stockCodeList
     * @param fileName
     */
    public static void exportHtml(List<String> stockCodeList, String fileName) {
        StringBuilder text = new StringBuilder();
        text.append("<html>\n");
        text.append("<head><meta charset=\"utf-8\"></head>\n");
        text.append("<body>\n");
        StockCodeNameLoader inst = StockCodeNameLoader.getInst();
        for (String code : stockCodeList) {
            String newCode = code;
            if (code.startsWith("6")) {
                newCode = "sh" + code;
            } else {
                newCode = "sz" + code;
            }
            String url = "<a href=\"http://f10.eastmoney.com/f10_v2/ShareholderResearch.aspx?code=%s\">%s</a>    ";
            text.append(String.format(url, newCode, code));
            String name = inst.getName(code);
            text.append(name + "<br>");
        }
        text.append("</body>\n");
        text.append("</html>");
        String filePath = AppFilePath.getOutputDir() + File.separator + fileName;
        FileUtil.writeFile(filePath, text.toString());
        LOGGER.info("Write file {}", filePath);
    }
}
