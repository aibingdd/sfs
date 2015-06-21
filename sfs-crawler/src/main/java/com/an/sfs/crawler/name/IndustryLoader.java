package com.an.sfs.crawler.name;

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
import com.an.sfs.crawler.FileUtil;
import com.an.sfs.crawler.gsgk.StockIndustryLoader;

public class IndustryLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndustryLoader.class);
    private List<IndustryVo> industryList = new ArrayList<>();
    // Industry Code -> Industry Name
    private Map<String, String> industryCodeNameMap = new HashMap<String, String>();
    // Industry Name -> Industry Code
    private Map<String, String> industryNameCodeMap = new HashMap<String, String>();

    private static String getIndustryFile() {
        return AppFilePath.getOutputDir() + File.separator + "Industry.txt";
    }

    public List<IndustryVo> getIndustryList() {
        return industryList;
    }

    public String getIndustryCode(String industryName) {
        return industryNameCodeMap.get(industryName);
    }

    public String getIndustryName(String industryCode) {
        return industryCodeNameMap.get(industryCode);
    }

    private void init() {
        StockIndustryLoader.exportStockIndustryFile();

        exportIndustryFile();

        loadFromFile();
    }

    private void loadFromFile() {
        String industryFile = getIndustryFile();
        try (BufferedReader br = new BufferedReader(new FileReader(industryFile))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] strs = line.split(",");
                    String code = strs[0];
                    String industry = strs[1];

                    IndustryVo vo = new IndustryVo(code, industry);
                    industryList.add(vo);
                    industryCodeNameMap.put(code, industry);
                    industryNameCodeMap.put(industry, code);
                }
            }
        } catch (IOException e) {
        }
    }

    private static void exportIndustryFile() {
        String industryFile = getIndustryFile();
        if (!FileUtil.isFileExist(industryFile)) {
            Set<String> industryNameSet = new HashSet<>();
            String stockIndustryFile = StockIndustryLoader.getStockIndustryFile();
            try (BufferedReader br = new BufferedReader(new FileReader(stockIndustryFile))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        String[] strs = line.split(",");
                        String industry = strs[1];
                        if (!industryNameSet.contains(industry)) {
                            industryNameSet.add(industry);
                        }
                    }
                }
            } catch (IOException e) {
            }
            List<String> industryNameList = new ArrayList<>();
            industryNameList.addAll(industryNameSet);
            Collections.sort(industryNameList);

            StringBuilder text = new StringBuilder();
            int i = 0;
            for (String industry : industryNameList) {
                String code = "9" + String.format("%05d", i++);
                text.append(code).append(",").append(industry).append("\n");
            }

            LOGGER.info("Save file {}", industryFile);
            FileUtil.writeFile(industryFile, text.toString());
        }
    }

    private IndustryLoader() {
    }

    private static IndustryLoader inst;

    public static IndustryLoader getInst() {
        if (inst == null) {
            inst = new IndustryLoader();
            inst.init();
        }
        return inst;
    }

    public static void main(String[] args) {
        IndustryLoader.getInst();
    }
}
