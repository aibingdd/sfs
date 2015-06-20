package com.an.sfs.crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * Save content to file.
     * 
     * @param filePath
     *            saved file path.
     * @param text
     *            file content.
     */
    public static void writeFile(String filePath, String text) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(filePath))) {
            out.write(text);
        } catch (IOException e) {
            LOGGER.error("Error, filePath {}", filePath, e);
        }
    }

    /**
     * @param filePath
     * @param text
     * @param append
     */
    public static void writeFile(String filePath, String text, boolean append) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(filePath, append))) {
            out.write(text);
        } catch (IOException e) {
            LOGGER.error("Error, filePath {}", filePath, e);
        }
    }

    /**
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * @param dirPath
     * @param outFileList
     */
    public static void getFilesUnderDir(String dirPath, List<File> outFileList) {
        getFilesUnderDir(dirPath, null, null, outFileList);
    }

    /**
     * @param dirPath
     * @param type
     * @param outFileList
     */
    public static void getFilesUnderDir(String dirPath, String start, String end, List<File> outFileList) {
        File dir = new File(dirPath);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File f : files) {
                if (f.isFile()) {
                    String filePath = f.getPath();
                    String fileNameName = FileUtil.getFileNameFull(filePath);
                    if (start != null && !fileNameName.startsWith(start)) {
                        continue;
                    }
                    if (end != null && !fileNameName.endsWith(end)) {
                        continue;
                    }
                    outFileList.add(f);
                }
            }
        }
    }

    /**
     * @param filePath
     * @return File name without suffix.
     */
    public static String getFileName(String filePath) {
        int beginIndex = filePath.lastIndexOf(File.separator);
        String fileName = filePath.substring(beginIndex + 1, filePath.indexOf("."));
        return fileName;
    }

    /**
     * @param filePath
     * @return File name with suffix.
     */
    public static String getFileNameFull(String filePath) {
        int beginIndex = filePath.lastIndexOf(File.separator);
        String fileName = filePath.substring(beginIndex + 1);
        return fileName;
    }

    public static String getHttpUrlFileName(String httpUrl) {
        int beginIndex = httpUrl.lastIndexOf("/");
        String fileName = httpUrl.substring(beginIndex + 1, httpUrl.lastIndexOf("."));
        return fileName;
    }

    public static String getHttpUrlFileNameFull(String httpUrl) {
        int beginIndex = httpUrl.lastIndexOf("/");
        String fileName = httpUrl.substring(beginIndex + 1);
        return fileName;
    }

    /**
     * Format html file
     * 
     * @param filePath
     */
    public static void formatHtmlFile(String filePath) {
        StringBuilder text = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("><", ">\n<");
                text.append(line).append("\n");
            }
        } catch (IOException e) {
            LOGGER.error("Error ", e);
        }
        FileUtil.writeFile(filePath, text.toString());
    }

    /**
     * @param outputDir
     * @param filePath
     * @param encoding
     */
    public static void formatHtmlFile(String outputDir, String outputFileType, String filePath, String encoding) {
        StringBuilder text = new StringBuilder();
        BufferedReader br = null;
        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            Reader isr = new InputStreamReader(fis, encoding);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("  ", "");
                line = line.replaceAll("><", ">\n<");
                text.append(line).append("\n");
            }
        } catch (IOException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        String fn = FileUtil.getFileName(filePath);
        FileUtil.writeFile(outputDir + File.separator + fn + outputFileType, text.toString());
    }

    /**
     * @param line
     *            like <th class="tips-dataR">2015-05-13</th>
     * @return
     */
    public static String extractVal(String line) {
        int startIndex = line.indexOf(">");
        int endIndex = line.indexOf("<", startIndex);
        if (startIndex != -1 && endIndex != -1) {
            String val = line.substring(startIndex + 1, endIndex);
            return val;
        }
        return null;
    }
}
