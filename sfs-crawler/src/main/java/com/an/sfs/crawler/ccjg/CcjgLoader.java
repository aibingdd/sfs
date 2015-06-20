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
import com.an.sfs.crawler.AppUtil;
import com.an.sfs.crawler.FileUtil;

public class CcjgLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CcjgLoader.class);
    // "2015-03-31"
    private Map<String, List<String>> jgStockMap = new HashMap<>();
    private Map<String, List<CcjgVo>> stockJgMap = new HashMap<>();

    /**
     * @param jg
     * @return
     */
    public List<String> getStocks(String jg) {
        if (jgStockMap.containsKey(jg)) {
            return jgStockMap.get(jg);
        }
        return new ArrayList<String>();
    }

    public Map<String, List<CcjgVo>> getStockJgMap() {
        return stockJgMap;
    }

    /**
     * @param stock
     * @return
     */
    public List<CcjgVo> getJgs(String stock) {
        if (stockJgMap.containsKey(stock)) {
            return stockJgMap.get(stock);
        }
        return new ArrayList<CcjgVo>();
    }

    private void init() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFilePath.getInputCcjgTxtDir(AppUtil.CUR_SEASON), files);
        for (File f : files) {
            String stock = FileUtil.getFileName(f.getPath());
            List<CcjgVo> jgList = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(f.getPath()))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        String[] strs = line.split(",");
                        String jg = strs[2];
                        long count = Long.parseLong(strs[5]);
                        String date = strs[9];
                        CcjgVo vo = new CcjgVo(date, stock, jg, count);
                        jgList.add(vo);

                        if (!jgStockMap.containsKey(jg)) {
                            jgStockMap.put(jg, new ArrayList<String>());
                        }
                        jgStockMap.get(jg).add(stock);
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }
            stockJgMap.put(stock, jgList);
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
