package com.an.sfs.crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppFilePath {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppFilePath.class);

    public static void initDirs() {
        String baseDir = getBaseDir();
        if (baseDir == null || baseDir.isEmpty()) {
            LOGGER.error("Empty base directory, please set SFS_HOME.");
            System.exit(1);
        }

        List<String> dirNames = new ArrayList<>();
        dirNames.add(baseDir);
        dirNames.add(getConfDir());
        dirNames.add(getInputDir());
        dirNames.add(getInputGdyjDir());
        dirNames.add(getInputCwfxDir());
        dirNames.add(getOutputDir());
        dirNames.add(getOutputGdyjGdrsDir());
        dirNames.add(getOutputGdyjLtgdDir());
        dirNames.add(getOutputCwfxDir());
        dirNames.add(getOutputCwfxYearDir());
        for (String dirName : dirNames) {
            File dir = new File(dirName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }

    private static String getBaseDir() {
        return System.getenv("SFS_HOME");
    }

    public static String getConfDir() {
        return getBaseDir() + File.separator + "conf";
    }

    private static String getInputDir() {
        return getBaseDir() + File.separator + "input";
    }

    public static String getInputGdyjDir() {
        return getInputDir() + File.separator + "gdyj";
    }

    public static String getInputCwfxDir() {
        return getInputDir() + File.separator + "cwfx";
    }

    public static String getInputCwfxYearDir() {
        return getInputDir() + File.separator + "cwfx_year";
    }

    public static String getOutputDir() {
        return getBaseDir() + File.separator + "output";
    }

    private static String getOutputGdyjDir() {
        return getOutputDir() + File.separator + "gdyj";
    }

    public static String getOutputGdyjGdrsDir() {
        return getOutputGdyjDir() + File.separator + "gdrs";
    }

    public static String getOutputGdyjLtgdDir() {
        return getOutputGdyjDir() + File.separator + "ltgf";
    }

    public static String getOutputCwfxDir() {
        return getOutputDir() + File.separator + "cwfx";
    }

    public static String getOutputCwfxYearDir() {
        return getOutputDir() + File.separator + "cwfx_year";
    }

    //
    public static String getConfFile(String fileName) {
        return getConfDir() + File.separator + fileName;
    }
}