package com.an.sfs.crawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeLoader.class);
    /**
     * Shanghai stock codes
     */
    private List<String> shCodes = new ArrayList<>();
    private List<String> szCodes = new ArrayList<>();

    private static final CodeLoader inst = new CodeLoader();

    private CodeLoader() {
        init();
    }

    public static CodeLoader getInst() {
        return inst;
    }

    private void init() {
        String shEbk = FileDirUtil.getShEbk();
        try (BufferedReader br = new BufferedReader(new FileReader(shEbk));) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    shCodes.add(line.trim().substring(1));
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while parsing shEbk.");
        }

        String szEbk = FileDirUtil.getSzEbk();
        try (BufferedReader br = new BufferedReader(new FileReader(szEbk));) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    szCodes.add(line.trim().substring(1));
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while parsing szEbk.");
        }
    }

    /**
     * @param result
     */
    public void getShCodes(List<String> result) {
        result.addAll(shCodes);
    }

    /**
     * @param result
     */
    public void getSzCodes(List<String> result) {
        result.addAll(szCodes);
    }
}
