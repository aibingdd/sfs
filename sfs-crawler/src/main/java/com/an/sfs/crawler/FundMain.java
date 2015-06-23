package com.an.sfs.crawler;

import java.util.List;

import com.an.sfs.crawler.jjcc.JjccLoader;
import com.an.sfs.crawler.name.FundLoader;
import com.an.sfs.crawler.name.FundVo;
import com.an.sfs.crawler.tdx.StockLoader;

public class FundMain {
    private static final String STOCK = "600551";
    private static final String FUND = "161825";

    public static void main(String[] args) {
        AppFilePath.initDirs();
        List<String> funds = JjccLoader.getInst().getFunds(STOCK);
        System.out.println("Search fund:");
        for (String fund : funds) {
            FundVo vo = FundLoader.getInst().getFund(fund);
            System.out.println(vo.getDisplayStr());
        }

        System.out.println("Search stock:");
        List<String> stocks = JjccLoader.getInst().getStocks(FUND);
        for (String stock : stocks) {
            String name = StockLoader.getInst().getStockName(stock);
            System.out.println(name);
        }
    }
}
