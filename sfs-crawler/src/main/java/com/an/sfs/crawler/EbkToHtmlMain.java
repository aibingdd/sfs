package com.an.sfs.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EbkToHtmlMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(EbkToHtmlMain.class);

    public static void main(String[] args) {
        AppFilePath.initDirs();
        run();
    }

    private static void run() {
        List<File> outFileList = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFilePath.getConfDir(), null, ".EBK", outFileList);
        for (File f : outFileList) {
            String ebkFilePath = f.getPath();
            String fileName = FileUtil.getFileName(ebkFilePath);
            export(ebkFilePath, fileName);
        }
    }

    private static void export(String ebkFilePath, String fileName) {
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

        Collections.sort(stockCodeList);

        String txt = AppFilePath.getOutputDir() + File.separator + fileName + ".txt";
        String html = AppFilePath.getOutputDir() + File.separator + fileName + ".html";

        FileUtil.exportStock(stockCodeList, txt);
        FileUtil.exportHtml(stockCodeList, null, html);
    }
}
