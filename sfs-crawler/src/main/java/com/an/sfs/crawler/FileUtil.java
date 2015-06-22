package com.an.sfs.crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.ccjg.CcjgLoader;
import com.an.sfs.crawler.cwfx.CwfxProfitUpLoader;
import com.an.sfs.crawler.gdyj.GdrsDownLoader;
import com.an.sfs.crawler.gsgk.GsgkLoader;
import com.an.sfs.crawler.gsgk.StockCodeLoader;
import com.an.sfs.crawler.name.IgnoreStockLoader;
import com.an.sfs.crawler.name.IndustryLoader;
import com.an.sfs.crawler.name.WhiteHorseStockLoader;

public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
    /**
     * "##.0%"
     */
    public static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("##.0%");
    /**
     * "##.0"
     */
    public static final DecimalFormat FLOAT_FORMAT = new DecimalFormat("##.00");
    /**
     * 123 -> 000123
     */
    public static final String INT_FORMAT = String.format("%06d", 123);

    /**
     * Save content to file.
     * 
     * @param filePath
     *            saved file path.
     * @param text
     *            file content.
     */
    public static void writeFile(String filePath, String text) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(filePath))) {
            out.write(text);
        } catch (IOException e) {
            LOGGER.error("Error, filePath {}", filePath, e);
        }
    }

    /**
     * @param filePath
     * @param text
     * @param append
     */
    public static void writeFile(String filePath, String text, boolean append) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(filePath, append))) {
            out.write(text);
        } catch (IOException e) {
            LOGGER.error("Error, filePath {}", filePath, e);
        }
    }

    /**
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * @param dirPath
     * @param outFileList
     */
    public static void getFilesUnderDir(String dirPath, List<File> outFileList) {
        getFilesUnderDir(dirPath, null, null, outFileList);
    }

    /**
     * @param dirPath
     * @param type
     * @param outFileList
     */
    public static void getFilesUnderDir(String dirPath, String start, String end, List<File> outFileList) {
        File dir = new File(dirPath);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File f : files) {
                if (f.isFile()) {
                    String filePath = f.getPath();
                    String fileNameName = FileUtil.getFileNameFull(filePath);
                    if (start != null && !fileNameName.startsWith(start)) {
                        continue;
                    }
                    if (end != null && !fileNameName.endsWith(end)) {
                        continue;
                    }
                    outFileList.add(f);
                }
            }
        }
    }

    /**
     * @param filePath
     * @return File name without suffix.
     */
    public static String getFileName(String filePath) {
        int beginIndex = filePath.lastIndexOf(File.separator);
        String fileName = filePath.substring(beginIndex + 1, filePath.indexOf("."));
        return fileName;
    }

    /**
     * @param filePath
     * @return File name with suffix.
     */
    public static String getFileNameFull(String filePath) {
        int beginIndex = filePath.lastIndexOf(File.separator);
        String fileName = filePath.substring(beginIndex + 1);
        return fileName;
    }

    public static String getHttpUrlFileName(String httpUrl) {
        int beginIndex = httpUrl.lastIndexOf("/");
        String fileName = httpUrl.substring(beginIndex + 1, httpUrl.lastIndexOf("."));
        return fileName;
    }

    public static String getHttpUrlFileNameFull(String httpUrl) {
        int beginIndex = httpUrl.lastIndexOf("/");
        String fileName = httpUrl.substring(beginIndex + 1);
        return fileName;
    }

    /**
     * Format html file
     * 
     * @param filePath
     */
    public static void formatHtmlFile(String filePath) {
        StringBuilder text = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("><", ">\n<");
                text.append(line).append("\n");
            }
        } catch (IOException e) {
            LOGGER.error("Error ", e);
        }
        FileUtil.writeFile(filePath, text.toString());
    }

    /**
     * @param outputDir
     * @param filePath
     * @param encoding
     */
    public static void formatHtmlFile(String outputDir, String outputFileType, String filePath, String encoding) {
        StringBuilder text = new StringBuilder();
        BufferedReader br = null;
        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            Reader isr = new InputStreamReader(fis, encoding);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("  ", "");
                line = line.replaceAll("><", ">\n<");
                text.append(line).append("\n");
            }
        } catch (IOException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        String fn = FileUtil.getFileName(filePath);
        FileUtil.writeFile(outputDir + File.separator + fn + outputFileType, text.toString());
    }

    /**
     * @param line
     *            like <th class="tips-dataR">2015-05-13</th>
     * @return
     */
    public static String extractVal(String line) {
        int startIndex = line.indexOf(">");
        int endIndex = line.indexOf("<", startIndex);
        if (startIndex != -1 && endIndex != -1) {
            String val = line.substring(startIndex + 1, endIndex);
            return val;
        }
        return null;
    }

    /**
     * @param stockCodeList
     * @param appendInfoList
     *            [ {code -> info}, {code -> info}]
     * @param fileName
     */
    public static void exportHtml(List<String> stockCodeList, List<Map<String, String>> appendInfoList, String filePath) {
        StringBuilder text = new StringBuilder();
        text.append("<html>\n");
        text.append("<head><meta charset=\"utf-8\"></head>\n");
        text.append("<body>\n");

        text.append("摊薄净资产收益率").append(" | 摊薄总资产收益率").append(" | 资产负债率").append(" | 机构持仓").append(" | 市盈率")
                .append(" | 市净率").append("<br>\n");

        int i = 1;
        for (String stock : stockCodeList) {
            String url = "<a href=\"http://f10.eastmoney.com/f10_v2/ShareholderResearch.aspx?code=%s%s\">%s</a>";
            text.append(String.format(url, StockCodeLoader.getTypeStr(stock), stock, stock));

            String name = GsgkLoader.getInst().getName(stock);
            if (name == null) {
                name = IndustryLoader.getInst().getIndustryName(stock);
            }

            text.append(" ").append(String.format("%04d", i++)).append(" ");
            if (name.length() < 4) {
                for (int j = 0; j < 4 - name.length(); j++) {
                    text.append("&nbsp&nbsp&nbsp&nbsp");
                }
            }
            text.append(name);
            if (appendInfoList != null && !appendInfoList.isEmpty()) {
                for (Map<String, String> infoMap : appendInfoList) {
                    if (infoMap.containsKey(stock)) {
                        String info = infoMap.get(stock);
                        text.append(" | ").append(info);
                    }
                }
            }

            long total = CcjgLoader.getInst().getTotal(stock);
            text.append(" | " + FLOAT_FORMAT.format((float) total / 10000f) + "万");

            if (IgnoreStockLoader.getInst().isIgnore(stock)) {
                text.append(" | IGNORE");
            } else if (WhiteHorseStockLoader.getInst().isWhiteHorse(stock)) {
                text.append(" | WHITEHORSE");
            }

            if (GdrsDownLoader.getInst().isGdrsDown(stock)) {
                text.append(" | GDRS");
            }
            if (CwfxProfitUpLoader.getInst().isProfitUp(stock)) {
                text.append(" | PROFIT");
            }

            text.append("<br>\n");
        }
        text.append("</body>\n");
        text.append("</html>");

        LOGGER.info("Write file {}", filePath);
        FileUtil.writeFile(filePath, text.toString());
    }

    /**
     * @param stockList
     * @param filePath
     */
    public static void exportStock(List<String> stockList, String filePath) {
        StringBuilder text = new StringBuilder();
        for (String code : stockList) {
            text.append(code + "\n");
        }

        LOGGER.info("Write file {}", filePath);
        FileUtil.writeFile(filePath, text.toString());
    }

    /**
     * @param list
     * @param rowCnt
     * @param filePath
     */
    public static void convertListToText(List<String> list, int rowCnt, StringBuilder text) {
        int columnCnt = list.size() / rowCnt;
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
    }
}
