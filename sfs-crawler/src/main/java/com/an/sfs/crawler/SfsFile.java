package com.an.sfs.crawler;

import java.io.File;

public class SfsFile implements Comparable<SfsFile> {
    private File file;

    public SfsFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "SfsFile [file=" + file + "]";
    }

    @Override
    public int compareTo(SfsFile o) {
        String tfn = FileUtil.getFileName(file.getPath());
        String ofn = FileUtil.getFileName(o.file.getPath());
        int tfni = Integer.parseInt(tfn);
        int ofni = Integer.parseInt(ofn);
        if (tfni > ofni) {
            return 1;
        } else if (tfni < ofni) {
            return -1;
        } else {
            return 0;
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
