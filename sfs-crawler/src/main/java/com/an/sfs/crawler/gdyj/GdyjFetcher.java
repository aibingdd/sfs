package com.an.sfs.crawler.gdyj;

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

public class GdyjFetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(GdyjFetcher.class);
    // Gu dong yan jiu
    // sh600000 or sz000001
    private static final String GDYJ_URL = "http://f10.eastmoney.com/f10_v2/ShareholderResearch.aspx?code=%s%s";

    private static final String FLAG_GDRS = "<strong>股东人数</strong>";
    private static final String FLAG_SDLTGD = "<strong>十大流通股东</strong>";

    private static final String FLAG_DATA = "tips-dataL";

    public void run() {
        LOGGER.info("Fetch ...");
        fetchHtml(GDYJ_URL);
        LOGGER.info("Analyze ...");
        analyzeGdrs();
        analyzeSdltgd();
    }

    private void fetchHtml(String url) {
        List<String> stockList = StockLoader.getInst().getStockCodeList();
        for (String stock : stockList) {
            String typeStr = StockLoader.getTypeStr(stock);
            String httpUrl = String.format(url, typeStr, stock);
            String filePath = AppFilePath.getInputGdyjRawDir() + File.separator + stock + ".html";
            if (!FileUtil.isFileExist(filePath)) {
                AppUtil.download(httpUrl, filePath);
            }

            extract(filePath);
        }
    }

    private void extract(String filePath) {
        String stock = FileUtil.getFileName(filePath);
        boolean beginGdrs = false;
        boolean finishGdrs = false;
        boolean beginSdltgd = false;
        boolean finishSdltgd = false;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!finishGdrs) {
                    if (line.indexOf(FLAG_GDRS) != -1) {
                        beginGdrs = true;
                        continue;
                    }
                    if (beginGdrs && line.contains(FLAG_DATA)) {
                        String text = line.trim();
                        text = text.replaceAll("><", ">\n<");
                        String fp = AppFilePath.getInputGdyjGdrsDir() + File.separator + stock + ".txt";
                        FileUtil.writeFile(fp, text);
                        finishGdrs = true;
                        continue;
                    }
                }

                if (!finishSdltgd) {
                    if (line.indexOf(FLAG_SDLTGD) != -1) {
                        beginSdltgd = true;
                        continue;
                    }
                    if (beginSdltgd && line.contains(FLAG_DATA)) {
                        String text = line.trim();
                        text = text.replaceAll("><", ">\n<");
                        String fp = AppFilePath.getInputGdyjSdltgdDir() + File.separator + stock + ".txt";
                        FileUtil.writeFile(fp, text);
                        finishSdltgd = true;
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error {}", filePath, e);
        }
    }

    private void analyzeGdrs() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFilePath.getInputGdyjGdrsDir(), files);
        for (File f : files) {
            String stock = FileUtil.getFileName(f.getPath());
            List<String> list = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (line.contains(FLAG_DATA)) {
                        list.add(FileUtil.extractVal(line));
                    }
                }

                StringBuilder text = new StringBuilder();
                FileUtil.convertListToText(list, 10, text);
                String fp = AppFilePath.getOutputGdyjGdrsDir() + File.separator + stock + ".txt";
                FileUtil.writeFile(fp, text.toString());
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }
        }
    }

    private void analyzeSdltgd() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFilePath.getInputGdyjSdltgdDir(), files);
        for (File f : files) {
            String stock = FileUtil.getFileName(f.getPath());
            StringBuilder text = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line = null;
                int i = 0;
                while ((line = br.readLine()) != null) {
                    if (line.contains("合计")) {
                        break;
                    }
                    if (line.contains(FLAG_DATA)) {
                        String val = FileUtil.extractVal(line);
                        if (val != null) {
                            text.append(val);
                            if ((i + 1) % 7 == 0) {
                                text.append("\n");
                            } else {
                                text.append(";");
                            }
                            i++;
                        }
                    }
                }
                String fp = AppFilePath.getOutputGdyjSdltgdDir() + File.separator + stock + ".txt";
                FileUtil.writeFile(fp, text.toString());
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }
        }
    }
}
