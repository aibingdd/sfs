package com.an.sfs.crawler.tfp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.util.AppFile;
import com.an.sfs.crawler.util.AppUtil;
import com.an.sfs.crawler.util.FileUtil;

public class TfpFetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(TfpFetcher.class);
    // index.html or index_1.html
    private static final String BASEDIR = "http://www.p5w.net/stock/news/zonghe";
    private static final String TFP = "http://www.p5w.net/stock/jinghua/tfp/index%s.htm";

    private Set<String> tfpUrls = new HashSet<>();
    private static final String FLAG_TFP = "<h2 class=\"title\"><a href=\"../../news/zonghe";
    private static final String TFP_URL_FILE = "TfpUrl.txt";
    public static final String TFP_FILE = "TFP.txt";

    public void run() {
        fetchTfpUrls(TFP, AppFile.getInputTfpggDir());
    }

    private void fetchTfpUrls(String url, String fileDir) {
        // Load existing TFP url
        String tfpUrlFile = AppFile.getOutputTfpggDir() + File.separator + TFP_URL_FILE;
        if (FileUtil.isFileExist(tfpUrlFile)) {
            try (BufferedReader br = new BufferedReader(new FileReader(tfpUrlFile))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        tfpUrls.add(line.trim());
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }
        }

        // Download TFGGG web page
        for (int i = 0; i < 20; i++) {
            String tmp = "";
            if (i != 0) {
                tmp = "_" + i;
            }
            String httpUrl = String.format(url, tmp);
            String filePath = fileDir + File.separator + "tfp_" + i + ".html";
            AppUtil.download(httpUrl, filePath);

            // Analyze
            boolean finished = false;
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    int startIndex = line.indexOf(FLAG_TFP);
                    if (startIndex != -1) {
                        startIndex = startIndex + FLAG_TFP.length();
                        int endIndex = line.indexOf("\"", startIndex);
                        String urlStr = line.substring(startIndex, endIndex);
                        urlStr = BASEDIR + urlStr;
                        if (tfpUrls.contains(urlStr)) {
                            finished = true;
                            break;
                        }
                        tfpUrls.add(urlStr);
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }

            if (finished) {
                break;
            }
        }

        // Save
        List<String> tfpUrlList = new ArrayList<>();
        tfpUrlList.addAll(tfpUrls);
        Collections.sort(tfpUrlList);
        StringBuilder txt = new StringBuilder();
        for (String httpUrl : tfpUrlList) {
            txt.append(httpUrl).append("\n");
        }
        FileUtil.writeFile(tfpUrlFile, txt.toString());

        // Download TFPGG web page
        for (String httpUrl : tfpUrlList) {
            String fileNameFull = FileUtil.getHttpUrlFileNameFull(httpUrl);
            String filePath = AppFile.getInputTfpggDir() + File.separator + fileNameFull;

            if (!FileUtil.isFileExist(filePath)) {
                AppUtil.download(httpUrl, filePath);
            }
        }

        // Analyze
        StringBuilder allText = new StringBuilder();
        List<File> outFileList = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFile.getInputTfpggDir(), null, ".htm", outFileList);
        for (File f : outFileList) {
            String filePath = f.getPath();
            String fileName = FileUtil.getFileName(filePath);
            List<String> valList = new ArrayList<>();
            try (InputStreamReader is = new InputStreamReader(new FileInputStream(filePath), "GB2312");
                    BufferedReader br = new BufferedReader(is)) {
                String line = null;
                int index = 0;
                while ((line = br.readLine()) != null) {
                    index = process_1(index, line, valList);
                }
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }

            if (valList.isEmpty()) {
                try (InputStreamReader is = new InputStreamReader(new FileInputStream(filePath), "GB2312");
                        BufferedReader br = new BufferedReader(is)) {
                    String line = null;
                    int index = 0;
                    while ((line = br.readLine()) != null) {
                        index = process_2(index, line, valList);
                    }
                } catch (IOException e) {
                    LOGGER.error("Error ", e);
                }
            }

            // Save
            StringBuilder tfpText = new StringBuilder();
            for (int i = 0; i < valList.size(); i++) {
                tfpText.append(valList.get(i));
                if ((i + 1) % 6 == 0) {
                    tfpText.append("\n");
                } else {
                    tfpText.append(";");
                }
            }

            String fp = AppFile.getOutputTfpggDir() + File.separator + fileName + ".txt";
            FileUtil.writeFile(fp, tfpText.toString());
            // Save to one file
            allText.append(fileName).append("\n");
            allText.append(tfpText.toString());
        }

        FileUtil.writeFile(AppFile.getOutputDir() + File.separator + TFP_FILE, allText.toString());
    }

    private static final String VAL_PATTERN_1 = ".*\">(.*)</td>.*";

    private int process_1(int index, String line, List<String> outValList) {
        int beginIndex1 = line.indexOf("<td style=\"text-align");
        int beginIndex2 = line.indexOf("<td style=\"list-style-type");
        if (beginIndex1 != -1 || beginIndex2 != -1) {
            if (index == 0) {
                return ++index;
            }
            Pattern p = Pattern.compile(VAL_PATTERN_1);
            Matcher m = p.matcher(line);
            if (m.find()) {
                String val = m.group(1).trim();
                if (val.equals("&nbsp;")) {
                    val = "";
                }
                outValList.add(val);
            }
            return ++index;
        }
        return index;
    }

    private static final String VAL_PATTERN_2 = ".*\">(.*)</span>.*";

    private int process_2(int index, String line, List<String> outValList) {
        int endIndex = line.indexOf("</span></p>");
        if (endIndex != -1) {
            if (index < 6) {
                return ++index;
            }
            Pattern p = Pattern.compile(VAL_PATTERN_2);
            Matcher m = p.matcher(line);
            if (m.find()) {
                String val = m.group(1).trim();
                if (val.equals("&nbsp;")) {
                    val = "";
                }
                outValList.add(val);
            }
            return ++index;
        }
        return index;
    }
}
