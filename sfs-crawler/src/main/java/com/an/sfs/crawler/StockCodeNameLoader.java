package com.an.sfs.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockCodeNameLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockCodeNameLoader.class);
    public static final String FILE_STOCK_CODE_NAME = "stockCodeName.txt";
    private Map<String, String> codeNameMap = new HashMap<>();

    private void init() {
        try (BufferedReader br = new BufferedReader(new FileReader(AppFilePath.getOutputDir() + File.separator
                + FILE_STOCK_CODE_NAME))) {

            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] strs = line.split(";");
                    codeNameMap.put(strs[0], strs[1]);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error ", e);
        }
    }

    /**
     * @param code
     * @return
     */
    public String getName(String code) {
        return codeNameMap.get(code);
    }

    //
    public static StockCodeNameLoader getInst() {
        if (inst == null) {
            inst = new StockCodeNameLoader();
            inst.init();
        }
        return inst;
    }

    private static StockCodeNameLoader inst = null;

    private StockCodeNameLoader() {
    }
}
