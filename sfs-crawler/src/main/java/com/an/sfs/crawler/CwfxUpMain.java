package com.an.sfs.crawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class CwfxUpMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(CwfxUpMain.class);
    private static final String START_DATE = "2012-01-01";
    private static final String TXT_FILE = "Stock_Cwfx_Up.txt";
    private static final String ZF_TXT_FILE = "Stock_Cwfx_Up_Zf.txt";
    private static final String TFP_TXT_FILE = "Stock_Cwfx_Up_Tfp.txt";
    private static final String ZF_HTML_FILE = "Stock_Cwfx_Up_Zf.html";
    private static final String TFP_HTML_FILE = "Stock_Cwfx_Up_Tfp.html";

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

        FileUtil.exportTxt(stockCodeList, TXT_FILE);
        FileUtil.exportTxt(zfmxCodeList, ZF_TXT_FILE);
        FileUtil.exportTxt(tfpCodeList, TFP_TXT_FILE);
        FileUtil.exportHtml(zfmxCodeList, zfList, ZF_HTML_FILE);
        FileUtil.exportHtml(tfpCodeList, tfpList, TFP_HTML_FILE);
    }

    private static void find(List<String> outStockCodeList) {
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
