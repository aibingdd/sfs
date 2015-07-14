package com.an.sfs.crawler.yjyg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.SfsConf;

// 000963,华东医药,预计2015年1-6月归属于上市公司股东的净利润盈利:60335万元-66174万元,55%～70%,预增,389259850.75,预增,2015-03-14,2015-06-30
public class YjygLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(YjygLoader.class);
    private List<YjygVo> yjygList = new ArrayList<>();
    private Map<String, YjygVo> yjygMap = new HashMap<>();

    private List<YjygVo> zengList = new ArrayList<>();
    private List<YjygVo> yingList = new ArrayList<>();
    private List<YjygVo> jianList = new ArrayList<>();
    private List<YjygVo> pingList = new ArrayList<>();
    private List<YjygVo> jingList = new ArrayList<>();
    private List<YjygVo> kuiList = new ArrayList<>();

    public List<YjygVo> getYjygList() {
        return yjygList;
    }

    public List<YjygVo> getZengList() {
        return zengList;
    }

    public List<YjygVo> getPingList() {
        return pingList;
    }

    public List<YjygVo> getYingList() {
        return yingList;
    }

    public List<YjygVo> getJianList() {
        return jianList;
    }

    public List<YjygVo> getJingList() {
        return jingList;
    }

    public List<YjygVo> getKuiList() {
        return kuiList;
    }

    /**
     * @param code
     * @return
     */
    public YjygVo getYjyg(String code) {
        return yjygMap.get(code);
    }

    private void init() {
        String yjygTxtFile = YjygFetcher.getYjygTxtFile(SfsConf.YJYG_SEASON);
        try (BufferedReader br = new BufferedReader(new FileReader(yjygTxtFile))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] strs = line.split(",");
                    if (strs.length != 9) {
                        LOGGER.error("Invalid line: {}", line);
                        continue;
                    }
                    String code = strs[0];
                    String name = strs[1];
                    String note = strs[2];
                    String range = strs[3];
                    String typeStr = strs[4];
                    int type = YjygVo.getType(typeStr);
                    String date = strs[7];
                    YjygVo vo = new YjygVo(code, name, date, type, range, note);

                    yjygList.add(vo);
                    yjygMap.put(code, vo);
                    if (vo.isZeng()) {
                        zengList.add(vo);
                    } else if (vo.isYing()) {
                        yingList.add(vo);
                    } else if (vo.isJian()) {
                        jianList.add(vo);
                    } else if (vo.isChiping()) {
                        pingList.add(vo);
                    } else if (vo.isJing()) {
                        jingList.add(vo);
                    } else if (vo.iskui()) {
                        kuiList.add(vo);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error ", e);
        }
    }

    public static YjygLoader getInst() {
        if (inst == null) {
            inst = new YjygLoader();
            inst.init();
        }
        return inst;
    }

    private static YjygLoader inst = null;

    private YjygLoader() {
    }
}
