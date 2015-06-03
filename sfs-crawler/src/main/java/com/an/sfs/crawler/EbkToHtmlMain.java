package com.an.sfs.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EbkToHtmlMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(EbkToHtmlMain.class);

    public static void main(String[] args) {
        run();
    }

    private static void run() {
        List<File> outFileList = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFilePath.getConfDir(), ".EBK", outFileList);
        for (File f : outFileList) {
            String filePath = f.getPath();
            String outputFileName = FileUtil.getFileName(filePath) + ".html";
            export(filePath, outputFileName);
        }
    }

    private static void export(String ebkFilePath, String outputFile) {
        List<String> stockCodeList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ebkFilePath))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String code = line.substring(1);
                    stockCodeList.add(code);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error ", e);
        }
        AppUtil.exportHtml(stockCodeList, outputFile);
        LOGGER.error("Save file {}", outputFile);
    }
}
