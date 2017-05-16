package com.an.sfs.crawler.ccjg;

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

import com.an.sfs.crawler.SfsConf;
import com.an.sfs.crawler.tdx.StockLoader;
import com.an.sfs.crawler.util.AppFile;
import com.an.sfs.crawler.util.AppUtil;
import com.an.sfs.crawler.util.FileUtil;

public class CcjgFetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(CcjgFetcher.class);
    // http://data.eastmoney.com/zlsj/detail/2015-03-31-0-600009.html
    // http://datainterface.eastmoney.com/EM_DataCenter/JS.aspx?type=ZLSJ&sty=CCJGMX&st=2&sr=-1&p=1&ps=300&stat=0&code=000001&fd=2015-03-31
    private static final String URL = "http://datainterface.eastmoney.com/EM_DataCenter/JS.aspx?type=ZLSJ&sty=CCJGMX&st=2&sr=-1&p=1&ps=300&stat=0&code=%s&fd=%s";

    // ([{stats:false}])
    private static final String FLAG_EMPTY_DATA = "stats:false";
    private Map<String, String> earliestSeasons = new HashMap<>();

    public void run() {
        LOGGER.info("Load earliest seasons.");
        loadEarliestSeasons();

        LOGGER.info("Download...");
        download();

        LOGGER.info("Analyze...");
        analyze();
    }

    private void download() {
        List<String> stockCodeList = StockLoader.getInst().getCodeList();
        for (String stock : stockCodeList) {
            boolean finished = false;

            for (String season : SfsConf.getSeasonList()) {
                if (earliestSeasons.containsKey(stock)) {
                    if (season.compareTo(earliestSeasons.get(stock)) <= 0) {
                        break;// Ignore earlier season
                    }
                }

                String url = String.format(URL, stock, season);
                String filePath = AppFile.getInputCcjgRawDir(season) + File.separator + stock + ".txt";
                if (FileUtil.isFileExist(filePath)) {
                    continue;
                }

                AppUtil.download(url, filePath);

                try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        if (line.contains(FLAG_EMPTY_DATA)) {
                            finished = true;
                            earliestSeasons.put(stock, season);
                            String text = stock + "," + season + "\n";
                            FileUtil.writeFile(getEarliestSeasonFile(), text, true);
                        }
                        break;
                    }
                } catch (IOException e) {
                    LOGGER.error("Error ", e);
                }

                if (finished) {
                    // Delete empty data file
                    FileUtil.deleteFile(filePath);
                    break;
                }
            }
        }
    }

    private void analyze() {
        for (String season : SfsConf.getSeasonList()) {
            List<File> files = new ArrayList<>();
            String dir = AppFile.getInputCcjgRawDir(season);
            FileUtil.getFilesUnderDir(dir, files);
            for (File f : files) {
                String path = f.getPath();
                String fn = FileUtil.getFileName(path);
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(new File(path)));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        if (!line.isEmpty()) {
                            int startIndex = line.indexOf("[");
                            int endIndex = line.indexOf("]");
                            if (startIndex != -1 && endIndex != -1) {
                                String text = line.substring(startIndex + 1, endIndex);
                                text = text.replaceAll("\",", "\n");
                                text = text.replaceAll("\"", "");
                                String fp = AppFile.getInputCcjgTxtDir(season) + File.separator + fn + ".txt";
                                FileUtil.writeFile(fp, text);
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Error, file: {}", path, e);
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
    }

    private void loadEarliestSeasons() {
        String file = getEarliestSeasonFile();
        if (FileUtil.isFileExist(file)) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        String[] strs = line.split(",");
                        earliestSeasons.put(strs[0], strs[1]);
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }
        }
    }

    private String getEarliestSeasonFile() {
        return AppFile.getOutputCcjgDir() + File.separator + "earliestSeason.txt";
    }
}
