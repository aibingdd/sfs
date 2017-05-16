package com.an.sfs.crawler.gdyj;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.an.sfs.crawler.SfsConf;

public class GdrsDownLoader {
    public List<String> stockList = new ArrayList<>();
    private Set<String> stockSet = new HashSet<>();

    public List<String> getStockList() {
        return stockList;
    }

    public boolean isGdrsDown(String stock) {
        return stockSet.contains(stock);
    }

    private void init() {
        Map<String, List<GdrsVo>> gdrsMap = GdrsLoader.getInst().getGdrsMap();
        for (String stock : gdrsMap.keySet()) {
            List<GdrsVo> list = gdrsMap.get(stock);
            if (list.size() < SfsConf.MIN_SEASON_COUNT) {
                continue;
            }

            boolean invalid = false;
            for (GdrsVo vo : list) {
                if (vo.getDate().compareTo(SfsConf.GDRS_START_SEASON) >= 0) {
                    if (vo.getShareholderCountChangeRate() > 1) {
                        invalid = true;
                        break;
                    }
                }
            }

            if (!invalid) {
                stockList.add(stock);
                stockSet.add(stock);
            }
        }
    }

    private GdrsDownLoader() {
    }

    private static GdrsDownLoader inst;

    public static GdrsDownLoader getInst() {
        if (inst == null) {
            inst = new GdrsDownLoader();
            inst.init();
        }
        return inst;
    }
}
