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

        List<String> dirs = new ArrayList<>();
        dirs.add(baseDir);
        dirs.add(getConfDir());
        dirs.add(getInputDir());
        dirs.add(getOutputDir());

        dirs.add(getInputGdyjDir());
        dirs.add(getOutputGdyjDir());

        dirs.add(getInputGdyjGdrsDir());
        dirs.add(getOutputGdyjGdrsDir());

        dirs.add(getInputCwfxYearDir());
        dirs.add(getOutputCwfxYearDir());

        dirs.add(getInputGgdqDir());
        dirs.add(getOutputGgdqDir());

        dirs.add(getInputGgdqPnDir());
        dirs.add(getOutputGgdqPnDir());

        dirs.add(getInputTfpggDir());
        dirs.add(getOutputTfpggDir());

        dirs.add(getInputFhrzDir());
        dirs.add(getInputFhrzRawDir());
        dirs.add(getInputFhrzFhyxDir());
        dirs.add(getInputFhrzZfmxDir());
        dirs.add(getOutputFhrzDir());

        for (String dirName : dirs) {
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

    public static String getOutputDir() {
        return getBaseDir() + File.separator + "output";
    }

    public static String getInputGdyjDir() {
        return getInputDir() + File.separator + "gdyj";
    }

    private static String getOutputGdyjDir() {
        return getOutputDir() + File.separator + "gdyj";
    }

    public static String getInputGdyjGdrsDir() {
        return getInputGdyjDir() + File.separator + "gdrs";
    }

    public static String getOutputGdyjGdrsDir() {
        return getOutputGdyjDir() + File.separator + "gdrs";
    }

    public static String getInputCwfxDir() {
        return getInputDir() + File.separator + "cwfx";
    }

    public static String getOutputCwfxDir() {
        return getOutputDir() + File.separator + "cwfx";
    }

    public static String getInputCwfxYearDir() {
        return getInputDir() + File.separator + "cwfx_year";
    }

    public static String getOutputCwfxYearDir() {
        return getOutputDir() + File.separator + "cwfx_year";
    }

    public static String getInputGgdqDir() {
        return getInputDir() + File.separator + "ggdq";
    }

    public static String getOutputGgdqDir() {
        return getOutputDir() + File.separator + "ggdq";
    }

    public static String getInputGgdqPnDir() {
        return getInputDir() + File.separator + "ggdq_pn";
    }

    public static String getOutputGgdqPnDir() {
        return getOutputDir() + File.separator + "ggdq_pn";
    }

    public static String getInputTfpggDir() {
        return getInputDir() + File.separator + "tfpgg";
    }

    public static String getOutputTfpggDir() {
        return getOutputDir() + File.separator + "tfpgg";
    }

    public static String getInputFhrzDir() {
        return getInputDir() + File.separator + "fhrz";
    }

    public static String getInputFhrzRawDir() {
        return getInputFhrzDir() + File.separator + "raw";
    }

    public static String getInputFhrzFhyxDir() {
        return getInputFhrzDir() + File.separator + "fhyx";
    }

    public static String getInputFhrzZfmxDir() {
        return getInputFhrzDir() + File.separator + "zfmx";
    }

    public static String getOutputFhrzDir() {
        return getOutputDir() + File.separator + "fhrz";
    }

    //
    public static String getConfFile(String fileName) {
        return getConfDir() + File.separator + fileName;
    }
}