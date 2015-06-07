package com.an.sfs.crawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.fhrz.FhrzLoader;
import com.an.sfs.crawler.gdrs.GdrsLoader;
import com.an.sfs.crawler.gdrs.GdrsVo;
import com.an.sfs.crawler.tfp.TfpLoader;

/**
 * Search all stocks which holder's count is decreasing from START_DATE.
 * 
 * @author Anthony
 *
 */
public class GdrsDownMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(GdrsDownMain.class);
    private static final String START_DATE = "2014-06-30";
    private static final String ZF_HTML_FILE = "Stock_Gdrs_Down_Zf.html";
    private static final String TFP_HTML_FILE = "Stock_Gdrs_Down_Tfp.html";

    private static final String TXT_FILE = "Stock_Gdrs_Down.txt";
    private static final String ZF_TXT_FILE = "Stock_Gdrs_Down_Zf.txt";
    private static final String TFP_TXT_FILE = "Stock_Gdrs_Down_Tfp.txt";

    public static void main(String[] args) {
        List<String> stockCodeList = new ArrayList<>();
        find(stockCodeList);
        Collections.sort(stockCodeList);

        List<String> zfmxCodeList = new ArrayList<>();
        FhrzLoader.getInst().getZfmxCodes(stockCodeList, zfmxCodeList);

        List<String> tfpCodeList = new ArrayList<>();
        TfpLoader.getInst().getTfpCodes(stockCodeList, tfpCodeList);

        Map<String, String> zfMap = new HashMap<>();
        FhrzLoader.getInst().getZfmxMap(zfMap);
        List<Map<String, String>> zfList = new ArrayList<Map<String, String>>();
        zfList.add(zfMap);

        Map<String, String> tfpMap = new HashMap<>();
        TfpLoader.getInst().getTfpMap(tfpMap);
        List<Map<String, String>> tfpList = new ArrayList<Map<String, String>>();
        tfpList.add(tfpMap);

        AppUtil.exportTxt(stockCodeList, TXT_FILE);
        AppUtil.exportTxt(zfmxCodeList, ZF_TXT_FILE);
        AppUtil.exportTxt(tfpCodeList, TFP_TXT_FILE);
        AppUtil.exportHtml(zfmxCodeList, zfList, ZF_HTML_FILE);
        AppUtil.exportHtml(tfpCodeList, tfpList, TFP_HTML_FILE);
    }

    private static void find(List<String> outStockCodeList) {
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
