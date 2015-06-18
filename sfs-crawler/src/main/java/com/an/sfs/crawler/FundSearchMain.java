package com.an.sfs.crawler;

import java.util.List;

import com.an.sfs.crawler.jjcc.JjccLoader;
import com.an.sfs.crawler.name.FundLoader;
import com.an.sfs.crawler.name.FundVo;
import com.an.sfs.crawler.name.StockLoader;

public class FundSearchMain {
    private static final String STOCK = "600551";
    private static final String FUND = "161825";

    public static void main(String[] args) {
        List<String> funds = JjccLoader.getInst().getFunds(STOCK);
        System.out.println("Search fund:");
        for (String fund : funds) {
            FundVo vo = FundLoader.getInst().getFund(fund);
            System.out.println(vo.getDisplayStr());
        }

        System.out.println("Search stock:");
        List<String> stocks = JjccLoader.getInst().getStocks(FUND);
        for (String stock : stocks) {
            String vo = StockLoader.getInst().getName(stock);
            System.out.println(vo);
        }
    }
}
