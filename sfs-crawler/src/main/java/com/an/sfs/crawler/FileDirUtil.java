package com.an.sfs.crawler;

import java.io.File;

public class FileDirUtil {
    static {
        File d = new File(getBaseDir());
        if (!d.exists()) {
            d.mkdirs();
        }
        d = new File(getWebPageDir());
        if (!d.exists()) {
            d.mkdirs();
        }
        d = new File(getGdyjDir());
        if (!d.exists()) {
            d.mkdirs();
        }
        d = new File(getConfDir());
        if (!d.exists()) {
            d.mkdirs();
        }
    }

    public static String getBaseDir() {
        return System.getenv("SFS_HOME");
    }

    public static String getWebPageDir() {
        return getBaseDir() + File.separator + "webpage";
    }

    public static String getGdyjDir() {
        return getWebPageDir() + File.separator + "gdyj";
    }

    public static String getConfDir() {
        return getBaseDir() + File.separator + "conf";
    }

    public static String getShEbk() {
        return getConfDir() + File.separator + "SH.EBK";
    }

    public static String getSzEbk() {
        return getConfDir() + File.separator + "SZ.EBK";
    }
}
