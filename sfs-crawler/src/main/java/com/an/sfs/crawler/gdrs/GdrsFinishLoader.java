package com.an.sfs.crawler.gdrs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;

public class GdrsFinishLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GdrsFinishLoader.class);
    private List<String> codeList = new ArrayList<>();

    public GdrsFinishLoader() {
        init();
    }

    public void getCodeSet(Set<String> codeSet) {
        codeSet.addAll(codeList);
    }

    private void init() {
        try (BufferedReader br = new BufferedReader(new FileReader(AppFilePath.getConfDir() + "gdrs_finished.txt"))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    codeList.add(line.trim());
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error ", e);
        }
    }
}
