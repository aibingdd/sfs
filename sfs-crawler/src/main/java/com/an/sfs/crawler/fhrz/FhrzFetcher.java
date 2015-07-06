package com.an.sfs.crawler.fhrz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.AppUtil;
import com.an.sfs.crawler.FileUtil;
import com.an.sfs.crawler.tdx.StockLoader;

public class FhrzFetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(FhrzFetcher.class);
    // sh600830
    private static final String URL = "http://f10.eastmoney.com/f10_v2/BonusFinancing.aspx?code=%s%s";

    public void run() {
        fetchRawHtml(URL, AppFilePath.getInputFhrzRawDir());
        LOGGER.info("Extract FHFA.");
        extractFhfa();
        LOGGER.info("Extract ZFMX.");
        extractZfmx();
        LOGGER.info("Analyze FHFA.");
        analyzeFhfa();
    }

    private void fetchRawHtml(String url, String fileDir) {
        List<String> stockCodeList = StockLoader.getInst().getStockCodeList();
        for (String stock : stockCodeList) {
            String typeStr = StockLoader.getTypeStr(stock);
            String httpUrl = String.format(url, typeStr, stock);
            String filePath = fileDir + File.separator + stock + ".html";
            if (!FileUtil.isFileExist(filePath)) {
                AppUtil.download(httpUrl, filePath);
            }
        }
    }

    private void extractFhfa() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFilePath.getInputFhrzRawDir(), files);
        for (File f : files) {
            String filePath = f.getPath();
            String fileName = FileUtil.getFileName(filePath);
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (line.contains("BonusDetailsTable")) {
                        String text = line.trim().replaceAll("><", ">\n<");
                        String fp = AppFilePath.getInputFhrzFhfaDir() + File.separator + fileName + ".txt";
                        FileUtil.writeFile(fp, text);
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }
        }
    }

    private void extractZfmx() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFilePath.getInputFhrzRawDir(), files);
        for (File f : files) {
            String filePath = f.getPath();
            String fileName = FileUtil.getFileName(filePath);
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line = null;
                String text = null;
                while ((line = br.readLine()) != null) {
                    if (line.contains("增发时间")) {
                        text = line.trim().replaceAll("><", ">\n<");
                        if (text.endsWith("</table>")) {
                            break;
                        }
                    }
                    if (text != null) {
                        text = text + line.trim();
                        if (text.endsWith("</table>")) {
                            break;
                        }
                    }
                }
                if (text != null) {
                    String fp = AppFilePath.getInputFhrzZfmxDir() + File.separator + fileName + ".txt";
                    FileUtil.writeFile(fp, text);
                }
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }
        }
    }

    private void analyzeFhfa() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFilePath.getInputFhrzFhfaDir(), files);
        for (File f : files) {
            try (BufferedReader br = new BufferedReader(new FileReader(f.getPath()))) {
                String line = null;
                boolean foundDate = false;
                StringBuilder text = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    if (line.contains("bonusTime")) {
                        String date = FileUtil.extractVal(line);
                        foundDate = true;
                        text.append(date).append(";");
                        continue;
                    }
                    if (foundDate) {
                        String val = FileUtil.extractVal(line);
                        text.append(val).append("\n");
                        foundDate = false;
                    }
                }
                String fileName = FileUtil.getFileNameFull(f.getPath());
                FileUtil.writeFile(AppFilePath.getOutputFhfaDir() + File.separator + fileName, text.toString());
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }
        }
    }
}
