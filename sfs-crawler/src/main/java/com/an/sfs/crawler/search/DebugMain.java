package com.an.sfs.crawler.search;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.FileUtil;

public class DebugMain {
    public static void main(String[] args) {
        FailedTfpggMain();
    }

    private static void FailedTfpggMain() {
        List<File> inputTfpFileList = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFilePath.getInputTfpggDir(), null, ".htm", inputTfpFileList);
        System.out.println("inputTfpFileList " + inputTfpFileList.size());

        List<File> outputTfpFileList = new ArrayList<File>();
        FileUtil.getFilesUnderDir(AppFilePath.getOutputTfpggDir(), "t2", null, outputTfpFileList);
        System.out.println("outputTfpFileList " + outputTfpFileList.size());

        Set<String> outputSet = new HashSet<>();
        for (File f : outputTfpFileList) {
            String fileName = FileUtil.getFileName(f.getPath());
            outputSet.add(fileName);
        }

        System.out.println("Failed TFPGG files:");
        for (File f : inputTfpFileList) {
            String fileName = FileUtil.getFileName(f.getPath());
            if (!outputSet.contains(fileName)) {
                System.out.println(fileName);
            }
        }
    }
}