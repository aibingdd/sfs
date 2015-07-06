package com.an.sfs.crawler.fhrz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.AppUtil;
import com.an.sfs.crawler.FileUtil;

public class FhfaLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FhfaLoader.class);
    private static final String FLAG_SONG = "送";
    private static final String FLAG_ZHUAN = "转";
    private static final String FLAG_PAI = "派";
    // Stock -> FhfaVo
    private Map<String, List<FhfaVo>> fhfaMap = new HashMap<>();

    public String getYearFhfa(String code) {
        List<FhfaVo> list = fhfaMap.get(code);
        if (!list.isEmpty()) {
            FhfaVo vo = list.get(0);
            String date = vo.getDate();
            if (date.compareTo(AppUtil.LAST_YEAR) > 0) {
                if (vo.getIncrease() > 0) {
                    return "送转" + vo.getIncrease();
                }
            }
        }
        return "";
    }

    /**
     * Query whether increase stock count since Last year
     * 
     * @param code
     * @return
     */
    public float getYearFactor(String code) {
        float factor = 1f;
        List<FhfaVo> list = fhfaMap.get(code);
        if (!list.isEmpty()) {
            FhfaVo vo = list.get(0);
            String date = vo.getDate();
            if (date.compareTo(AppUtil.LAST_YEAR) > 0) {
                if (vo.getIncrease() > 0) {
                    factor = 10 / (vo.getIncrease() + 10);
                }
            }
        }
        return factor;
    }

    /**
     * Query whether increase stock count since Last season
     * 
     * @param code
     * @return
     */
    public float getSeasonFactor(String code) {
        float factor = 1f;
        List<FhfaVo> list = fhfaMap.get(code);
        if (!list.isEmpty()) {
            FhfaVo vo = list.get(0);
            String date = vo.getDate();
            if (date.compareTo(AppUtil.CURRENT_SEASON) > 0) {
                if (vo.getIncrease() > 0) {
                    factor = 10 / (vo.getIncrease() + 10);
                }
            }
        }
        return factor;
    }

    public Map<String, List<FhfaVo>> getFhfaMap() {
        return fhfaMap;
    }

    private void init() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFilePath.getOutputFhfaDir(), files);
        for (File f : files) {
            String code = FileUtil.getFileName(f.getPath());
            if (code.equals("300017")) {
                System.out.println(code);
            }
            fhfaMap.put(code, new ArrayList<FhfaVo>());

            try (BufferedReader br = new BufferedReader(new FileReader(f.getPath()))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (line.contains("不分配")) {
                        continue;
                    }
                    String[] strs = line.split(";");

                    FhfaVo vo = new FhfaVo();
                    String date = strs[0];
                    vo.setDate(date);
                    String cont = strs[1];

                    int sIndex = cont.indexOf(FLAG_SONG);
                    int zIndex = cont.indexOf(FLAG_ZHUAN);
                    int pIndex = cont.indexOf(FLAG_PAI);
                    float szCount = 0f;
                    if (sIndex != -1) {
                        // 10送1.00转0.50派0.30元
                        // 10送6.00派1.70元
                        // 10送4.50
                        int dotIndex = cont.indexOf(".", sIndex);
                        String str = cont.substring(sIndex + FLAG_SONG.length(), dotIndex + 3);
                        szCount = szCount + Float.parseFloat(str);
                    }
                    if (zIndex != -1) {
                        // 10转5.00派8.00元(含税,扣税后7.20元)
                        // 10转7.78
                        int dotIndex = cont.indexOf(".", zIndex);
                        String str = cont.substring(zIndex + FLAG_ZHUAN.length(), dotIndex + 3);
                        szCount = szCount + Float.parseFloat(str);
                    }
                    vo.setIncrease(szCount);

                    if (pIndex != -1) {
                        int pEndIndex = cont.indexOf("元", pIndex);
                        String bonusStr = cont.substring(pIndex + FLAG_PAI.length(), pEndIndex);
                        float bonus = Float.parseFloat(bonusStr);
                        vo.setBonus(bonus);
                    }

                    fhfaMap.get(code).add(vo);
                }
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }

        }
    }

    private FhfaLoader() {
    }

    private static FhfaLoader inst;

    public static FhfaLoader getInst() {
        if (inst == null) {
            inst = new FhfaLoader();
            inst.init();
        }
        return inst;
    }
}