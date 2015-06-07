package com.an.sfs.crawler.tfp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;

public class TfpLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(TfpLoader.class);

    // code -> List<TfpggVo>
    private Map<String, List<TfpVo>> tfpMap = new HashMap<String, List<TfpVo>>();

    public void getStockCodeList(List<String> codeList) {
        codeList.addAll(tfpMap.keySet());
        Collections.sort(codeList);
    }

    /**
     * @param allCodeList
     * @param outCodeList
     */
    public void getTfpCodes(List<String> allCodeList, List<String> outCodeList) {
        List<TfpVo> tpVoList = new ArrayList<>();
        List<TfpVo> fpVoList = new ArrayList<>();
        for (String code : allCodeList) {
            if (tfpMap.containsKey(code)) {
                List<TfpVo> list = tfpMap.get(code);
                if (!list.isEmpty()) {
                    TfpVo vo = list.get(0);
                    if (vo.isFp()) {
                        fpVoList.add(vo);
                    } else {
                        tpVoList.add(vo);
                    }
                }
            }
        }

        Collections.sort(fpVoList);
        Collections.sort(tpVoList);
        for (TfpVo vo : fpVoList) {
            outCodeList.add(vo.getCode());
        }
        for (TfpVo vo : tpVoList) {
            outCodeList.add(vo.getCode());
        }
    }

    /**
     * @param outTfpMap
     */
    public void getTfpMap(Map<String, String> outTfpMap) {
        for (String code : tfpMap.keySet()) {
            List<TfpVo> voList = tfpMap.get(code);
            Collections.sort(voList);

            String val = "";
            for (TfpVo vo : voList) {
                val += (vo.getDisplayStr() + "; ");
            }
            outTfpMap.put(code, val);
        }
    }

    private void init() {
        try (BufferedReader br = new BufferedReader(new FileReader(AppFilePath.getOutputDir() + File.separator
                + TfpFetcher.TFP_FILE))) {
            // 600054;黄山旅游;2015-06-05 09:30;2015-06-05 09:30;今起复牌;重大事项
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    if (!line.startsWith("6") && !line.startsWith("0") && !line.startsWith("3")) {
                        continue;
                    }
                    String[] strs = line.split(";");
                    if (strs.length != 6) {
                        continue;
                    }

                    String code = strs[0];
                    String name = strs[1];
                    String stopTime = strs[2];
                    String resumeTime = strs[3];
                    String period = strs[4];
                    String reason = strs[5];

                    TfpVo vo = new TfpVo();
                    vo.setCode(code);
                    vo.setName(name);
                    vo.setStopTime(stopTime);
                    vo.setResumeTime(resumeTime);
                    vo.setPeriod(period);
                    vo.setReason(reason);

                    if (!tfpMap.containsKey(code)) {
                        tfpMap.put(code, new ArrayList<TfpVo>());
                    }
                    tfpMap.get(code).add(vo);
                }
            }

        } catch (IOException e) {
            LOGGER.error("Error ", e);
        }
    }

    private TfpLoader() {
    }

    private static TfpLoader inst;

    public static TfpLoader getInst() {
        if (inst == null) {
            inst = new TfpLoader();
            inst.init();
        }
        return inst;
    }
}
