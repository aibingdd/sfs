package com.an.sfs.crawler.gsgk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.FileUtil;

public class IndustryLoader {
    private List<String> industryList = new ArrayList<>();

    public List<String> getIndustryList() {
        return industryList;
    }

    private void init() {
        String fp = getIndustryFile();
        if (!FileUtil.isFileExist(fp)) {
            exportIndustry();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(fp))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    industryList.add(line.trim());
                }
            }
        } catch (IOException e) {
        }
    }

    private static void exportIndustry() {
        List<GsgkVo> voList = GsgkLoader.getInst().getGsgkList();
        Set<String> set = new HashSet<>();
        for (GsgkVo vo : voList) {
            set.add(vo.getIndustry());
        }
        List<String> list = new ArrayList<>();
        list.addAll(set);
        Collections.sort(list);

        StringBuilder text = new StringBuilder();
        for (String in : list) {
            text.append(in).append("\n");
        }
        FileUtil.writeFile(getIndustryFile(), text.toString());
    }

    private static String getIndustryFile() {
        return AppFilePath.getOutputDir() + File.separator + "Industry.txt";
    }

    private IndustryLoader() {
    }

    private static IndustryLoader inst;

    public static IndustryLoader getInst() {
        if (inst == null) {
            inst = new IndustryLoader();
            inst.init();
        }
        return inst;
    }

    public static void main(String[] args) {
        IndustryLoader.getInst();
    }
}
