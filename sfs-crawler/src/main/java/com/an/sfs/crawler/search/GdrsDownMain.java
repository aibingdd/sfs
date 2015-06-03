package com.an.sfs.crawler.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppUtil;
import com.an.sfs.crawler.GdrsLoader;
import com.an.sfs.crawler.GdrsVo;

/**
 * Search all stocks which holder's count is decreasing from START_DATE.
 * 
 * @author Anthony
 *
 */
public class GdrsDownMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(GdrsDownMain.class);
    private static final String START_DATE = "2014-06-30";
    private static final String OUTPUT_HTML_FILE = "Stock_Gdrs_Down.html";
    private static final String OUTPUT_TXT_FILE = "Stock_Gdrs_Down.txt";

    public static void main(String[] args) {
        List<String> ebkStockCodeList = new ArrayList<>();
        new GdrsDownMain().find(ebkStockCodeList);

        Collections.sort(ebkStockCodeList);

        AppUtil.exportHtml(ebkStockCodeList, OUTPUT_HTML_FILE);
        LOGGER.info("Save file {}", OUTPUT_HTML_FILE);

        AppUtil.exportTxt(ebkStockCodeList, OUTPUT_TXT_FILE);
        LOGGER.info("Save file {}", OUTPUT_TXT_FILE);
    }

    private void find(List<String> outStockCodeList) {
        Map<String, List<GdrsVo>> gdrsMap = GdrsLoader.getInst().getGdrsMap();
        List<String> codeList = new ArrayList<>();
        GdrsLoader.getInst().getStockCodeList(codeList);
        for (String code : codeList) {
            List<GdrsVo> list = gdrsMap.get(code);
            if (list.size() < 4) {
                // At least has 4 seasons' data
                continue;
            }

            Collections.sort(list);

            boolean invalid = false;
            for (GdrsVo vo : list) {
                if (vo.getDate().compareTo(START_DATE) >= 0) {
                    if (vo.getCountChangeRate() > 1) {
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
