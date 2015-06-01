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
public class GdrsSeacherMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(GdrsSeacherMain.class);
    private static final String START_DATE = "2014-06-30";
    private static final String OUTPUT_FILE = "DecrementStock.html";

    public static void main(String[] args) {
        List<String> stockCodeList = new ArrayList<>();
        new GdrsSeacherMain().getDecrement(stockCodeList);
        Collections.sort(stockCodeList);
        AppUtil.exportHtml(stockCodeList, OUTPUT_FILE);
        LOGGER.info("Save file {}", OUTPUT_FILE);
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
