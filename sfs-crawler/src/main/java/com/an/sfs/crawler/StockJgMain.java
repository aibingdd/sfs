package com.an.sfs.crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.ccjg.CcjgLoader;
import com.an.sfs.crawler.ccjg.CcjgVo;
import com.an.sfs.crawler.gbjg.GbJgLoader;
import com.an.sfs.crawler.gbjg.GbjgVo;
import com.an.sfs.crawler.gdyj.GdyjVo;
import com.an.sfs.crawler.tdx.StockLoader;
import com.an.sfs.crawler.util.AppFile;
import com.an.sfs.crawler.util.FileUtil;

public class StockJgMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppFile.class);

    public static void main(String[] args) {
        System.out.println("Stock Ji Gou Search.");
        AppFile.initDirs();
        run();
    }

    private static void run() {
        init();
    }

    private static void init() {
        Map<String, Long> stockJgTotalMap = new HashMap<>();

        Map<String, List<CcjgVo>> stockJgMap = CcjgLoader.getInst().getStockJgMap();
        Map<String, GbjgVo> gbjgMap = GbJgLoader.getInst().getCurSeasonGbjgMap();

        for (String stock : stockJgMap.keySet()) {
            List<CcjgVo> jgList = stockJgMap.get(stock);
            long totalL = 0L;
            for (CcjgVo vo : jgList) {
                totalL += vo.getCount();
            }
            stockJgTotalMap.put(stock, totalL);
        }

        List<GdyjVo> gdyjList = new ArrayList<>();

        List<String> stockCodeList = StockLoader.getInst().getCodeList();
        for (String stock : stockCodeList) {
            long totalL = 0L;
            if (stockJgTotalMap.containsKey(stock)) {
                totalL = stockJgTotalMap.get(stock);
            }

            long circulation = 0L;
            GbjgVo gbjgVo = gbjgMap.get(stock);
            if (gbjgVo != null) {
                circulation = gbjgVo.getCirculation();
            }

            gdyjList.add(new GdyjVo(stock, totalL, circulation));
        }

        List<String> stockList = new ArrayList<>();
        Map<String, String> jgTotalMap = new HashMap<>();
        Map<String, String> circulationMap = new HashMap<>();

        for (GdyjVo vo : gdyjList) {
            String stock = vo.getCode();

            stockList.add(stock);
            jgTotalMap.put(stock, vo.getJgTotal() / 10000 + "");
            circulationMap.put(stock, vo.getCirculation() / 10000 + "");
        }

        List<Map<String, String>> appendInfoList = new ArrayList<>();
        appendInfoList.add(jgTotalMap);
        appendInfoList.add(circulationMap);

        FileUtil.exportStock(stockList, AppFile.getOutputFp("StockJg.txt"));
        FileUtil.exportHtml(stockList, appendInfoList, AppFile.getOutputFp("StockJg.html"));
    }
}
