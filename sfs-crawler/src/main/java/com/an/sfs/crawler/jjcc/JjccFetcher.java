package com.an.sfs.crawler.jjcc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.SfsConf;
import com.an.sfs.crawler.name.FundLoader;
import com.an.sfs.crawler.name.FundVo;
import com.an.sfs.crawler.util.AppFile;
import com.an.sfs.crawler.util.AppUtil;
import com.an.sfs.crawler.util.FileUtil;

public class JjccFetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(JjccFetcher.class);
    // 201503/000173.html
    // http://data.eastmoney.com/zlsj/ccjj/2015-03-31-000173.html
    private static final String URL = "http://data.eastmoney.com/zlsj/ccjj/%s-%s.html";

    // defjson: {pages:0,data:[{stats:false}]}
    private static final String FLAG_DATA = "defjson";
    private static final String FLAG_ZERO_ROW = "pages:0";
    private Map<String, String> earliestSeasons = new HashMap<>();

    public void run() {
        LOGGER.info("Load earliest seasons.");
        loadEarliestSeasons();

        LOGGER.info("Download...");
        download();

        LOGGER.info("Analyze...");
        analyze();
    }

    private void analyze() {
        for (String season : SfsConf.getSeasonList()) {
            List<File> fileList = new ArrayList<>();
            String dir = AppFile.getInputJjccRawDir(season);
            FileUtil.getFilesUnderDir(dir, fileList);
            for (File f : fileList) {
                String fileName = FileUtil.getFileName(f.getPath());
                BufferedReader br = null;
                try {
                    FileInputStream fis = new FileInputStream(new File(f.getPath()));
                    br = new BufferedReader(new InputStreamReader(fis, "GB2312"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        if (line.contains(FLAG_DATA)) {
                            int startIndex = line.indexOf("[");
                            int endIndex = line.indexOf("]");
                            if (startIndex != -1 && endIndex != -1) {
                                String text = line.substring(startIndex + 1, endIndex);
                                text = text.replaceAll("\",", "\n");
                                text = text.replaceAll("\"", "");
                                String fp = AppFile.getInputJjccTxtDir(season) + File.separator + fileName + ".txt";
                                FileUtil.writeFile(fp, text);
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Error, file: {}", f.getPath(), e);
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

    private void download() {
        List<FundVo> funds = FundLoader.getInst().getFunds();
        for (FundVo vo : funds) {
            boolean finished = false;
            String code = vo.getCode();

            for (String season : SfsConf.getSeasonList()) {
                if (earliestSeasons.containsKey(code)) {
                    if (season.compareTo(earliestSeasons.get(code)) <= 0) {
                        // Ignore earlier season
                        break;
                    }
                }

                String url = String.format(URL, season, code);
                String filePath = AppFile.getInputJjccRawDir(season) + File.separator + code + ".html";
                if (FileUtil.isFileExist(filePath)) {
                    continue;
                }

                AppUtil.download(url, filePath);

                try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        if (line.contains(FLAG_DATA)) {
                            if (line.contains(FLAG_ZERO_ROW)) {
                                finished = true;
                                earliestSeasons.put(code, season);
                                String text = code + "," + season + "\n";
                                FileUtil.writeFile(getEarliestSeaonFile(), text, true);
                            }
                            break;
                        }
                    }
                } catch (IOException e) {
                    LOGGER.error("Error ", e);
                }

                if (finished) {
                    // Delete empty data web page
                    FileUtil.deleteFile(filePath);
                    break;
                }
            }
        }
    }

    private void loadEarliestSeasons() {
        String file = getEarliestSeaonFile();
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

    private String getEarliestSeaonFile() {
        return AppFile.getOutputJjccDir() + File.separator + "earliestSeason.txt";
    }
}
