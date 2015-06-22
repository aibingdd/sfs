package com.an.sfs.crawler.cwfx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.AppUtil;
import com.an.sfs.crawler.FileUtil;
import com.an.sfs.crawler.gsgk.StockIndustryLoader;
import com.an.sfs.crawler.name.IndustryVo;

public class InvalidCwfxLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvalidCwfxLoader.class);

    private String getInvalidCwfxFile() {
        return AppFilePath.getOutputDir() + File.separator + "Invalid_Cwfx_Rona_Rota.txt";
    }

    private Set<String> stockSet = new HashSet<>();
    private List<String> stockList = new ArrayList<>();

    public List<String> getStockList() {
        return stockList;
    }

    public Set<String> getStockSet() {
        return stockSet;
    }

    private void init() {
        extract();

        try (BufferedReader br = new BufferedReader(new FileReader(getInvalidCwfxFile()))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] strs = line.split(",");
                stockList.add(strs[0]);
            }
        } catch (IOException e) {
            LOGGER.error("Error ", e);
        }

        stockSet.addAll(stockList);
    }

    private void extract() {
        List<File> files = new ArrayList<File>();
        FileUtil.getFilesUnderDir(AppFilePath.getOutputCwfxDir(), files);

        StringBuilder text = new StringBuilder();
        for (File f : files) {
            String stock = FileUtil.getFileName(f.toString());
            IndustryVo industry = StockIndustryLoader.getInst().getIndustry(stock);
            try (BufferedReader br = new BufferedReader(new FileReader(f));) {
                String line = null;
                int i = 0;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        if (i++ < 3) {
                            line = line.replaceAll(",", "");
                            String[] strs = line.split(";");
                            if (!AppUtil.INVALID.equals(strs[3])) {
                                float rona = Float.parseFloat(strs[3]);
                                if (rona > 100f) {
                                    text.append(stock).append(",").append(industry.getIndustry()).append(",")
                                            .append(line).append("\n");
                                    break;
                                }
                                if (rona < -50f) {
                                    text.append(stock).append(",").append(industry.getIndustry()).append(",")
                                            .append(line).append("\n");
                                    break;
                                }
                            }
                            if (!AppUtil.INVALID.equals(strs[4])) {
                                float rota = Float.parseFloat(strs[4]);
                                if (rota > 100f) {
                                    text.append(stock).append(",").append(industry.getIndustry()).append(",")
                                            .append(line).append("\n");
                                    break;
                                }
                                if (rota < -50f) {
                                    text.append(stock).append(",").append(industry.getIndustry()).append(",")
                                            .append(line).append("\n");
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error, code {}", stock, e);
                break;
            }
        }

        FileUtil.writeFile(getInvalidCwfxFile(), text.toString());
    }

    private InvalidCwfxLoader() {
    }

    private static InvalidCwfxLoader inst;

    public static InvalidCwfxLoader getInst() {
        if (inst == null) {
            inst = new InvalidCwfxLoader();
            inst.init();
        }
        return inst;
    }

    public static void main(String[] args) {
        for (String stock : InvalidCwfxLoader.getInst().getStockList()) {
            System.out.println(stock);
        }
    }
}
