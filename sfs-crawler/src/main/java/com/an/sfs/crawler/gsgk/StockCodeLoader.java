package com.an.sfs.crawler.gsgk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.gdyj.GdyjFetcher;

public class StockCodeLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GdyjFetcher.class);
    private List<String> stockCodeList = new ArrayList<>();

    public static String getTypeStr(String stockCode) {
        if (stockCode.startsWith("6")) {
            return "sh";
        } else {
            return "sz";
        }
    }

    public static String getCodeSuffix(String stockCode) {
        if (stockCode.startsWith("6")) {
            return "01";
        } else {
            return "02";
        }
    }

    public List<String> getStockCodeList() {
        return stockCodeList;
    }

    private void init() {
        LOGGER.info("Load stock code ...");
        loadStockCode();
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

    private StockCodeLoader() {
    }

    private static StockCodeLoader inst;

    public static StockCodeLoader getInst() {
        if (inst == null) {
            inst = new StockCodeLoader();
            inst.init();
        }
        return inst;
    }
}
