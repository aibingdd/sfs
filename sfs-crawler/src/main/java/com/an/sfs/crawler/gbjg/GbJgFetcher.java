package com.an.sfs.crawler.gbjg;

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

public class GbJgFetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(GbJgFetcher.class);
    // http://f10.eastmoney.com/f10_v2/CapitalStockStructure.aspx?code=sz000002
    private static final String URL = "http://f10.eastmoney.com/f10_v2/CapitalStockStructure.aspx?code=%s%s";

    public void run() {
        LOGGER.info("Download...");
        download();

        LOGGER.info("Analyze...");
        analyze();
    }

    private void download() {
        List<String> stockCodeList = StockLoader.getInst().getCodeList();
        for (String stock : stockCodeList) {
            String typeStr = StockLoader.getTypeStr(stock);

            String url = String.format(URL, typeStr, stock);
            String fp = AppFile.getInputGbjgRawDir() + File.separator + stock + ".html";
            if (!FileUtil.isFileExist(fp)) {
                AppUtil.download(url, fp);
            }
            convert(fp);
        }
    }

    private void analyze() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFile.getInputGbjgTxtDir(), files);
        for (File f : files) {
            String stock = FileUtil.getFileName(f.getPath());
            List<String> valList = new ArrayList<String>();
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (line.contains("dataR")) {
                        String val = FileUtil.extractVal(line);
                        valList.add(val);
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }

            String fp = AppFile.getOutputGbjgDir() + File.separator + stock + ".txt";
            StringBuilder text = new StringBuilder();
            FileUtil.convertListToText(valList, 4, text);
            FileUtil.writeFile(fp, text.toString());
        }
    }

    private void convert(String filePath) {
        String fn = FileUtil.getFileName(filePath);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(filePath)));
            String line = null;
            boolean start = false;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    if (!start && line.contains("<strong>股本构成</strong>")) {
                        start = true;
                        continue;
                    }
                    if (start && line.contains("<table>")) {
                        String text = line.trim();
                        text = text.replaceAll("><", ">\n<");
                        String fp = AppFile.getInputGbjgTxtDir() + File.separator + fn + ".txt";
                        FileUtil.writeFile(fp, text);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error, file: {}", filePath, e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
