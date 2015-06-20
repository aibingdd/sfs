package com.an.sfs.crawler.gsgk;

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

public class GsgkFetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(GsgkFetcher.class);
    // http://f10.eastmoney.com/f10_v2/CompanySurvey.aspx?code=sz300436
    private static final String URL = "http://f10.eastmoney.com/f10_v2/CompanySurvey.aspx?code=%s%s";

    private static final String FLAG_JBZL = "<strong>基本资料</strong>";
    private static final String FLAG_FXXG = "<strong>发行相关</strong>";

    private static final String FLAG_CREATE = "成立日期";
    private static final String FLAG_PUBLIC = "上市日期";
    private static final String FLAG_INDUSTRY = "所属行业";

    public void run() {
        LOGGER.info("Fetch ...");
        fetch(URL, AppFilePath.getInputGsgkRawDir());

        LOGGER.info("Analyze ...");
        analyze();
    }

    private void fetch(String url, String fileDir) {
        List<StockVo> stocks = StockLoader.getInst().getStocks();
        for (StockVo vo : stocks) {
            String stock = vo.getCode();
            String httpUrl = String.format(url, vo.getTypeStr(), stock);
            String fp = fileDir + File.separator + stock + ".html";
            if (!FileUtil.isFileExist(fp)) {
                AppUtil.download(httpUrl, fp);
            }
            extract(stock, fp);
        }
    }

    private void extract(String stock, String filePath) {
        StringBuilder data = new StringBuilder();
        boolean jbzlStart = false;
        boolean jbzlFinish = false;
        boolean fxxgStart = false;
        boolean fxxgFinish = false;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!jbzlStart && line.contains(FLAG_JBZL)) {
                    jbzlStart = true;
                    continue;
                }
                if (!jbzlFinish && jbzlStart && line.contains("<table")) {
                    String text = line.trim();
                    int endIndex = text.indexOf("公司简介");
                    if (endIndex != -1) {
                        text.substring(0, endIndex);
                    }
                    text = text.replace("><", ">\n<");
                    data.append(text).append("\n");
                    jbzlFinish = true;
                    continue;
                }

                if (!fxxgStart && line.contains(FLAG_FXXG)) {
                    fxxgStart = true;
                    continue;
                }
                if (!fxxgFinish && fxxgStart && line.contains("<table")) {
                    String text = line.trim();
                    text = text.replace("><", ">\n<");
                    data.append(text);
                    fxxgFinish = true;
                    continue;
                }

                if (fxxgFinish && jbzlFinish) {
                    break;
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error ", e);
        }
        String txtFp = AppFilePath.getInputGsgkTxtDir() + File.separator + stock + ".txt";
        FileUtil.writeFile(txtFp, data.toString());
    }

    private void analyze() {
        StringBuilder text = new StringBuilder();

        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFilePath.getInputGsgkTxtDir(), files);
        for (File f : files) {
            String stock = FileUtil.getFileName(f.getPath());
            text.append(stock).append(",");

            boolean startCreate = false;
            boolean finishCreate = false;
            boolean startPublic = false;
            boolean finishPublic = false;
            boolean startIndustry = false;
            boolean finishIndustry = false;
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (!startIndustry && line.contains(FLAG_INDUSTRY)) {
                        startIndustry = true;
                        continue;
                    }
                    if (!finishIndustry && startIndustry) {
                        String date = FileUtil.extractVal(line);
                        text.append(date).append(",");
                        finishIndustry = true;
                    }

                    if (!startCreate && line.contains(FLAG_CREATE)) {
                        startCreate = true;
                        continue;
                    }
                    if (!finishCreate && startCreate) {
                        String date = FileUtil.extractVal(line);
                        text.append(date).append(",");
                        finishCreate = true;
                    }

                    if (!startPublic && line.contains(FLAG_PUBLIC)) {
                        startPublic = true;
                        continue;
                    }
                    if (!finishPublic && startPublic) {
                        String date = FileUtil.extractVal(line);
                        text.append(date).append("\n");
                        finishPublic = true;
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }

            FileUtil.writeFile(getGsgkFile(), text.toString());
        }
    }

    public static String getGsgkFile() {
        return AppFilePath.getOutputGsgkDir() + File.separator + "gsgk.txt";
    }
}
