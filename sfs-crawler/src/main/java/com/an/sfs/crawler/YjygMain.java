package com.an.sfs.crawler;

import java.io.File;
import java.util.Collections;
import java.util.List;

import com.an.sfs.crawler.yjyg.YjygLoader;
import com.an.sfs.crawler.yjyg.YjygVo;

public class YjygMain {
    public static void main(String[] args) {
        System.out.println("Ye ji yu gao search.");
        AppFilePath.initDirs();
        run();
    }

    public static final boolean COMPARE_BY_DATE = true;

    private static void run() {
        List<YjygVo> yjygList = YjygLoader.getInst().getYjygList();
        List<YjygVo> zList = YjygLoader.getInst().getZengList();
        List<YjygVo> yList = YjygLoader.getInst().getYingList();
        List<YjygVo> jList = YjygLoader.getInst().getJianList();
        List<YjygVo> pList = YjygLoader.getInst().getPingList();
        List<YjygVo> jingList = YjygLoader.getInst().getJingList();
        List<YjygVo> kList = YjygLoader.getInst().getKuiList();

        Collections.sort(yjygList);
        Collections.sort(zList);
        Collections.sort(yList);
        Collections.sort(jList);
        Collections.sort(pList);
        Collections.sort(jingList);
        Collections.sort(kList);

        StringBuilder textYjygRaw = new StringBuilder();
        StringBuilder textYjygCode = new StringBuilder();
        for (YjygVo vo : yjygList) {
            textYjygRaw.append(vo.getDisp()).append("\n");
            textYjygCode.append(vo.getCode()).append("\n");
        }
        FileUtil.writeFile(getOutputDir("0_YJYG_raw.txt"), textYjygRaw.toString());
        FileUtil.writeFile(getOutputDir("0_YJYG_code.txt"), textYjygCode.toString());

        StringBuilder textYzRaw = new StringBuilder();
        StringBuilder textYzCode = new StringBuilder();
        for (YjygVo vo : zList) {
            textYzRaw.append(vo.getDisp()).append("\n");
            textYzCode.append(vo.getCode()).append("\n");
        }
        FileUtil.writeFile(getOutputDir("1_Zeng_raw.txt"), textYzRaw.toString());
        FileUtil.writeFile(getOutputDir("1_Zeng_code.txt"), textYzCode.toString());

        StringBuilder textYyRaw = new StringBuilder();
        StringBuilder textYyCode = new StringBuilder();
        for (YjygVo vo : yList) {
            textYyRaw.append(vo.getDisp()).append("\n");
            textYyCode.append(vo.getCode()).append("\n");
        }
        FileUtil.writeFile(getOutputDir("2_Ying_raw.txt"), textYyRaw.toString());
        FileUtil.writeFile(getOutputDir("2_Ying_code.txt"), textYyCode.toString());

        StringBuilder textYjRaw = new StringBuilder();
        StringBuilder textYjCode = new StringBuilder();
        for (YjygVo vo : jList) {
            textYjRaw.append(vo.getDisp()).append("\n");
            textYjCode.append(vo.getCode()).append("\n");
        }
        FileUtil.writeFile(getOutputDir("3_Jian_raw.txt"), textYjRaw.toString());
        FileUtil.writeFile(getOutputDir("3_Jian_code.txt"), textYjCode.toString());

        StringBuilder textCpRaw = new StringBuilder();
        StringBuilder textCpCode = new StringBuilder();
        for (YjygVo vo : pList) {
            textCpRaw.append(vo.getDisp()).append("\n");
            textCpCode.append(vo.getCode()).append("\n");
        }
        FileUtil.writeFile(getOutputDir("4_Ping_raw.txt"), textCpRaw.toString());
        FileUtil.writeFile(getOutputDir("4_Ping_code.txt"), textCpCode.toString());

        StringBuilder textYjingRaw = new StringBuilder();
        StringBuilder textYjingCode = new StringBuilder();
        for (YjygVo vo : jingList) {
            textYjingRaw.append(vo.getDisp()).append("\n");
            textYjingCode.append(vo.getCode()).append("\n");
        }
        FileUtil.writeFile(getOutputDir("5_Jing_raw.txt"), textYjingRaw.toString());
        FileUtil.writeFile(getOutputDir("5_Jing_code.txt"), textYjingCode.toString());

        StringBuilder textYkRaw = new StringBuilder();
        StringBuilder textYkCode = new StringBuilder();
        for (YjygVo vo : kList) {
            textYkRaw.append(vo.getDisp()).append("\n");
            textYkCode.append(vo.getCode()).append("\n");
        }
        FileUtil.writeFile(getOutputDir("6_Kui_raw.txt"), textYkRaw.toString());
        FileUtil.writeFile(getOutputDir("6_Kui_code.txt"), textYkCode.toString());
    }

    private static String getOutputDir(String fileName) {
        return AppFilePath.getOutputYjygDir() + File.separator + fileName;
    }
}
