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
import com.an.sfs.crawler.name.StockVo;

public class GdyjFetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(GdyjFetcher.class);
    // Gu dong yan jiu
    // sh600000 or sz000001
    private static final String GDYJ_URL = "http://f10.eastmoney.com/f10_v2/ShareholderResearch.aspx?code=%s%s";

    private static final String FLAG_GDRS = "<strong>股东人数</strong>";
    private static final String FLAG_GDRS_DATA = "tips-dataL";

    private List<String> stockCodeList = new ArrayList<>();

    public void run() {
        LOGGER.info("Load stock code ...");
        loadStockCode();
        LOGGER.info("Fetch ...");
        fetchHtml(GDYJ_URL);
        LOGGER.info("Analyze ...");
        analyze();
    }

    private void fetchHtml(String url) {
        for (String code : stockCodeList) {
            String typeStr = StockVo.getTypeStr(code);
            String httpUrl = String.format(url, typeStr, code);
            String filePath = AppFilePath.getInputGdyjRawDir() + File.separator + code + ".html";
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
        boolean finishName = false;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!finishName) {
                    int idx = line.indexOf("(" + stock + ")");
                    if (idx != -1) {
                        String name = line.substring(0, idx).trim();
                        // stockNameMap.put(code, name);
                        finishName = true;
                    }
                }

                if (!finishGdrs && line.indexOf(FLAG_GDRS) != -1) {
                    beginGdrs = true;
                    continue;
                }
                if (!finishGdrs && beginGdrs && line.indexOf("<table") != -1) {
                    String text = line.trim();
                    text = text.replaceAll("><", ">\n<");
                    String fp = AppFilePath.getInputGdyjTxtDir() + File.separator + stock + ".txt";
                    FileUtil.writeFile(fp, text);
                    finishGdrs = true;
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error {}", filePath, e);
        }
    }

    private void analyze() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFilePath.getInputGdyjTxtDir(), files);
        for (File f : files) {
            String stockCode = FileUtil.getFileName(f.getPath());
            List<String> list = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (line.contains(FLAG_GDRS_DATA)) {
                        list.add(FileUtil.extractVal(line));
                    }
                }

                String fp = AppFilePath.getOutputGdyjDir() + File.separator + stockCode + ".txt";
                StringBuilder text = new StringBuilder();
                FileUtil.convertListToText(list, 10, text);
                LOGGER.info("Save file {}", fp);
                FileUtil.writeFile(fp, text.toString());
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }
        }
    }

    private void loadStockCode() {
        try (BufferedReader br = new BufferedReader(new FileReader(AppFilePath.getConfFile("SH.EBK")));) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    stockCodeList.add(line.trim().substring(1));
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while parsing SH.EBK.");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(AppFilePath.getConfFile("SZ.EBK")));) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    stockCodeList.add(line.trim().substring(1));
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while parsing SZ.EBK.");
        }
    }
}
