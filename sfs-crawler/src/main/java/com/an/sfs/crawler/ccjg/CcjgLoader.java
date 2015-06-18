package com.an.sfs.crawler.ccjg;

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
import com.an.sfs.crawler.FileUtil;

public class CcjgLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CcjgLoader.class);
    // "2015-03-31"
    private static final String CUR_SEASON = "2015-03-31";
    private Map<String, List<String>> stockFundMap = new HashMap<>();
    private Map<String, List<String>> fundStockMap = new HashMap<>();

    /**
     * @param fund
     * @return
     */
    public List<String> getStocks(String fund) {
        if (fundStockMap.containsKey(fund)) {
            return fundStockMap.get(fund);
        }
        return new ArrayList<String>();
    }

    /**
     * @param stock
     * @return
     */
    public List<String> getFunds(String stock) {
        if (stockFundMap.containsKey(stock)) {
            return stockFundMap.get(stock);
        }
        return new ArrayList<String>();
    }

    private void init() {
        List<File> fileList = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFilePath.getInputJjccTxtDir(CUR_SEASON), fileList);
        for (File f : fileList) {
            String fund = FileUtil.getFileName(f.getPath());
            List<String> stockList = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(f.getPath()))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        String[] strs = line.split(",");
                        String stock = strs[0];
                        stockList.add(stock);
                        if (!stockFundMap.containsKey(stock)) {
                            stockFundMap.put(stock, new ArrayList<String>());
                        }
                        stockFundMap.get(stock).add(fund);
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }
            fundStockMap.put(fund, stockList);
        }
    }

    public static CcjgLoader getInst() {
        if (inst == null) {
            inst = new CcjgLoader();
            inst.init();
        }
        return inst;
    }

    private static CcjgLoader inst = null;

    private CcjgLoader() {
    }
}
