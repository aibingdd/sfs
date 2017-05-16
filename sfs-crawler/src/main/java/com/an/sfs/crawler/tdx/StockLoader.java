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

import com.an.sfs.crawler.util.AppFile;
import com.an.sfs.crawler.util.FileUtil;

public class StockLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockLoader.class);
    private List<String> codeList = new ArrayList<>();
    private Map<String, StockVo> stockMap = new HashMap<>();

    public List<String> getCodeList() {
        return codeList;
    }

    public StockVo getStockVo(String stockCode) {
        StockVo tStockVo = stockMap.get(stockCode);
        return tStockVo;
    }

    public String getStockName(String stockCode) {
        StockVo tStockVo = stockMap.get(stockCode);
        if (tStockVo != null) {
            return tStockVo.getName();
        }
        return null;
    }

    public float getPrice(String stockCode) {
        StockVo tStockVo = stockMap.get(stockCode);
        if (tStockVo != null) {
            return tStockVo.getPrice();
        }
        return 0f;
    }

    public String getIndustryName(String stockCode) {
        StockVo tStockVo = stockMap.get(stockCode);
        if (tStockVo != null) {
            return tStockVo.getIndustry();
        }
        return null;
    }

    public String getPublicDate(String stockCode) {
        StockVo tStockVo = stockMap.get(stockCode);
        if (tStockVo != null) {
            return tStockVo.getPublicDate();
        }
        return null;
    }

    public StockVo getTStockVo(String stockCode) {
        return stockMap.get(stockCode);
    }

    public Map<String, StockVo> getTstockMap() {
        return stockMap;
    }

    public static String getTypeStr(String stockCode) {
        return stockCode.startsWith("6") ? "sh" : "sz";
    }

    public static String getCodeSuffix(String stockCode) {
        return stockCode.startsWith("6") ? "01" : "02";
    }

    private void init() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFile.getInputTdxDir(), files);
        Collections.sort(files);

        File latestFile = files.get(files.size() - 1);

        BufferedReader br = null;
        String line = null;
        try {
            FileInputStream fis = new FileInputStream(latestFile);
            br = new BufferedReader(new InputStreamReader(fis, "GB2312"));
            while ((line = br.readLine()) != null) {
                if (line.startsWith("代码")) {
                    String[] strs = line.split("\t");
                    if (!"代码".equals(strs[0]) || !"名称".equals(strs[1]) || !"现价".equals(strs[2])
                            || !"市盈(动)".equals(strs[3]) || !"细分行业".equals(strs[4]) || !"地区".equals(strs[5])
                            || !"流通股本(万)".equals(strs[6]) || !"上市日期".equals(strs[7]) || !"总股本(万)".equals(strs[8])) {
                        LOGGER.error("Error exported headers of file {}", latestFile);
                        System.exit(1);
                    }
                }
                if (line.startsWith("0") || line.startsWith("3") || line.startsWith("6")) {
                    String[] strs = line.split("\t");
                    String code = strs[0];// code
                    String name = strs[1];// name
                    String priceStr = strs[2].trim();// price
                    String peStr = strs[3].trim();
                    String industryStr = strs[4].trim();// industry
                    String regionStr = strs[5].trim();// region
                    String floatShareStr = strs[6].trim();// FloatShare
                    String publicDateStr = strs[7].trim();// Public date
                    String outstandingShareStr = strs[8].trim();// OutstandingShare

                    float price = Float.parseFloat(priceStr);
                    float pe = 0f;
                    if (!"--".equals(peStr)) {
                        pe = Float.parseFloat(peStr);
                    }
                    String industry = industryStr;
                    if (industry.isEmpty()) {
                        continue;
                    }
                    String region = regionStr;
                    long floatShare = 0;
                    if (!"--".equals(floatShareStr)) {
                        floatShare = FileUtil.parseFloat(floatShareStr);
                    }
                    String newPublicDate = "NULL";
                    if (!"--".equals(publicDateStr)) {
                        newPublicDate = publicDateStr.substring(0, 4) + "-" + publicDateStr.substring(4, 6) + "-"
                                + publicDateStr.substring(6);
                    }
                    long outstandingShare = FileUtil.parseFloat(outstandingShareStr);

                    StockVo vo = new StockVo();
                    vo.setCode(code);
                    vo.setName(name);
                    vo.setPrice(price);
                    vo.setPe(pe);
                    vo.setIndustry(industry);
                    vo.setRegion(region);
                    vo.setFloatShare(floatShare);
                    vo.setPublicDate(newPublicDate);
                    vo.setOutstandingShare(outstandingShare);
                    codeList.add(code);
                    stockMap.put(code, vo);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error {}", line, e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
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