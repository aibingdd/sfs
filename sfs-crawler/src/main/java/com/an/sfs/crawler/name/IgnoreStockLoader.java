package com.an.sfs.crawler.name;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.util.AppFile;

public class IgnoreStockLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(IgnoreStockLoader.class);
    private Set<String> stockSet = new HashSet<>();

    public static String getStockFile() {
        return AppFile.getConfDir() + File.separator + "IgnoreStock.txt";
    }

    public boolean isIgnore(String stock) {
        return stockSet.contains(stock);
    }

    private void init() {
        String fn = getStockFile();
        try (BufferedReader br = new BufferedReader(new FileReader(fn))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    stockSet.add(line.trim());
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error ", e);
        }
    }

    //
    public static IgnoreStockLoader getInst() {
        if (inst == null) {
            inst = new IgnoreStockLoader();
            inst.init();
        }
        return inst;
    }

    private static IgnoreStockLoader inst = null;

    private IgnoreStockLoader() {
    }
}
