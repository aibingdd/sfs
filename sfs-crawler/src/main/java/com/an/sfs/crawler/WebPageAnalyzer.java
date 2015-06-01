/**
 * 
 */
package com.an.sfs.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Anthony
 *
 */
public class WebPageAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebPageAnalyzer.class);
    private static final String FLAG_GDRS = "<strong>股东人数</strong>";
    private static final String FLAG_DATA = "tips-dataL\">";
    private Map<String, String> stockCodeNameMap = new HashMap<>();

    public void run() {
        analyzeGdyj();

        saveCodeName();
    }

    private void saveCodeName() {
        StringBuilder text = new StringBuilder();
        List<String> codeList = new ArrayList<>();
        codeList.addAll(stockCodeNameMap.keySet());
        Collections.sort(codeList);
        for (String code : codeList) {
            text.append(code).append(";").append(stockCodeNameMap.get(code)).append("\n");
        }

        FileUtil.writeFile(AppFilePath.getOutputDir() + File.separator + StockCodeNameLoader.FILE_STOCK_CODE_NAME,
                text.toString());
    }

    private void analyzeGdyj() {
        List<File> fileList = new ArrayList<>();
        String dirPath = AppFilePath.getInputGdyjDir();
        FileUtil.getFilesUnderDir(dirPath, fileList);
        for (File f : fileList) {
            LOGGER.info("Process file {}", f.getPath());
            String stockCode = f.getName();
            stockCode = stockCode.substring(0, stockCode.indexOf("."));

            boolean beginGdrs = false;
            boolean finishGdrs = false;
            boolean finishName = false;
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (!finishName) {
                        int idx = line.indexOf("(" + stockCode + ")");
                        if (idx != -1) {
                            String name = line.substring(0, idx).trim();
                            stockCodeNameMap.put(stockCode, name);
                            finishName = true;
                        }
                    }

                    if (!finishGdrs && line.indexOf(FLAG_GDRS) != -1) {
                        beginGdrs = true;
                        continue;
                    }
                    if (!finishGdrs && beginGdrs && line.indexOf("<table") != -1) {
                        processGdrs(line, stockCode);
                        finishGdrs = true;
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Error while analyzing file {}", f.getPath());
            }
        }
    }

    private void processGdrs(String line, String stockCode) {
        List<String> list = new ArrayList<>();
        String curLine = line;
        while (true) {

            int fromIdx = curLine.indexOf(FLAG_DATA);
            if (fromIdx == -1) {
                break;
            }

            fromIdx += FLAG_DATA.length();
            int toIdx = curLine.indexOf("</", fromIdx);
            String item = curLine.substring(fromIdx, toIdx);
            if (item.indexOf(",") != -1) {
                item = item.replaceAll(",", "");
            }
            list.add(item);
            curLine = curLine.substring(toIdx);
        }
        if (stockCode.equals("000166")) {
            LOGGER.error("file name {}", stockCode);
        }
        int rowCnt = 10;
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
        String filePath = AppFilePath.getOutputGdyjGdrsDir() + File.separator + stockCode + ".txt";
        LOGGER.info("Save file {}", filePath);
        FileUtil.writeFile(filePath, text.toString());
    }
}
