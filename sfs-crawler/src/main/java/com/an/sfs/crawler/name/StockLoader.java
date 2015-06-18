package com.an.sfs.crawler.name;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;

public class StockLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockLoader.class);
    private List<StockVo> stockList = new ArrayList<>();
    private Map<String, StockVo> stockMap = new HashMap<>();

    public static String getStockFile() {
        return AppFilePath.getOutputDir() + File.separator + "StockName.txt";
    }

    /**
     * @param code
     * @return
     */
    public String getName(String code) {
        if (stockMap.containsKey(code)) {
            return stockMap.get(code).getName();
        }
        return null;
    }

    public List<StockVo> getStocks() {
        return stockList;
    }

    private void init() {
        String fn = getStockFile();
        try (BufferedReader br = new BufferedReader(new FileReader(fn))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] strs = line.split(";");
                    String code = strs[0];
                    String name = strs[1];
                    int type = 0;
                    if (code.startsWith("6")) {
                        type = 1;
                    } else if (code.startsWith("0")) {
                        type = 2;
                    } else if (code.startsWith("3")) {
                        type = 3;
                    }
                    StockVo vo = new StockVo(type, code, name);
                    stockList.add(vo);
                    stockMap.put(code, vo);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error ", e);
        }
    }

    //
    public static StockLoader getInst() {
        if (inst == null) {
            inst = new StockLoader();
            inst.init();
        }
        return inst;
    }

    private static StockLoader inst = null;

    private StockLoader() {
    }
}
