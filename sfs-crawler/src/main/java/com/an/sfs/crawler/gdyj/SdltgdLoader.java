package com.an.sfs.crawler.gdyj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.util.AppFile;
import com.an.sfs.crawler.util.FileUtil;

public class SdltgdLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(SdltgdLoader.class);
    private List<SdltgdVo> sdltgdList = new ArrayList<>();

    private void init() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFile.getOutputGdyjSdltgdDir(), files);
        for (File f : files) {
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        line = line.replaceAll(",", "");
                        String[] strs = line.split(";");
                        String jgName = strs[0];
                        String jgType = strs[1];
                        String stockType = strs[2];
                        long stockCount = (long) (Float.parseFloat(strs[3]) * 10000f);
                        float stockPer = Float.parseFloat(strs[4]);
                        SdltgdVo vo = new SdltgdVo(jgName, jgType, stockType, stockCount, stockPer);
                        sdltgdList.add(vo);
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }
        }
    }

    private static SdltgdLoader inst;

    private SdltgdLoader() {
    }

    public static SdltgdLoader getInst() {
        if (inst == null) {
            inst = new SdltgdLoader();
            inst.init();
        }
        return inst;
    }
}
