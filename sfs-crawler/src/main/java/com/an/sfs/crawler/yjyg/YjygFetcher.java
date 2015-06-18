package com.an.sfs.crawler.yjyg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.AppUtil;
import com.an.sfs.crawler.FileUtil;

public class YjygFetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(YjygFetcher.class);
    // http://data.eastmoney.com/zlsj/detail/2015-03-31-0-600009.html
    // http://datainterface.eastmoney.com/EM_DataCenter/JS.aspx?type=SR&sty=YJYG&fd=2015-06-30&st=4&sr=-1&p=1&ps=50&js=var%20HNKTWZPc={pages:(pc),data:[(x)]}&stat=0&rt=47820847
    private static final String URL = "http://datainterface.eastmoney.com/EM_DataCenter/JS.aspx?type=SR&sty=YJYG&fd=%s&st=4&sr=-1&p=1&ps=3000&&stat=0";

    public void run() {
        download();
    }

    public static String getYjygTxtFile(String season) {
        String txtFilePath = AppFilePath.getInputYjygTxtDir() + File.separator + season + ".txt";
        return txtFilePath;
    }

    private void download() {
        String season = AppUtil.SEASON_YJYG;
        String url = String.format(URL, season);
        String rawFilePath = AppFilePath.getInputYjygRawDir() + File.separator + season + ".txt";
        if (!FileUtil.isFileExist(rawFilePath)) {
            AppUtil.download(url, rawFilePath);
        }
        analyze(rawFilePath);
    }

    private void analyze(String rawFilePath) {
        String season = AppUtil.SEASON_YJYG;
        String txtFilePath = getYjygTxtFile(season);
        try (BufferedReader br = new BufferedReader(new FileReader(rawFilePath))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    int startIndex = line.indexOf("[");
                    int endIndex = line.indexOf("]");
                    if (startIndex != -1 && endIndex != -1) {
                        String text = line.substring(startIndex + 1, endIndex);
                        text = text.replaceAll("\",", "\n");
                        text = text.replaceAll("\"", "");
                        text = text.replaceAll("&sbquo", "");
                        text = text.replaceAll("～", ":");
                        LOGGER.info("Save file: {}", txtFilePath);
                        FileUtil.writeFile(txtFilePath, text);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error ", e);
        }
    }
}
