package com.an.sfs.crawler.fhrz;

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
import com.an.sfs.crawler.name.StockLoader;
import com.an.sfs.crawler.name.StockVo;

public class FhrzFetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(FhrzFetcher.class);
    // sh600830
    private static final String URL = "http://f10.eastmoney.com/f10_v2/BonusFinancing.aspx?code=%s%s";

    private Set<String> tfpUrls = new HashSet<>();
    private static final String FLAG_TFP = "<h2 class=\"title\"><a href=\"../../news/zonghe";
    private static final String TFP_URL_FILE = "TfpUrl.txt";
    public static final String TFP_FILE = "TFP.txt";

    public void run() {
        fetchRawHtml(URL, AppFilePath.getInputFhrzRawDir());
        LOGGER.info("Extract FHYX.");
        extractFhyx();
        LOGGER.info("Extract ZFMX.");
        extractZfmx();
    }

    private void fetchRawHtml(String url, String fileDir) {
        List<StockVo> stocks = StockLoader.getInst().getStocks();
        for (StockVo vo : stocks) {
            String code = vo.getCode();
            String typeStr = vo.getTypeStr();
            String httpUrl = String.format(url, typeStr, code);
            String filePath = fileDir + File.separator + code + ".html";
            if (!FileUtil.isFileExist(filePath)) {
                AppUtil.download(httpUrl, filePath);
            }
        }
    }

    private void extractFhyx() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFilePath.getInputFhrzRawDir(), files);
        for (File f : files) {
            String filePath = f.getPath();
            String fileName = FileUtil.getFileName(filePath);
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (line.contains("BonusDetailsTable")) {
                        String text = line.trim().replaceAll("><", ">\n<");
                        FileUtil.writeFile(AppFilePath.getInputFhrzFhyxDir() + File.separator + fileName + ".txt", text);
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }
        }
    }

    private void extractZfmx() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFilePath.getInputFhrzRawDir(), files);
        for (File f : files) {
            String filePath = f.getPath();
            String fileName = FileUtil.getFileName(filePath);
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line = null;
                String text = null;
                while ((line = br.readLine()) != null) {
                    if (line.contains("增发时间")) {
                        text = line.trim().replaceAll("><", ">\n<");
                        if (text.endsWith("</table>")) {
                            break;
                        }
                    }
                    if (text != null) {
                        text = text + line.trim();
                        if (text.endsWith("</table>")) {
                            break;
                        }
                    }
                }
                if (text != null) {
                    FileUtil.writeFile(AppFilePath.getInputFhrzZfmxDir() + File.separator + fileName + ".txt", text);
                }
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }
        }
    }
}
