package com.an.sfs.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CwfxAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CwfxAnalyzer.class);
    private static final String FLAG_TIME = "成长能力指标";
    private static final String FLAG_INCOME = "营业收入(元)";
    private static final String FLAG_NET_PROFIT = "扣非净利润(元)";
    private static final String FLAG_START_DATA = "<span>";
    private static final String FLAG_END_DATA = "</span>";

    public void run() {
        analyzeGdrs();
    }

    private void analyzeGdrs() {
        List<File> fileList = new ArrayList<>();
        String dirPath = AppFilePath.getInputCwfxYearDir();
        FileUtil.getFilesUnderDir(dirPath, fileList);
        for (File f : fileList) {
            LOGGER.info("Process file {}", f.getPath());
            String stockCode = f.getName();
            stockCode = stockCode.substring(0, stockCode.indexOf("."));

            List<String> list = new ArrayList<>();
            boolean finishTime = false;
            boolean beginTime = false;
            boolean finishIncome = false;
            boolean beginIncome = false;
            boolean finishNetProfit = false;
            boolean beginNetProfit = false;
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    // Handle time
                    if (!finishTime && line.indexOf(FLAG_TIME) != -1) {
                        beginTime = true;
                        continue;
                    }
                    if (!finishTime && beginTime) {
                        int timeFromIdx = line.indexOf(FLAG_START_DATA);
                        if (timeFromIdx != -1) {
                            timeFromIdx = timeFromIdx + FLAG_START_DATA.length();
                            int timeToIdx = line.indexOf(FLAG_END_DATA);
                            String time = line.substring(timeFromIdx, timeToIdx);
                            list.add(time);
                        }
                        if (line.contains("</tr>")) {
                            finishTime = true;
                        }
                    }
                    // Handle income
                    if (!finishIncome && line.indexOf(FLAG_INCOME) != -1) {
                        beginIncome = true;
                        continue;
                    }
                    if (!finishIncome && beginIncome) {
                        int incomeFromIdx = line.indexOf(FLAG_START_DATA);
                        if (incomeFromIdx != -1) {
                            incomeFromIdx = incomeFromIdx + FLAG_START_DATA.length();
                            int incomeToIdx = line.indexOf(FLAG_END_DATA);
                            String income = line.substring(incomeFromIdx, incomeToIdx);
                            list.add(income);
                        }
                        if (line.contains("</tr>")) {
                            finishIncome = true;
                        }
                    }
                    // Handle net profit
                    if (!finishNetProfit && line.indexOf(FLAG_NET_PROFIT) != -1) {
                        beginNetProfit = true;
                        continue;
                    }
                    if (!finishNetProfit && beginNetProfit) {
                        int netProfitFromIdx = line.indexOf(FLAG_START_DATA);
                        if (netProfitFromIdx != -1) {
                            netProfitFromIdx = netProfitFromIdx + FLAG_START_DATA.length();
                            int netProfitToIdx = line.indexOf(FLAG_END_DATA);
                            String netProfit = line.substring(netProfitFromIdx, netProfitToIdx);
                            list.add(netProfit);
                        }
                        if (line.contains("</tr>")) {
                            finishNetProfit = true;
                        }
                    }

                    // Finish all
                    if (finishTime && finishIncome && finishNetProfit) {
                        break;
                    }
                }

                String filePath = AppFilePath.getOutputCwfxYearDir() + File.separator + stockCode + ".txt";
                AppUtil.convertListToFile(list, 3, filePath);
            } catch (IOException e) {
                LOGGER.error("Error while analyzing file {}", f.getPath());
            }
        }
    }
}
