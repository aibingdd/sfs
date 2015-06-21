package com.an.sfs.crawler.cwfx;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CwfxProfitUpLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CwfxProfitUpLoader.class);
    private static final String START_DATE = "2012-01-01";

    public List<String> stockList = new ArrayList<>();
    private Set<String> stockSet = new HashSet<>();

    public List<String> getStockList() {
        return stockList;
    }

    public boolean isProfitUp(String stock) {
        return stockSet.contains(stock);
    }

    private void init() {
        Map<String, List<CwfxVo>> cwfxMap = CwfxLoader.getInst().getCwfxMap();

        for (String stock : cwfxMap.keySet()) {

            List<CwfxVo> voList = cwfxMap.get(stock);

            boolean invalid = false;
            for (int i = 0; i < voList.size() - 1; i++) {
                CwfxVo vo = voList.get(i);
                if (vo.getDate().compareTo(START_DATE) > 0) {
                    if (vo.getProfit() < 0 || vo.getProfitChangeRate() < 1f) {
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

    private CwfxProfitUpLoader() {
    }

    private static CwfxProfitUpLoader inst;

    public static CwfxProfitUpLoader getInst() {
        if (inst == null) {
            inst = new CwfxProfitUpLoader();
            inst.init();
        }
        return inst;
    }
}
