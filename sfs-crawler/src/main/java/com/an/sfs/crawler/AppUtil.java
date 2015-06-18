package com.an.sfs.crawler;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.name.StockLoader;

public class AppUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppUtil.class);
    public static final String INVALID = "--";
    public static final String UNIT_WAN = "万";
    public static final String UNIT_YI = "亿";

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
     * @param stockCodeList
     * @param appendInfoList
     *            [ {code -> info}, {code -> info}]
     * @param fileName
     */
    public static void exportHtml(List<String> stockCodeList, List<Map<String, String>> appendInfoList, String fileName) {
        StringBuilder text = new StringBuilder();
        text.append("<html>\n");
        text.append("<head><meta charset=\"utf-8\"></head>\n");
        text.append("<body>\n");
        StockLoader inst = StockLoader.getInst();
        for (String code : stockCodeList) {
            String newCode = code;
            if (code.startsWith("6")) {
                newCode = "sh" + code;
            } else {
                newCode = "sz" + code;
            }
            String url = "<a href=\"http://f10.eastmoney.com/f10_v2/ShareholderResearch.aspx?code=%s\">%s</a>";
            text.append(String.format(url, newCode, code));
            String name = inst.getName(code);
            text.append(name);
            if (appendInfoList != null && !appendInfoList.isEmpty()) {
                for (Map<String, String> infoMap : appendInfoList) {
                    if (infoMap.containsKey(code)) {
                        String info = infoMap.get(code);
                        text.append(" | ").append(info);
                    }
                }
            }
            text.append("<br>\n");
        }
        text.append("</body>\n");
        text.append("</html>");
        String filePath = AppFilePath.getOutputDir() + File.separator + fileName;
        FileUtil.writeFile(filePath, text.toString());
        LOGGER.info("Write file {}", filePath);
    }

    public static void exportTxt(List<String> stockCodeList, String fileName) {
        StringBuilder text = new StringBuilder();
        for (String code : stockCodeList) {
            text.append(code + "\n");
        }
        String filePath = AppFilePath.getOutputDir() + File.separator + fileName;
        FileUtil.writeFile(filePath, text.toString());
        LOGGER.info("Write file {}", filePath);
    }

    /**
     * @param list
     * @param rowCnt
     * @param filePath
     */
    public static void convertListToFile(List<String> list, int rowCnt, String filePath) {
        int columnCnt = list.size() / rowCnt;
        StringBuilder text = new StringBuilder();
        // 0*columnCnt+0,1*columnCnt+0,2*columnCnt+0
        // 0*columnCnt+1,1*columnCnt+1,2*columnCnt+1
        for (int colIdx = 0; colIdx < columnCnt; colIdx++) {
            for (int rowIdx = 0; rowIdx < rowCnt; rowIdx++) {
                if (rowIdx == rowCnt - 1) {
                    text.append(list.get(rowIdx * columnCnt + colIdx)).append("\n");
                } else {
                    text.append(list.get(rowIdx * columnCnt + colIdx)).append(";");
                }
            }
        }
        LOGGER.info("Save file {}", filePath);
        FileUtil.writeFile(filePath, text.toString());
    }

    /**
     * Current YJYG season
     */
    public static final String SEASON_YJYG = "2015-06-30";

    public static List<String> seasonList = new ArrayList<>();
    static {
        seasonList.add("2015-03-31");

        seasonList.add("2014-12-31");
        seasonList.add("2014-09-30");
        seasonList.add("2014-06-30");
        seasonList.add("2014-03-31");

        seasonList.add("2013-12-31");
        seasonList.add("2013-09-30");
        seasonList.add("2013-06-30");
        seasonList.add("2013-03-31");

        seasonList.add("2012-12-31");
        seasonList.add("2012-09-30");
        seasonList.add("2012-06-30");
        seasonList.add("2012-03-31");

        seasonList.add("2011-12-31");
        seasonList.add("2011-09-30");
        seasonList.add("2011-06-30");
        seasonList.add("2011-03-31");

        seasonList.add("2010-12-31");
        seasonList.add("2010-09-30");
        seasonList.add("2010-06-30");
        seasonList.add("2010-03-31");

        seasonList.add("2009-12-31");
        seasonList.add("2009-09-30");
        seasonList.add("2009-06-30");
        seasonList.add("2009-03-31");

        seasonList.add("2008-12-31");
        seasonList.add("2008-09-30");
        seasonList.add("2008-06-30");
        seasonList.add("2008-03-31");

        seasonList.add("2007-12-31");
        seasonList.add("2007-09-30");
        seasonList.add("2007-06-30");
        seasonList.add("2007-03-31");

        seasonList.add("2006-12-31");
        seasonList.add("2006-09-30");
        seasonList.add("2006-06-30");
        seasonList.add("2006-03-31");

        seasonList.add("2005-12-31");
        seasonList.add("2005-09-30");
        seasonList.add("2005-06-30");
        seasonList.add("2005-03-31");
    }

    /**
     * @param date
     * @return
     */
    public static boolean isValidDate(String date) {
        return seasonList.contains(date);
    }
}
