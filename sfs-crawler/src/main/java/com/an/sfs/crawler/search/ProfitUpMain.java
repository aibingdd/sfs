package com.an.sfs.crawler.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppUtil;
import com.an.sfs.crawler.cwfx.CwfxLoader;
import com.an.sfs.crawler.cwfx.CwfxVo;
import com.an.sfs.crawler.fhrz.FhrzLoader;
import com.an.sfs.crawler.tfp.TfpLoader;

/**
 * Search all stocks which net profit is increasing for last three years.
 * 
 * @author Anthony
 *
 */
public class ProfitUpMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfitUpMain.class);
    private static final String START_DATE = "2012-01-01";
    private static final String RZ_HTML_FILE = "Stock_Profit_Up_Rz.html";
    private static final String TFP_HTML_FILE = "Stock_Profit_Up_Tfp.html";
    private static final String OUTPUT_TXT_FILE = "Stock_Profit_Up.txt";
    private static final String TFP_TXT_FILE = "Stock_Profit_Up_Tfp.txt";
    private static final String RZ_TXT_FILE = "Stock_Profit_Up_Rz.txt";

    public static void main(String[] args) {
        List<String> ebkStockCodeList = new ArrayList<>();
        new ProfitUpMain().find(ebkStockCodeList);

        Collections.sort(ebkStockCodeList);

        Map<String, String> rzMap = new HashMap<>();
        FhrzLoader.getInst().getRzMap(rzMap);
        AppUtil.exportRzHtml(ebkStockCodeList, rzMap, RZ_HTML_FILE);
        LOGGER.info("Save file {}", RZ_HTML_FILE);

        Map<String, String> tfpMap = new HashMap<>();
        TfpLoader.getInst().getTfpMap(tfpMap);
        AppUtil.exportRzHtml(ebkStockCodeList, tfpMap, TFP_HTML_FILE);
        LOGGER.info("Save file {}", TFP_HTML_FILE);

        AppUtil.exportTxt(ebkStockCodeList, OUTPUT_TXT_FILE);
        LOGGER.info("Save file {}", OUTPUT_TXT_FILE);

        List<String> rzCodeList = new ArrayList<>();
        for (String code : ebkStockCodeList) {
            if (rzMap.containsKey(code)) {
                rzCodeList.add(code);
            }
        }
        AppUtil.exportTxt(rzCodeList, RZ_TXT_FILE);
        LOGGER.info("Save file {}", RZ_TXT_FILE);

        List<String> tfpCodeList = new ArrayList<>();
        for (String code : ebkStockCodeList) {
            if (tfpMap.containsKey(code)) {
                tfpCodeList.add(code);
            }
        }
        AppUtil.exportTxt(tfpCodeList, TFP_TXT_FILE);
        LOGGER.info("Save file {}", TFP_TXT_FILE);
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
