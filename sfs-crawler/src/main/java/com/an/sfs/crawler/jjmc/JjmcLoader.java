package com.an.sfs.crawler.jjmc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JjmcLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(JjmcLoader.class);
    private Map<String, JjmcVo> jjMap = new HashMap<>();
    private List<JjmcVo> jjList = new ArrayList<>();

    public List<JjmcVo> getJjs() {
        return jjList;
    }

    public JjmcVo getJjmc(String code) {
        return jjMap.get(code);
    }

    private void init() {
        try (BufferedReader br = new BufferedReader(new FileReader(new JjmcFetcher().getJjmcFile()))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] strs = line.split(",");
                    JjmcVo vo = new JjmcVo(Integer.parseInt(strs[0]), strs[1], strs[2]);
                    jjList.add(vo);
                    jjMap.put(vo.getCode(), vo);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error ", e);
        }
    }

    public static JjmcLoader getInst() {
        if (inst == null) {
            inst = new JjmcLoader();
            inst.init();
        }
        return inst;
    }

    private static JjmcLoader inst = null;

    private JjmcLoader() {
    }
}
