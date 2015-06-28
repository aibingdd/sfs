package com.an.sfs.crawler.tdx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.FileUtil;

public class StockLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockLoader.class);
    private List<String> stockCodeList = new ArrayList<>();
    private Map<String, StockVo> tstockMap = new HashMap<>();

    public List<String> getStockCodeList() {
        return stockCodeList;
    }

    public String getRegion(String stockCode) {
        StockVo tStockVo = tstockMap.get(stockCode);
        if (tStockVo != null) {
            return tStockVo.getRegion();
        }
        return null;
    }

    public String getStockName(String stockCode) {
        StockVo tStockVo = tstockMap.get(stockCode);
        if (tStockVo != null) {
            return tStockVo.getName();
        }
        return null;
    }

    public float getPrice(String stockCode) {
        StockVo tStockVo = tstockMap.get(stockCode);
        if (tStockVo != null) {
            return tStockVo.getPrice();
        }
        return 0f;
    }

    public String getIndustryName(String stockCode) {
        StockVo tStockVo = tstockMap.get(stockCode);
        if (tStockVo != null) {
            return tStockVo.getIndustry();
        }
        return null;
    }

    public String getPublicDate(String stockCode) {
        StockVo tStockVo = tstockMap.get(stockCode);
        if (tStockVo != null) {
            return tStockVo.getPublicDate();
        }
        return null;
    }

    public StockVo getTStockVo(String stockCode) {
        return tstockMap.get(stockCode);
    }

    public Map<String, StockVo> getTstockMap() {
        return tstockMap;
    }

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

    private void init() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFilePath.getInputTdxDir(), files);
        Collections.sort(files);

        File latestFile = files.get(files.size() - 1);

        BufferedReader br = null;
        String line = null;
        try {
            FileInputStream fis = new FileInputStream(latestFile);
            br = new BufferedReader(new InputStreamReader(fis, "GB2312"));
            while ((line = br.readLine()) != null) {
                if (line.startsWith("0") || line.startsWith("3") || line.startsWith("6")) {
                    String[] strs = line.split("\t");
                    String code = strs[0];// code
                    String name = strs[1];// name
                    float price = Float.parseFloat(strs[2]);// price
                    String industry = strs[3];// industry
                    if (industry.isEmpty()) {
                        continue;
                    }
                    String region = strs[4];// region
                    long floatShare = FileUtil.parseFloat(strs[5]);// FloatShare
                    String publicDate = strs[6];// Public date
                    String newPublicDate = publicDate.substring(0, 4) + "-" + publicDate.substring(4, 6) + "-"
                            + publicDate.substring(6);
                    long outstandingShare = FileUtil.parseFloat(strs[7]);// OutstandingShare
                    long bShare = FileUtil.parseFloat(strs[8]);// B
                    long hShare = FileUtil.parseFloat(strs[9]);// H

                    StockVo vo = new StockVo();
                    vo.setCode(code);
                    vo.setName(name);
                    vo.setPrice(price);
                    vo.setIndustry(industry);
                    vo.setRegion(region);
                    vo.setFloatShare(floatShare);
                    vo.setPublicDate(newPublicDate);
                    vo.setOutstandingShare(outstandingShare);
                    vo.setbShare(bShare);
                    vo.sethShare(hShare);

                    stockCodeList.add(code);
                    tstockMap.put(code, vo);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error {}", line, e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
    }

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
