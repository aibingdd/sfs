package com.an.sfs.crawler.cwfx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.AppUtil;
import com.an.sfs.crawler.FileUtil;
import com.an.sfs.crawler.name.StockLoader;
import com.an.sfs.crawler.name.StockVo;

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

    public void run() {
        LOGGER.info("Fetch CWFX.");
        fetchCwfxData(URL, AppFilePath.getInputCwfxRawDir());

        LOGGER.info("Analyze CWFX...");
        analyzeCwfx();
    }

    private void fetchCwfxData(String url, String fileDir) {
        List<StockVo> stocks = StockLoader.getInst().getStocks();
        for (StockVo vo : stocks) {
            String code = vo.getCode();
            String codeSuffix = vo.getCodeSuffix();
            String httpUrl = String.format(url, code, codeSuffix, Count, vo.getTypeStr(), code, n);
            String fp = fileDir + File.separator + code + ".txt";
            if (!FileUtil.isFileExist(fp)) {
                AppUtil.download(httpUrl, fp);
            }
            FileUtil.formatHtmlFile(fp);
        }
    }

    private void analyzeCwfx() {
        List<File> fileList = new ArrayList<>();
        String dirPath = AppFilePath.getInputCwfxRawDir();
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
                            String time = FileUtil.extractVal(line);
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
                        if (line.contains(FLAG_VALUE)) {
                            String income = FileUtil.extractVal(line);
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
                        if (line.contains(FLAG_VALUE)) {
                            String netProfit = FileUtil.extractVal(line);
                            list.add(netProfit);
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
                            String rona = FileUtil.extractVal(line);
                            list.add(rona);
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
                            String rota = FileUtil.extractVal(line);
                            list.add(rota);
                        }
                        if (line.contains("</tr>")) {
                            finishROTA = true;
                        }
                    }
                    // Finish all
                    if (finishTime && finishIncome && finishNetProfit && finishRONA && finishROTA) {
                        break;
                    }
                }

                StringBuilder text = new StringBuilder();
                FileUtil.convertListToText(list, 5, text);
                String fp = AppFilePath.getOutputCwfxDir() + File.separator + stock + ".txt";
                LOGGER.info("Save file {}", fp);
                FileUtil.writeFile(fp, text.toString());
            } catch (IOException e) {
                LOGGER.error("Error while analyzing file {}", f.getPath());
            }
        }
    }
}
