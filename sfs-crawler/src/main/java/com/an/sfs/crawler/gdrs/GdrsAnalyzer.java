/**
 * 
 */
package com.an.sfs.crawler.gdrs;

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

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.AppUtil;
import com.an.sfs.crawler.FileUtil;
import com.an.sfs.crawler.name.StockLoader;

/**
 * @author Anthony
 *
 */
public class GdrsAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(GdrsAnalyzer.class);
    private static final String FLAG_GDRS = "<strong>股东人数</strong>";
    private static final String FLAG_GDRS_DATA = "tips-dataL\">";
    private Map<String, String> stockNameMap = new HashMap<>();

    public void run() {
        analyzeGdrs();
        saveStockName();
    }

    private void saveStockName() {
        StringBuilder text = new StringBuilder();
        List<String> codeList = new ArrayList<>();
        codeList.addAll(stockNameMap.keySet());
        Collections.sort(codeList);

        for (String code : codeList) {
            text.append(code).append(";").append(stockNameMap.get(code)).append("\n");
        }

        String fn = StockLoader.getStockFile();
        FileUtil.writeFile(fn, text.toString());
    }

    private void analyzeGdrs() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFilePath.getInputGdyjDir(), files);
        for (File f : files) {
            LOGGER.info("Process file {}", f.getPath());
            String code = FileUtil.getFileName(f.getPath());

            boolean beginGdrs = false;
            boolean finishGdrs = false;
            boolean finishName = false;
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (!finishName) {
                        int idx = line.indexOf("(" + code + ")");
                        if (idx != -1) {
                            String name = line.substring(0, idx).trim();
                            stockNameMap.put(code, name);
                            finishName = true;
                        }
                    }

                    if (!finishGdrs && line.indexOf(FLAG_GDRS) != -1) {
                        beginGdrs = true;
                        continue;
                    }
                    if (!finishGdrs && beginGdrs && line.indexOf("<table") != -1) {
                        processGdrs(line, code);
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
            int fromIdx = curLine.indexOf(FLAG_GDRS_DATA);
            if (fromIdx == -1) {
                break;
            }

            fromIdx += FLAG_GDRS_DATA.length();
            int toIdx = curLine.indexOf("</", fromIdx);
            String item = curLine.substring(fromIdx, toIdx);
            if (item.indexOf(",") != -1) {
                item = item.replaceAll(",", "");
            }
            list.add(item);
            curLine = curLine.substring(toIdx);
        }

        String filePath = AppFilePath.getOutputGdyjGdrsDir() + File.separator + stockCode + ".txt";
        LOGGER.info("Save file {}", filePath);
        AppUtil.convertListToFile(list, 10, filePath);
    }
}
