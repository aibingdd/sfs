package com.an.sfs.crawler.cninfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.tdx.StockLoader;
import com.an.sfs.crawler.util.AppFile;
import com.an.sfs.crawler.util.AppUtil;
import com.an.sfs.crawler.util.FileUtil;

public class CninfoFetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(CninfoFetcher.class);
    // condition.stockcode=000553&pageNo=2
    private static final String URL = "http://irm.cninfo.com.cn/ircs/interaction/lastRepliesforSzseSsgs.do?condition.type=1&condition.stockcode=%s&condition.stocktype=S&pageNo=%s";

    private static final String FLAG_PAGE_COUNT = "javascript:toPage(";
    private static final String FLAG_TIME = "（20";

    private List<String> stockCodeList;
    // Stock -> PageCount
    private Map<String, Integer> stockPageCountMap = new HashMap<>();

    public void run() {
        stockCodeList = StockLoader.getInst().getStockCodeList();
        LOGGER.info("Fetch raw html.");
        fetchRaw(URL, AppFile.getInputCninfoRawDir());

        LOGGER.info("Extract Page Count.");
        extractPageCount();

        LOGGER.info("Fetch detail html.");
        fetchDetail(URL);

        LOGGER.info("Extract ask and answer.");
        extractAskAnswer();

        LOGGER.info("Extract GDRS.");
        extractGdrs();
    }

    private void fetchRaw(String url, String fileDir) {
        int PAGE_INDEX = 1;
        for (String stock : stockCodeList) {
            if (stock.startsWith("3") || stock.startsWith("0")) {
                String httpUrl = String.format(url, stock, PAGE_INDEX);
                String filePath = fileDir + File.separator + stock + ".html";
                if (!FileUtil.isFileExist(filePath)) {
                    AppUtil.download(httpUrl, filePath);
                }
            }
        }
    }

    private void extractPageCount() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFile.getInputCninfoRawDir(), files);
        StringBuilder text = new StringBuilder();
        for (File f : files) {
            String filePath = f.getPath();
            String stockCode = FileUtil.getFileName(filePath);
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line = null;
                int maxPageIndex = -1;
                while ((line = br.readLine()) != null) {
                    int beginIndex = line.indexOf(FLAG_PAGE_COUNT);
                    if (beginIndex != -1) {
                        int endIndex = line.indexOf(")", beginIndex);
                        String str = line.substring(beginIndex + FLAG_PAGE_COUNT.length(), endIndex);
                        int cnt = Integer.parseInt(str);
                        if (cnt > maxPageIndex) {
                            maxPageIndex = cnt;
                        }
                    }
                }
                if (maxPageIndex < 1) {
                    maxPageIndex = 1;
                }

                // Export to file
                stockPageCountMap.put(stockCode, maxPageIndex);
                text.append(stockCode).append(",").append(maxPageIndex).append("\n");
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }
        }
        FileUtil.writeFile(AppFile.getOutputCninfoDir() + File.separator + "pageCount.txt", text.toString());
    }

    private void fetchDetail(String url) {
        for (String stock : stockCodeList) {
            String detailDir = AppFile.getInputCninfoDetailDir(stock);
            AppFile.mkdirs(detailDir);
            if (stock.startsWith("3") || stock.startsWith("0")) {
                int pageCount = stockPageCountMap.get(stock);
                for (int pageIndex = 1; pageIndex < pageCount + 1; pageIndex++) {
                    String httpUrl = String.format(url, stock, pageIndex);
                    String filePath = detailDir + File.separator + String.format("%02d", pageIndex) + ".html";
                    if (!FileUtil.isFileExist(filePath)) {
                        AppUtil.download(httpUrl, filePath);
                    }
                }
            }
        }
    }

    private void extractAskAnswer() {
        for (String stock : stockCodeList) {
            List<File> files = new ArrayList<>();
            FileUtil.getSortedFilesUnderDir(AppFile.getInputCninfoDetailDir(stock), files);

            StringBuilder text = new StringBuilder();
            String askAnswer = null;
            for (File f : files) {
                String line = null;
                try (BufferedReader br = new BufferedReader(new FileReader(f.getPath()))) {
                    boolean isAskAnswerLine = false;
                    while ((line = br.readLine()) != null) {
                        if (line.indexOf("class=\"ask\"") != -1 || line.indexOf("class=\"answer\"") != -1) {
                            isAskAnswerLine = true;
                            continue;
                        }
                        if (isAskAnswerLine) {
                            askAnswer = line.trim();
                            isAskAnswerLine = false;
                            continue;
                        }
                        if (line.trim().startsWith(FLAG_TIME)) {
                            int fromIndex = line.indexOf(FLAG_TIME) + FLAG_TIME.length() - 2;
                            int endIndex = line.indexOf("）", fromIndex);
                            String time = line.substring(fromIndex, endIndex);
                            text.append(time).append(" ").append(askAnswer).append("\n");
                            continue;
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Error {}", line, e);
                }
            }
            String fp = AppFile.getInputCninfoTxtDir() + File.separator + stock + ".txt";
            FileUtil.writeFile(fp, text.toString());
        }
    }

    private void extractGdrs() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFile.getInputCninfoTxtDir(), files);
        for (File f : files) {
            String fileName = FileUtil.getFileNameFull(f.getPath());
            if (fileName.startsWith("3") || fileName.startsWith("2")) {
                try (BufferedReader br = new BufferedReader(new FileReader(f.getPath()))) {
                    String line = null;
                    boolean isGdrsLine = false;
                    StringBuilder text = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        if (line.contains("人数")) {
                            text.append(line).append("\n");
                            isGdrsLine = true;
                            continue;
                        }
                        if (isGdrsLine) {
                            text.append(line).append("\n");
                            isGdrsLine = false;
                            continue;
                        }
                    }
                    FileUtil.writeFile(AppFile.getOutputCninfoGdrsDir() + File.separator + fileName, text.toString());
                } catch (IOException e) {
                    LOGGER.error("Error ", e);
                }
            }
        }
    }
}
