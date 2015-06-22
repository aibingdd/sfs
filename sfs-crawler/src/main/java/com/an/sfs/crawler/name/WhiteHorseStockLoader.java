package com.an.sfs.crawler.name;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;

public class WhiteHorseStockLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhiteHorseStockLoader.class);
    private Set<String> stockSet = new HashSet<>();
    private List<String> stockList = new ArrayList<>();

    public List<String> getStockList() {
        return stockList;
    }

    public boolean isWhiteHorse(String stockCode) {
        return stockSet.contains(stockCode);
    }

    private void init() {
        String fp = AppFilePath.getConfDir() + File.separator + "WhiteHorse.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fp))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] strs = line.split(",");
                stockList.add(strs[0]);
            }
        } catch (IOException e) {
            LOGGER.error("Error ", e);
        }

        stockSet.addAll(stockList);
    }

    private WhiteHorseStockLoader() {
    }

    private static WhiteHorseStockLoader inst;

    public static WhiteHorseStockLoader getInst() {
        if (inst == null) {
            inst = new WhiteHorseStockLoader();
            inst.init();
        }
        return inst;
    }
}
