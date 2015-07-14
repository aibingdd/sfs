package com.an.sfs.crawler.cwfx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.tdx.StockLoader;
import com.an.sfs.crawler.util.AppFile;
import com.an.sfs.crawler.util.AppUtil;
import com.an.sfs.crawler.util.FileUtil;

public class CwfxFetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(CwfxFetcher.class);
    // Cai wu fen xi
    private static final int Count = 4;// Year count or Season Count
    private static final int n = 1;// For year
    // 60000001 or 00000102
    // num=6 year count or season count
    // n=1 for year, n=2 for season
    // http://f10.eastmoney.com/f10_v2/BackOffice.aspx?command=RptF10MainTarget&code=00254302&num=9&code1=sz002543&n=1
    private static final String URL = "http://f10.eastmoney.com/f10_v2/BackOffice.aspx?command=RptF10MainTarget&code=%s%s&num=%s&code1=%s%s&n=%s";

    private static final String FLAG_VALUE = "<span>";

    private static final String FLAG_TIME = "成长能力指标";
    private static final String FLAG_INCOME = "营业收入(元)";

    private static final String FLAG_NET_PROFIT = "扣非净利润(元)";
    // Rate of return on net assets
    private static final String FLAG_RONA = "摊薄净资产收益率";
    // Return on Total Assets
    private static final String FLAG_ROTA = "摊薄总资产收益率";
    // debt to assets ratio
    private static final String FLAG_DTAR = "资产负债率";
    // Primary Earnings Per Share
    private static final String FLAG_PEPS = "基本每股收益";
    // Net assets per share
    private static final String FLAG_NAPS = "每股净资产";

    public void run() {
        LOGGER.info("Fetch CWFX.");
        fetchCwfxData(URL, AppFile.getInputCwfxRawDir());

        LOGGER.info("Analyze CWFX...");
        analyzeCwfx();
    }

    private void fetchCwfxData(String url, String fileDir) {
        List<String> stockCodeList = StockLoader.getInst().getStockCodeList();
        for (String stock : stockCodeList) {
            String codeSuffix = StockLoader.getCodeSuffix(stock);
            String typeStr = StockLoader.getTypeStr(stock);
            String httpUrl = String.format(url, stock, codeSuffix, Count, typeStr, stock, n);
            String fp = fileDir + File.separator + stock + ".txt";
            if (!FileUtil.isFileExist(fp)) {
                AppUtil.download(httpUrl, fp);
            }
            FileUtil.formatHtmlFile(fp);
        }
    }

    private void analyzeCwfx() {
        List<File> fileList = new ArrayList<>();
        String dirPath = AppFile.getInputCwfxRawDir();
        FileUtil.getFilesUnderDir(dirPath, fileList);
        for (File f : fileList) {
            String stock = FileUtil.getFileName(f.getPath());

            List<String> list = new ArrayList<>();
            boolean finishTime = false;
            boolean beginTime = false;
            boolean finishIncome = false;
            boolean beginIncome = false;
            boolean finishNetProfit = false;
            boolean beginNetProfit = false;
            boolean finishRONA = false;
            boolean beginRONA = false;
            boolean finishROTA = false;
            boolean beginROTA = false;
            boolean finishDTAR = false;
            boolean beginDTAR = false;
            boolean finishPEPS = false;
            boolean beginPEPS = false;
            boolean finishNAPS = false;
            boolean beginNAPS = false;
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    // Handle time
                    if (!finishTime && line.indexOf(FLAG_TIME) != -1) {
                        beginTime = true;
                        continue;
                    }
                    if (!finishTime && beginTime) {
                        if (line.contains(FLAG_VALUE)) {
                            String val = FileUtil.extractVal(line);
                            list.add(val);
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
                        if (line.contains(FLAG_VALUE)) {
                            String val = FileUtil.extractVal(line);
                            list.add(val);
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
                        if (line.contains(FLAG_VALUE)) {
                            String val = FileUtil.extractVal(line);
                            list.add(val);
                        }
                        if (line.contains("</tr>")) {
                            finishNetProfit = true;
                        }
                    }

                    // Handle net profit ratio
                    if (!finishRONA && line.indexOf(FLAG_RONA) != -1) {
                        beginRONA = true;
                        continue;
                    }
                    if (!finishRONA && beginRONA) {
                        if (line.contains(FLAG_VALUE)) {
                            String val = FileUtil.extractVal(line);
                            list.add(val);
                        }
                        if (line.contains("</tr>")) {
                            finishRONA = true;
                        }
                    }

                    // Return on Total Assets
                    if (!finishROTA && line.indexOf(FLAG_ROTA) != -1) {
                        beginROTA = true;
                        continue;
                    }
                    if (!finishROTA && beginROTA) {
                        if (line.contains(FLAG_VALUE)) {
                            String val = FileUtil.extractVal(line);
                            list.add(val);
                        }
                        if (line.contains("</tr>")) {
                            finishROTA = true;
                        }
                    }

                    // Debt to Assets Ratio
                    if (!finishDTAR && line.indexOf(FLAG_DTAR) != -1) {
                        beginDTAR = true;
                        continue;
                    }
                    if (!finishDTAR && beginDTAR) {
                        if (line.contains(FLAG_VALUE)) {
                            String val = FileUtil.extractVal(line);
                            list.add(val);
                        }
                        if (line.contains("</tr>")) {
                            finishDTAR = true;
                        }
                    }

                    // Primary Earnings Per Share
                    if (!finishDTAR && line.indexOf(FLAG_PEPS) != -1) {
                        beginPEPS = true;
                        continue;
                    }
                    if (!finishPEPS && beginPEPS) {
                        if (line.contains(FLAG_VALUE)) {
                            String val = FileUtil.extractVal(line);
                            list.add(val);
                        }
                        if (line.contains("</tr>")) {
                            finishPEPS = true;
                        }
                    }

                    // Primary Earnings Per Share
                    if (!finishNAPS && line.indexOf(FLAG_NAPS) != -1) {
                        beginNAPS = true;
                        continue;
                    }
                    if (!finishNAPS && beginNAPS) {
                        if (line.contains(FLAG_VALUE)) {
                            String val = FileUtil.extractVal(line);
                            list.add(val);
                        }
                        if (line.contains("</tr>")) {
                            finishNAPS = true;
                        }
                    }
                    // Finish all
                    if (finishTime && finishIncome && finishNetProfit && finishRONA && finishROTA && finishDTAR
                            && finishPEPS && finishNAPS) {
                        break;
                    }
                }

                StringBuilder text = new StringBuilder();
                FileUtil.convertListToText(list, 8, text);
                String fp = AppFile.getOutputCwfxDir() + File.separator + stock + ".txt";
                FileUtil.writeFile(fp, text.toString());
            } catch (IOException e) {
                LOGGER.error("Error while analyzing file {}", f.getPath());
            }
        }
    }
}
