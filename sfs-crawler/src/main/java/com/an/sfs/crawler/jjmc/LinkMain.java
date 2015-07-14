package com.an.sfs.crawler.jjmc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.an.sfs.crawler.util.AppFile;

public class LinkMain {
    public static void main(String[] args) {
        // StringBuilder text = new StringBuilder();
        // text.append("<html>");
        // text.append("<body>");
        // String url =
        // "<a href=\"http://hq2data.eastmoney.com/fund/fundlist.aspx?jsName=fundListObj&fund=0&type=6&page=%s\" target=\"_blank\">";
        // for (int i = 1; i < 32; i++) {
        // text.append(String.format(url,
        // i)).append(i).append("</a>").append("\n");
        // }
        // text.append("</body>");
        // text.append("</html>");
        // try {
        // FileWriter fw = new FileWriter(new
        // File(AppFilePath.getInputJjmcDir()) + File.separator + "6.html");
        // fw.write(text.toString());
        // fw.close();
        // } catch (IOException e) {
        // }

        try {
            for (int i = 1; i < 8; i++) {
                FileWriter fw = new FileWriter(
                        new File(AppFile.getInputJjmcDir() + File.separator + "6-" + i + ".html"));
                fw.write("");
                fw.close();
            }
        } catch (IOException e) {
        }
    }
}
