package com.an.sfs.crawler.code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;

public class StockCodeLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockCodeLoader.class);
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

    public static StockCodeLoader getInst() {
        if (inst == null) {
            inst = new StockCodeLoader();
            inst.init();
        }
        return inst;
    }

    private static StockCodeLoader inst = null;

    private StockCodeLoader() {
    }
}
