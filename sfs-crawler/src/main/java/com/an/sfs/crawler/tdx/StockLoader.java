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

    public StockVo getStockVo(String stockCode) {
        StockVo tStockVo = tstockMap.get(stockCode);
        return tStockVo;
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
                if (line.startsWith("代码")) {
                    String[] strs = line.split("\t");
                    if (!"代码".equals(strs[0]) || !"名称".equals(strs[1]) || !"现价".equals(strs[2])
                            || !"总量".equals(strs[3]) || !"市盈(动)".equals(strs[4]) || !"细分行业".equals(strs[5])
                            || !"地区".equals(strs[6]) || !"流通股本(万)".equals(strs[7]) || !"上市日期".equals(strs[8])
                            || !"总股本(万)".equals(strs[9])) {
                        LOGGER.error("Error exported headers of file {}", latestFile);
                        System.exit(1);
                    }
                }
                if (line.startsWith("0") || line.startsWith("3") || line.startsWith("6")) {
                    String[] strs = line.split("\t");
                    String code = strs[0];// code
                    String name = strs[1];// name
                    float price = Float.parseFloat(strs[2]);// price
                    long totalVolume = Long.parseLong(strs[3]);
                    float pe = 0f;
                    if (!"--".equals(strs[4].trim())) {
                        pe = Float.parseFloat(strs[4].trim());
                    }
                    String industry = strs[5];// industry
                    if (industry.isEmpty()) {
                        continue;
                    }
                    String region = strs[6];// region
                    long floatShare = FileUtil.parseFloat(strs[7]);// FloatShare
                    String publicDate = strs[8];// Public date
                    String newPublicDate = publicDate.substring(0, 4) + "-" + publicDate.substring(4, 6) + "-"
                            + publicDate.substring(6);
                    long outstandingShare = FileUtil.parseFloat(strs[9]);// OutstandingShare

                    StockVo vo = new StockVo();
                    vo.setCode(code);
                    vo.setName(name);
                    vo.setPrice(price);
                    vo.setTotalVolume(totalVolume);
                    vo.setPe(pe);
                    vo.setIndustry(industry);
                    vo.setRegion(region);
                    vo.setFloatShare(floatShare);
                    vo.setPublicDate(newPublicDate);
                    vo.setOutstandingShare(outstandingShare);

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