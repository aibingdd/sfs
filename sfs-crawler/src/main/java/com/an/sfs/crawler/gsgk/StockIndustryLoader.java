package com.an.sfs.crawler.gsgk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.FileUtil;
import com.an.sfs.crawler.name.IndustryLoader;
import com.an.sfs.crawler.name.IndustryVo;

public class StockIndustryLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockIndustryLoader.class);
    private Map<String, IndustryVo> stockIndustryMap = new HashMap<>();
    private IndustryLoader industryLoaderInst = IndustryLoader.getInst();

    public static String getStockIndustryFile() {
        return AppFilePath.getOutputDir() + File.separator + "Stock_Industry.txt";
    }

    public IndustryVo getIndustry(String stock) {
        return stockIndustryMap.get(stock);
    }

    private void init() {

        exportStockIndustryFile();

        loadFromFile();
    }

    private void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(getStockIndustryFile()))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] strs = line.split(",");
                    String stock = strs[0];
                    String industryName = strs[1];
                    String industryCode = industryLoaderInst.getIndustryCode(industryName);
                    IndustryVo vo = new IndustryVo(industryCode, industryName);
                    stockIndustryMap.put(stock, vo);
                }
            }
        } catch (IOException e) {
        }
    }

    public static void exportStockIndustryFile() {
        String stockIndustryFile = getStockIndustryFile();
        if (!FileUtil.isFileExist(stockIndustryFile)) {
            List<GsgkVo> voList = GsgkLoader.getInst().getGsgkList();
            StringBuilder text = new StringBuilder();
            for (GsgkVo vo : voList) {
                text.append(vo.getCode()).append(",").append(vo.getIndustry()).append("\n");
            }

            LOGGER.info("Save file {}", stockIndustryFile);
            FileUtil.writeFile(stockIndustryFile, text.toString());
        }
    }

    private StockIndustryLoader() {
    }

    private static StockIndustryLoader inst;

    public static StockIndustryLoader getInst() {
        if (inst == null) {
            inst = new StockIndustryLoader();
            inst.init();
        }
        return inst;
    }

    public static void main(String[] args) {
        StockIndustryLoader.getInst();
    }
}
