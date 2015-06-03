package com.an.sfs.crawler.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppUtil;
import com.an.sfs.crawler.CwfxLoader;
import com.an.sfs.crawler.CwfxVo;

/**
 * Search all stocks which net profit is increasing for last three years.
 * 
 * @author Anthony
 *
 */
public class ProfitUpMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfitUpMain.class);
    private static final String START_DATE = "2012-01-01";
    private static final String OUTPUT_HTML_FILE = "Stock_Profit_Up.html";
    private static final String OUTPUT_TXT_FILE = "Stock_Profit_Up.txt";

    public static void main(String[] args) {
        List<String> ebkStockCodeList = new ArrayList<>();
        new ProfitUpMain().find(ebkStockCodeList);

        Collections.sort(ebkStockCodeList);

        AppUtil.exportHtml(ebkStockCodeList, OUTPUT_HTML_FILE);
        LOGGER.info("Save file {}", OUTPUT_HTML_FILE);

        AppUtil.exportTxt(ebkStockCodeList, OUTPUT_TXT_FILE);
        LOGGER.info("Save file {}", OUTPUT_TXT_FILE);
    }

    private void find(List<String> outStockCodeList) {
        Map<String, List<CwfxVo>> cwfxMap = CwfxLoader.getInst().getCwfxMap();
        List<String> codeList = new ArrayList<>();
        CwfxLoader.getInst().getStockCodeList(codeList);
        for (String code : codeList) {
            List<CwfxVo> list = cwfxMap.get(code);
            Collections.sort(list);

            boolean invalid = false;
            for (int i = 0; i < list.size() - 1; i++) {
                CwfxVo vo = list.get(i);
                if (vo.getDate().compareTo(START_DATE) > 0) {
                    if (vo.getProfit() < 0 || (vo.getProfitRate() - 1f) < 0) {
                        invalid = true;
                        break;
                    }
                }
            }

            if (!invalid) {
                outStockCodeList.add(code);
            }
        }
    }

}
