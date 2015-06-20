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
import com.an.sfs.crawler.name.StockLoader;
import com.an.sfs.crawler.name.StockVo;

public class StockJgSearchMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppFilePath.class);

    public static void main(String[] args) {
        System.out.println("Stock Ji Gou Search.");
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

        for (StockVo stockVo : StockLoader.getInst().getStocks()) {
            String stock = stockVo.getCode();

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

        String fileName = "StockJg.txt";
        LOGGER.info("Write file {}", fileName);
        FileUtil.exportTxt(stockList, fileName);

        fileName = "StockJg.html";
        LOGGER.info("Write file {}", fileName);
        FileUtil.exportHtml(stockList, appendInfoList, fileName);
    }
}
