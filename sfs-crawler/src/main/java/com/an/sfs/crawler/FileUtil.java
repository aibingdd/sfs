package com.an.sfs.crawler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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
            LOGGER.error("Error while save content to file {}", filePath, e);
        }
    }

    public static void main(String[] args) {
        FileUtil.writeFile("D:\\test.txt", "a\nb\nc");
    }
}
