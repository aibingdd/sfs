package com.an.sfs.crawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GdrsFinishJumpLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GdrsFinishJumpLoader.class);
    private List<String> codeList = new ArrayList<>();

    public GdrsFinishJumpLoader() {
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
