package com.an.sfs.crawler.fhrz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.FileUtil;

public class FhrzLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FhrzLoader.class);
    private Map<String, List<RzVo>> zfmxMap = new HashMap<>();

    /**
     * @param rzMap
     */
    public void getRzMap(Map<String, String> rzMap) {
        for (String code : zfmxMap.keySet()) {
            RzVo vo = getRzVo(code);
            if (vo != null) {
                rzMap.put(code, vo.getDisplayStr());
            }
        }
    }

    /**
     * @param code
     * @return
     */
    public RzVo getRzVo(String code) {
        List<RzVo> list = zfmxMap.get(code);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    private void init() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFilePath.getInputFhrzZfmxDir(), files);
        for (File f : files) {
            String stockCode = FileUtil.getFileName(f.getPath());
            List<RzVo> voList = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(f.getPath()))) {
                int cnt = 0;
                RzVo vo = null;
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("<td")) {
                        String val = getVal(line);
                        if (!val.equals("--")) {
                            if (cnt % 8 == 0) {
                                vo = new RzVo();
                                vo.setCode(stockCode);
                                vo.setIncreasetime(val);
                            } else if (cnt % 8 == 1) {
                                val = val.replaceAll(",", "");
                                float tmp = Float.parseFloat(val);
                                long num = (long) (tmp * 10000f);
                                vo.setNum(num);
                            } else if (cnt % 8 == 2) {
                                val = val.replaceAll(",", "");
                                float tmp = Float.parseFloat(val);
                                long amount = (long) (tmp * 10000f);
                                vo.setAmount(amount);
                            } else if (cnt % 8 == 3) {
                                float price = Float.parseFloat(val);
                                vo.setPrice(price);
                            } else if (cnt % 8 == 4) {
                                vo.setType(val);
                            } else if (cnt % 8 == 6) {
                                vo.setOnSaleTime(val);
                                voList.add(vo);
                            }
                        }
                        cnt++;
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error, code {}", stockCode, e);
                break;
            }

            zfmxMap.put(stockCode, voList);
        }
    }

    private String getVal(String line) {
        int beginIndex = line.indexOf(">");
        int endIndex = line.indexOf("</td>");
        String val = line.substring(beginIndex + 1, endIndex);
        return val;
    }

    private FhrzLoader() {
    }

    private static FhrzLoader inst;

    public static FhrzLoader getInst() {
        if (inst == null) {
            inst = new FhrzLoader();
            inst.init();
        }
        return inst;
    }
}
