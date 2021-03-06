package com.an.sfs.crawler.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.cwfx.ReportVo;
import com.an.sfs.crawler.gdyj.GdrsReportVo;
import com.an.sfs.crawler.name.IndustryLoader;
import com.an.sfs.crawler.tdx.StockLoader;
import com.an.sfs.crawler.tdx.StockVo;

public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
    public static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("00.00%");
    public static final DecimalFormat PERCENT_3_FORMAT = new DecimalFormat("000.00%");
    public static final DecimalFormat FLOAT_FORMAT = new DecimalFormat("00.00");
    /**
     * 123 -> 000123
     */
    public static final String INT_6_FORMAT = String.format("%06d", 123);

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
            LOGGER.info("Save file {}", filePath);
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
     * @param filepath
     * @param content
     * @param writeType
     *            0-ignore exists<br>
     *            1-create new<br>
     *            2-append
     * @param createEmptyFile
     *            true-create file while empty content
     * @throws IOException
     */
    public static void writeFile(String filepath, String content, int writeType, boolean createEmptyFile)
            throws IOException {
        if (filepath == null || filepath.isEmpty()) {
            throw new IllegalArgumentException("Invalid filePath " + filepath);
        }
        if (content == null || content.isEmpty()) {
            if (!createEmptyFile) {
                return;
            }
        }
        String dirName = filepath.substring(0, filepath.lastIndexOf(File.separator));
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(filepath);
        if (file.exists()) {
            if (writeType == 0) {
                return;
            } else if (writeType == 1) {
                file.delete();
                file.createNewFile();
            }
        } else {
            file.createNewFile();
        }

        if (!content.isEmpty()) {
            System.out.println("Write file: " + filepath);
            try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File(filepath)), "GB2312");) {
                osw.write(content);
                osw.flush();
            }
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
     * @param files
     */
    public static void getFilesUnderDir(String dirPath, List<File> files) {
        getFilesUnderDir(dirPath, null, null, files);
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
     * @param dirPath
     * @param files
     */
    public static void getSortedFilesUnderDir(String dirPath, List<File> files) {
        getSortedFilesUnderDir(dirPath, null, null, files);
    }

    public static void getSortedFilesUnderDir(String dirPath, String start, String end, List<File> outFiles) {
        List<SfsFile> sfsFileList = new ArrayList<>();
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
                    sfsFileList.add(new SfsFile(f));
                }
            }
        }

        Collections.sort(sfsFileList);
        for (SfsFile f : sfsFileList) {
            outFiles.add(f.getFile());
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
    public static void exportReport(List<ReportVo> reportVoList, String filePath, boolean displayIndustry) {
        StringBuilder text = new StringBuilder();
        text.append("<html>\n");
        text.append("<head><meta charset=\"utf-8\"></head>\n");
        text.append("<body>\n");

        for (int i = 0; i < 58; i++) {
            text.append("&nbsp");
        }
        text.append("ROTA&nbsp").append(" |&nbspRONA").append(" |&nbspN P&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp")
                .append(" | DTAR&nbsp").append(" | P E&nbsp&nbsp").append(" | PED").append(" | P B&nbsp&nbsp")
                .append(" | JGCC&nbsp&nbsp").append(" |地区").append(" | ROTA(14-13-12)").append(" | RONA(14-13-12)")
                .append(" | N P(14-13-12) ").append(" | 机构持仓").append("<br>\n");

        String industryUrl = "<a href=\"D:\\sfs_home\\output\\cwfx_rona\\Stock_Cwfx_Rona_%s%s.html\">%s</a>";
        String stockUrl = "<a href=\"http://f10.eastmoney.com/f10_v2/ShareholderResearch.aspx?code=%s%s\">%s</a>";

        for (ReportVo vo : reportVoList) {
            String code = vo.getCode();
            text.append(vo.getIndex()).append("&nbsp");
            String url = null;
            if (vo.isIndustry()) {
                url = String.format(industryUrl, code, vo.getName().trim(), code);
            } else {
                url = String.format(stockUrl, StockLoader.getTypeStr(code), code, code);
            }
            text.append(url).append("&nbsp");
            text.append(vo.getNameDisplayStr());
            StockVo stockVo = StockLoader.getInst().getStockVo(code);
            if (displayIndustry) {
                text.append("\t|").append(stockVo.getIndustryDisplay());
            }
            String peDynamic = "";
            if (stockVo != null) {
                peDynamic = stockVo.getPeDisplayStr();
            }
            text.append("\t|").append(vo.getRotaDisplayStr());
            text.append("\t|").append(vo.getRonaDisplayStr());
            text.append("\t|").append(vo.getNetProfitChangeRateDisplayStr());
            text.append("\t|").append(vo.getDtarDisplayStr());
            text.append("\t|").append(vo.getPeDisplayStr());
            text.append("\t|").append(peDynamic);
            text.append("\t|").append(vo.getPbDisplayStr());
            text.append("\t|").append(vo.getJgccRatioDisplayStr());
            text.append("\t|").append(vo.getRegion());
            text.append("\t|").append(vo.getRotaStr());
            text.append("\t|").append(vo.getRonaStr());
            text.append("\t|").append(vo.getNetProfitChangeStr());
            text.append("\t|").append(vo.getJgcc());
            text.append(vo.getNote());
            text.append("\t|").append(vo.getFhfa());

            text.append("<br>\n");
        }
        text.append("</body>\n");
        text.append("</html>");

        FileUtil.writeFile(filePath, text.toString());
    }

    public static void exportHtml(List<GdrsReportVo> reportList, String filePath) {
        StringBuilder content = new StringBuilder();
        content.append("<html>\n");
        content.append("<head><meta charset=\"utf-8\"></head>\n");
        content.append("<body>\n");

        content.append("股票代码").append(" | 排名").append(" | 名称").append(" | 人数变化").append(" | 股东人数").append(" | 流通股本")
                .append("<br>\n");

        int i = 1;
        for (GdrsReportVo rpt : reportList) {
            // 1. Code
            content.append(rpt.getDisplayCode());
            // 2. Rank
            content.append(" ").append(String.format("%04d", i++)).append(" ");
            // 3. Name
            content.append(rpt.getName());
            // 4. Count down rate
            content.append(" | " + rpt.getCountRateStr());
            // 5. GDRS Number
            content.append(" | " + rpt.getGdrsNum());
            // 6. Net float share
            content.append(" | " + rpt.getNetFloatShare());
            content.append("<br>\n");
        }
        content.append("</body>\n");
        content.append("</html>");

        FileUtil.writeFile(filePath, content.toString());
    }

    /**
     * @param codeList
     *            Stock Code or Industry Code
     * @param appendInfoList
     * @param filePath
     */
    public static void exportHtml(List<String> codeList, List<Map<String, String>> appendInfoList, String filePath) {
        StringBuilder text = new StringBuilder();
        text.append("<html>\n");
        text.append("<head><meta charset=\"utf-8\"></head>\n");
        text.append("<body>\n");

        text.append("净资产收益率").append(" | 总资产收益率").append(" | 资产负债率").append(" | 市盈率").append(" | 市净率")
                .append(" | 机构持仓").append("<br>\n");

        int i = 1;
        for (String code : codeList) {
            String url = "<a href=\"http://f10.eastmoney.com/f10_v2/ShareholderResearch.aspx?code=%s%s\">%s</a>";
            text.append(String.format(url, StockLoader.getTypeStr(code), code, code));

            String name = StockLoader.getInst().getStockName(code);
            if (name == null) {
                name = IndustryLoader.getInst().getIndustryName(code);
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

    public static long parseFloat(String str) {
        if (str.equals("0.00")) {
            return 0;
        } else if (str.endsWith("0")) {
            return (long) (((int) (Float.parseFloat(str) * 10)) * 1000L);
        } else {
            return (long) (((int) (Float.parseFloat(str) * 100)) * 100L);
        }
    }
}
