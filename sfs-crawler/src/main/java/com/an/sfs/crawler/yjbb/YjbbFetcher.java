package com.an.sfs.crawler.yjbb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.tdx.StockLoader;
import com.an.sfs.crawler.util.AppFile;
import com.an.sfs.crawler.util.AppUtil;
import com.an.sfs.crawler.util.FileUtil;

public class YjbbFetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(YjbbFetcher.class);
    // http://data.eastmoney.com/bbsj/stock300015/yjbb.html
    private static final String YYBB_URL = "http://data.eastmoney.com/bbsj/stock%s/yjbb.html";

    private static final String FLAG_GDRS = "<strong>股东人数</strong>";
    private static final String FLAG_SDLTGD = "<strong>十大流通股东</strong>";

    private static final String FLAG_DATA = "tips-dataL";

    public void run() {
        LOGGER.info("Fetch ...");
        fetchHtml(YYBB_URL);
        LOGGER.info("Analyze ...");
        // // analyzeGdrs();
        // analyzeSdltgd();
    }

    private void fetchHtml(String url) {
        List<String> stockList = StockLoader.getInst().getCodeList();
        for (String stock : stockList) {
            String httpUrl = String.format(url, stock);
            String filePath = AppFile.getInputYjbbRawDir() + File.separator + stock + ".html";
            if (!FileUtil.isFileExist(filePath)) {
                AppUtil.download(httpUrl, filePath);
            }

            extract(filePath);
        }
    }

    private void extract(String fp) {
        String stock = FileUtil.getFileName(fp);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fp)), "GB2312"))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.contains("defjson:")) {
                    line = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
                    line = line.replace("},{", "},\n{");
                    String outfp = AppFile.getInputYjbbTxtDir() + File.separator + stock + ".txt";
                    FileUtil.writeFile(outfp, line, 1, true);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error {}", fp, e);
        }
    }

    private void analyzeGdrs() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFile.getInputGdyjGdrsDir(), files);
        for (File f : files) {
            String stock = FileUtil.getFileName(f.getPath());
            List<String> list = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (line.contains(FLAG_DATA)) {
                        list.add(FileUtil.extractVal(line));
                    }
                }

                StringBuilder text = new StringBuilder();
                FileUtil.convertListToText(list, 10, text);
                String fp = AppFile.getOutputGdyjGdrsDir() + File.separator + stock + ".txt";
                FileUtil.writeFile(fp, text.toString());
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }
        }
    }

    private void analyzeSdltgd() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFile.getInputGdyjSdltgdDir(), files);
        for (File f : files) {
            String stock = FileUtil.getFileName(f.getPath());
            StringBuilder text = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line = null;
                int i = 0;
                while ((line = br.readLine()) != null) {
                    if (line.contains("合计")) {
                        break;
                    }
                    if (line.contains(FLAG_DATA)) {
                        String val = FileUtil.extractVal(line);
                        if (val != null) {
                            text.append(val);
                            if ((i + 1) % 7 == 0) {
                                text.append("\n");
                            } else {
                                text.append(";");
                            }
                            i++;
                        }
                    }
                }
                String fp = AppFile.getOutputGdyjSdltgdDir() + File.separator + stock + ".txt";
                FileUtil.writeFile(fp, text.toString());
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }
        }
    }
}
