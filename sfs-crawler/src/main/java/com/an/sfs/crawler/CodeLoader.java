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

    private void init() {
        String shEbk = AppFilePath.getConfFile("SH.EBK");
        try (BufferedReader br = new BufferedReader(new FileReader(shEbk));) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    shCodes.add(line.trim().substring(1));
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while parsing SH.EBK.");
        }

        String szEbk = AppFilePath.getConfFile("SZ.EBK");
        try (BufferedReader br = new BufferedReader(new FileReader(szEbk));) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    szCodes.add(line.trim().substring(1));
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while parsing SZ.EBK.");
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

    //

    public static CodeLoader getInst() {
        if (inst == null) {
            inst = new CodeLoader();
            inst.init();
        }
        return inst;
    }

    private static CodeLoader inst = null;

    private CodeLoader() {
    }
}
