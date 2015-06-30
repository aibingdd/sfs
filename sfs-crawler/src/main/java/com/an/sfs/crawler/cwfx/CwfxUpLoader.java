package com.an.sfs.crawler.cwfx;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CwfxUpLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CwfxUpLoader.class);
    private static final String START_DATE = "2012-01-01";

    public List<String> profitUpStockList = new ArrayList<>();
    private Set<String> profitUpStockSet = new HashSet<>();

    public List<String> ronaUpStockList = new ArrayList<>();
    private Set<String> ronaUpStockSet = new HashSet<>();

    public List<String> rotaUpStockList = new ArrayList<>();
    private Set<String> rotaUpStockSet = new HashSet<>();

    public List<String> getProfitUpStockList() {
        return profitUpStockList;
    }

    public boolean isProfitUp(String stock) {
        return profitUpStockSet.contains(stock);
    }

    public boolean isRonaUp(String stock) {
        return ronaUpStockSet.contains(stock);
    }

    public boolean isRotaUp(String stock) {
        return rotaUpStockSet.contains(stock);
    }

    private void init() {
        Map<String, List<CwfxVo>> cwfxMap = CwfxLoader.getInst().getCwfxMap();

        for (String stock : cwfxMap.keySet()) {
            List<CwfxVo> rawVoList = cwfxMap.get(stock);
            List<CwfxVo> voList = new ArrayList<>();
            for (int i = 0; i < rawVoList.size() - 1; i++) {
                CwfxVo vo = rawVoList.get(i);
                if (rawVoList.get(i).getDate().compareTo(START_DATE) > 0) {
                    voList.add(vo);
                }
            }

            boolean invalidProfitUp = false;
            boolean invalidRonaUp = false;
            boolean invalidRotaUp = false;
            for (CwfxVo vo : voList) {
                if (vo.getProfit() < 0 || vo.getProfitChangeRate() < 1f) {
                    invalidProfitUp = true;
                    break;
                }
            }
            for (CwfxVo vo : voList) {
                if (vo.getRona() < 0 || vo.getRonaChangeRate() < 1f) {
                    invalidRonaUp = true;
                    break;
                }
            }
            for (CwfxVo vo : voList) {
                if (vo.getRota() < 0 || vo.getRotaChangeRate() < 1f) {
                    invalidRotaUp = true;
                    break;
                }
            }
            if (!invalidProfitUp) {
                profitUpStockList.add(stock);
                profitUpStockSet.add(stock);
            }
            if (!invalidRonaUp) {
                ronaUpStockList.add(stock);
                ronaUpStockSet.add(stock);
            }
            if (!invalidRotaUp) {
                rotaUpStockList.add(stock);
                rotaUpStockSet.add(stock);
            }
        }
    }

    private CwfxUpLoader() {
    }

    private static CwfxUpLoader inst;

    public static CwfxUpLoader getInst() {
        if (inst == null) {
            inst = new CwfxUpLoader();
            inst.init();
        }
        return inst;
    }
}
