package com.an.sfs.crawler.gsgk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GsgkLoader {
    private List<GsgkVo> gsgkList = new ArrayList<>();
    private Map<String, GsgkVo> gsgkMap = new HashMap<>();

    public List<GsgkVo> getGsgkList() {
        return gsgkList;
    }

    public GsgkVo getGsgk(String stock) {
        return gsgkMap.get(stock);
    }

    private void init() {
        String fp = GsgkFetcher.getGsgkFile();
        try (BufferedReader br = new BufferedReader(new FileReader(fp))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] strs = line.split(",");
                String code = strs[0];
                String createDate = strs[1];
                String publicDate = strs[2];
                GsgkVo vo = new GsgkVo(code, createDate, publicDate);
                gsgkList.add(vo);
                gsgkMap.put(code, vo);
            }
        } catch (IOException e) {
        }
    }

    private GsgkLoader() {
    }

    private static GsgkLoader inst;

    public static GsgkLoader getInst() {
        if (inst == null) {
            inst = new GsgkLoader();
            inst.init();
        }
        return inst;
    }
}
