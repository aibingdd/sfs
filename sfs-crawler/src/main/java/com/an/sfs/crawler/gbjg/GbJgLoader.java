package com.an.sfs.crawler.gbjg;

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

import com.an.sfs.crawler.SfsConf;
import com.an.sfs.crawler.util.AppFile;
import com.an.sfs.crawler.util.FileUtil;

public class GbJgLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GbJgLoader.class);
    private List<GbjgVo> gbjgList = new ArrayList<>();
    private Map<String, List<GbjgVo>> gbjgMap = new HashMap<>();
    // Current season's Gbjg
    private Map<String, GbjgVo> curSeasonGbjgMap = new HashMap<>();

    public List<GbjgVo> getGbjgList() {
        return gbjgList;
    }

    public Map<String, GbjgVo> getCurSeasonGbjgMap() {
        return curSeasonGbjgMap;
    }

    private void init() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFile.getOutputGbjgDir(), files);
        for (File f : files) {
            String stock = FileUtil.getFileName(f.getPath());
            List<GbjgVo> stockGbjgList = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        line = line.replaceAll(",", "");
                        String[] strs = line.split(";");
                        String date = strs[0];
                        String total = strs[1];
                        String circulation = strs[2];
                        String restriction = strs[3];
                        long totalL = 0L;
                        long circulationL = 0L;
                        long restrictionL = 0L;
                        if (!"--".equals(total)) {
                            totalL = (long) (Float.parseFloat(total) * 10000L);
                        }
                        if (!"--".equals(circulation)) {
                            circulationL = (long) (Float.parseFloat(circulation) * 10000L);
                        }
                        if (!"--".equals(restriction)) {
                            restrictionL = (long) (Float.parseFloat(restriction) * 10000L);
                        }
                        GbjgVo vo = new GbjgVo(stock, date, totalL, circulationL, restrictionL);

                        stockGbjgList.add(vo);
                        gbjgList.add(vo);

                        if (SfsConf.CURRENT_SEASON.equals(vo.getDate())) {
                            curSeasonGbjgMap.put(stock, vo);
                        }
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }
            gbjgMap.put(stock, stockGbjgList);
        }
    }

    private static GbJgLoader inst;

    private GbJgLoader() {
    }

    public static GbJgLoader getInst() {
        if (inst == null) {
            inst = new GbJgLoader();
            inst.init();
        }
        return inst;
    }
}
