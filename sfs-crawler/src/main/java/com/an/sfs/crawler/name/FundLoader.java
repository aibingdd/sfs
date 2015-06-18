package com.an.sfs.crawler.name;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.jjmc.JjmcFetcher;

public class FundLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FundLoader.class);
    private Map<String, FundVo> fundMap = new HashMap<>();
    private List<FundVo> fundList = new ArrayList<>();

    public List<FundVo> getFunds() {
        return fundList;
    }

    public FundVo getFund(String code) {
        return fundMap.get(code);
    }

    private void init() {
        try (BufferedReader br = new BufferedReader(new FileReader(JjmcFetcher.getJjmcFile()))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] strs = line.split(",");
                    FundVo vo = new FundVo(Integer.parseInt(strs[0]), strs[1], strs[2]);
                    fundList.add(vo);
                    fundMap.put(vo.getCode(), vo);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error ", e);
        }
    }

    public static FundLoader getInst() {
        if (inst == null) {
            inst = new FundLoader();
            inst.init();
        }
        return inst;
    }

    private static FundLoader inst = null;

    private FundLoader() {
    }
}
