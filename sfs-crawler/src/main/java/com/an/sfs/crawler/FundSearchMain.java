package com.an.sfs.crawler;

import java.util.List;

import com.an.sfs.crawler.code.StockCodeNameLoader;
import com.an.sfs.crawler.jjcc.JjccLoader;
import com.an.sfs.crawler.jjmc.JjmcLoader;
import com.an.sfs.crawler.jjmc.JjmcVo;

public class FundSearchMain {
    private static final String STOCK = "600551";
    private static final String FUND = "161825";

    public static void main(String[] args) {
        List<String> funds = JjccLoader.getInst().getFunds(STOCK);
        System.out.println("Search fund:");
        for (String fund : funds) {
            JjmcVo jjmc = JjmcLoader.getInst().getJjmc(fund);
            System.out.println(jjmc.getDisplayStr());
        }

        System.out.println("Search stock:");
        List<String> stocks = JjccLoader.getInst().getStocks(FUND);
        for (String stock : stocks) {
            System.out.println(StockCodeNameLoader.getInst().getName(stock));
        }
    }
}
