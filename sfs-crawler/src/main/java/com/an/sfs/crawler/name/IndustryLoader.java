package com.an.sfs.crawler.name;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.FileUtil;
import com.an.sfs.crawler.tdx.StockLoader;
import com.an.sfs.crawler.tdx.StockVo;

public class IndustryLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndustryLoader.class);
    private List<IndustryVo> industryList = new ArrayList<>();
    private Map<String, String> codeNameMap = new HashMap<>();
    private Map<String, String> nameCodeMap = new HashMap<>();

    public List<IndustryVo> getIndustryList() {
        return industryList;
    }

    public String getIndustryName(String industryCode) {
        return codeNameMap.get(industryCode);
    }

    public String getIndustryCode(String industryName) {
        return nameCodeMap.get(industryName);
    }

    private String getIndustryFile() {
        return AppFilePath.getOutputDir() + File.separator + "Industry_Tdx.txt";
    }

    private void init() {
        if (!FileUtil.isFileExist(getIndustryFile())) {
            exportFile();
        }

        load();
    }

    private void load() {
        try (BufferedReader br = new BufferedReader(new FileReader(getIndustryFile()))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] strs = line.split(",");
                    String code = strs[0];
                    String name = strs[1];
                    IndustryVo vo = new IndustryVo(code, name);

                    industryList.add(vo);
                    codeNameMap.put(code, name);
                    nameCodeMap.put(name, code);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error ", e);
        }
    }

    private void exportFile() {
        Map<String, StockVo> map = StockLoader.getInst().getTstockMap();

        Set<String> industrySet = new HashSet<>();
        for (StockVo vo : map.values()) {
            industrySet.add(vo.getIndustry());
        }
        List<String> industryNameList = new ArrayList<>();
        industryNameList.addAll(industrySet);
        StringBuilder text = new StringBuilder();

        int i = 0;
        for (String industry : industryNameList) {
            String code = "9" + String.format("%05d", i++);
            text.append(code).append(",").append(industry).append("\n");
        }

        FileUtil.writeFile(getIndustryFile(), text.toString());
    }

    public static IndustryLoader getInst() {
        if (inst == null) {
            inst = new IndustryLoader();
            inst.init();
        }
        return inst;
    }

    private static IndustryLoader inst = null;

    private IndustryLoader() {
    }

    public static void main(String[] args) {
        IndustryLoader.getInst();
    }
}
