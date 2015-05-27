/**
 * 
 */
package com.an.sfs.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public WebPageAnalyzer() {
    }

    public void run() {
        analyzeGdyj();
    }

    private void analyzeGdyj() {
        List<File> fileList = new ArrayList<>();
        String dirPath = AppFilePath.getInputGdyjDir();
        FileUtil.getFilesUnderDir(dirPath, fileList);
        for (File f : fileList) {
            LOGGER.info("Process file {}", f.getPath());
            String fileName = f.getName();
            fileName = fileName.substring(0, fileName.indexOf("."));

            boolean beginGdrs = false;
            boolean finishGdrs = false;
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (!finishGdrs && line.indexOf(FLAG_GDRS) != -1) {
                        beginGdrs = true;
                        continue;
                    }
                    if (!finishGdrs && beginGdrs && line.indexOf("<table") != -1) {
                        processGdrs(line, fileName);
                        finishGdrs = true;
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Error while analyzing file {}", f.getPath());
            }
        }
    }

    private void processGdrs(String line, String fileName) {
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
            list.add(item);
            curLine = curLine.substring(toIdx);
        }
        int rowCnt = 10;
        int quarterCnt = list.size() / rowCnt;
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < quarterCnt; i++) {
            for (int j = 0; j < rowCnt; j++) {
                if (j == rowCnt - 1) {
                    text.append(list.get(i + j)).append("\n");
                } else {
                    text.append(list.get(i + j)).append(";");
                }
            }
        }

        String filePath = AppFilePath.getOutputGdyjDir() + File.separator + fileName + ".txt";
        LOGGER.info("Save file {}", filePath);
        FileUtil.writeFile(filePath, text.toString());
    }
}
