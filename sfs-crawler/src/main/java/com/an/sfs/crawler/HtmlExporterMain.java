package com.an.sfs.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlExporterMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlExporterMain.class);
    private static final String INPUT_ZXG = "ZXG.EBK";
    private static final String OUTPUT_ZXG = "ZXG.html";
    private static final String INPUT_GDRS = "ZXG_GDRS.EBK";
    private static final String OUTPUT_GDRS = "ZXG_GDRS.html";
    private static final String INPUT_GDRS_FINISHED = "ZXG_GDRS_FINISHED.EBK";
    private static final String OUTPUT_GDRS_FINISHED = "ZXG_GDRS_FINISHED.html";

    public static void main(String[] args) {
        export(AppFilePath.getConfDir() + File.separator + INPUT_ZXG, OUTPUT_ZXG);
        export(AppFilePath.getConfDir() + File.separator + INPUT_GDRS, OUTPUT_GDRS);
        export(AppFilePath.getConfDir() + File.separator + INPUT_GDRS_FINISHED, OUTPUT_GDRS_FINISHED);
    }

    private static void export(String inputEbkFilePath, String outputFile) {
        List<String> stockCodeList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputEbkFilePath))) {
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
