package com.an.sfs.crawler.gdyj;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.SfsConf;

public class GdrsDownLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GdrsDownLoader.class);

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
            if (list.size() < 4) {
                continue; // At least has 4 seasons' data
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
