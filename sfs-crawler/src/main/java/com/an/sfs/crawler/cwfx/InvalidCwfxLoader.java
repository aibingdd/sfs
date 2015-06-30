package com.an.sfs.crawler.cwfx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.AppUtil;
import com.an.sfs.crawler.FileUtil;
import com.an.sfs.crawler.tdx.StockLoader;

public class InvalidCwfxLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvalidCwfxLoader.class);

    private String getInvalidCwfxFile() {
        return AppFilePath.getOutputDir() + File.separator + "Invalid_Cwfx_Rona_Rota.txt";
    }

    private Set<String> stockSet = new HashSet<>();
    private List<String> stockList = new ArrayList<>();

    public List<String> getStockList() {
        return stockList;
    }

    public Set<String> getStockSet() {
        return stockSet;
    }

    private void init() {
        extract();
        load();
    }

    private void load() {
        try (BufferedReader br = new BufferedReader(new FileReader(getInvalidCwfxFile()))) {
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

    private void extract() {
        List<File> files = new ArrayList<File>();
        FileUtil.getFilesUnderDir(AppFilePath.getOutputCwfxDir(), files);

        Map<String, List<String>> industryTextMap = new HashMap<>();

        for (File f : files) {
            String stock = FileUtil.getFileName(f.toString());
            String industryName = StockLoader.getInst().getIndustryName(stock);
            String stockName = StockLoader.getInst().getStockName(stock);

            if (!industryTextMap.containsKey(industryName)) {
                industryTextMap.put(industryName, new ArrayList<String>());
            }

            try (BufferedReader br = new BufferedReader(new FileReader(f));) {
                String line = null;
                int i = 0;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        if (i++ < 3) {
                            line = line.replaceAll(",", "");
                            String[] strs = line.split(";");

                            boolean invalid = false;
                            if (!AppUtil.INVALID.equals(strs[5])) {
                                float rona = Float.parseFloat(strs[5]);
                                if (rona > 100f || rona < -20f) {
                                    invalid = true;
                                }
                            } else {
                                invalid = true;
                            }
                            if (!AppUtil.INVALID.equals(strs[6])) {
                                float rota = Float.parseFloat(strs[6]);
                                if (rota > 100f || rota < -20f) {
                                    invalid = true;
                                }
                            } else {
                                invalid = true;
                            }
                            if (invalid) {
                                String text = stock + "," + industryName + "," + stockName + "," + line + "\n";
                                industryTextMap.get(industryName).add(text);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error, code {}", stock, e);
                break;
            }
        }

        List<String> industryList = new ArrayList<>();
        industryList.addAll(industryTextMap.keySet());
        Collections.sort(industryList);

        StringBuilder text = new StringBuilder();
        for (String industry : industryList) {
            List<String> list = industryTextMap.get(industry);
            for (String line : list) {
                text.append(line);
            }
        }
        FileUtil.writeFile(getInvalidCwfxFile(), text.toString());
    }

    private InvalidCwfxLoader() {
    }

    private static InvalidCwfxLoader inst;

    public static InvalidCwfxLoader getInst() {
        if (inst == null) {
            inst = new InvalidCwfxLoader();
            inst.init();
        }
        return inst;
    }

    public static void main(String[] args) {
        for (String stock : InvalidCwfxLoader.getInst().getStockList()) {
            System.out.println(stock);
        }
    }
}
