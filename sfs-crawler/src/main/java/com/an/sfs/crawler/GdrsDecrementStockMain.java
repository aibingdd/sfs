package com.an.sfs.crawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Search all stocks which holder's count is decreasing from START_DATE
 * 
 * @author Anthony
 *
 */
public class GdrsDecrementStockMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(GdrsDecrementStockMain.class);
    private static final String START_DATE = "2014-06-30";
    private static final String OUTPUT_HTML_FILE = "Stock_Gdrs_Decrement.html";
    private static final String OUTPUT_TXT_FILE = "Stock_Gdrs_Decrement.txt";

    public static void main(String[] args) {
        List<String> ebkStockCodeList = new ArrayList<>();
        new GdrsDecrementStockMain().getDecrement(ebkStockCodeList);
        Collections.sort(ebkStockCodeList);
        AppUtil.exportHtml(ebkStockCodeList, OUTPUT_HTML_FILE);
        LOGGER.info("Save file {}", OUTPUT_HTML_FILE);
        AppUtil.exportTxt(ebkStockCodeList, OUTPUT_TXT_FILE);
        LOGGER.info("Save file {}", OUTPUT_TXT_FILE);
    }

    /*
     * 
     * Increment before 2014-06-30
     */
    public void getDecrement(List<String> outList) {
        Map<String, List<GdrsVo>> gdrsMap = GdrsLoader.getInst().getGdrsMap();
        List<String> codeList = new ArrayList<>();
        GdrsLoader.getInst().getCodeList(codeList);
        for (String code : codeList) {
            List<GdrsVo> list = gdrsMap.get(code);
            Collections.sort(list);
            for (GdrsVo vo : list) {
                if (vo.getCountChangeRate() > 0) {
                    if (vo.getDate().compareTo(START_DATE) < 0) {
                        outList.add(code);
                    }
                    break;
                }
            }
        }
    }
}
