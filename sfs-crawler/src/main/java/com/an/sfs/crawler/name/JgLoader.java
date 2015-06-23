package com.an.sfs.crawler.name;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.crawler.AppFilePath;
import com.an.sfs.crawler.AppUtil;
import com.an.sfs.crawler.FileUtil;

public class JgLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(JgLoader.class);
    private List<JgVo> jgList = new ArrayList<>();
    private Map<String, JgVo> jgMap = new HashMap<>();

    private static final String JG_FILE = AppFilePath.getOutputDir() + File.separator + "JG.txt";

    public List<JgVo> getJgList() {
        return jgList;
    }

    /**
     * @param code
     * @return
     */
    public JgVo getJg(String code) {
        if (!jgMap.containsKey(code)) {
            LOGGER.warn("Not found Ji Gou {}", code);
            return null;
        }
        return jgMap.get(code);
    }

    private void init() {
        if (!FileUtil.isFileExist(JG_FILE)) {
            analyze();
        }
        load();
    }

    private void analyze() {
        List<File> files = new ArrayList<>();
        FileUtil.getFilesUnderDir(AppFilePath.getInputCcjgTxtDir(AppUtil.CUR_SEASON), files);

        StringBuilder text = new StringBuilder();
        for (File f : files) {
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        String[] strs = line.split(",");
                        String code = strs[2];
                        String name = strs[3];
                        String typeStr = strs[4];
                        int type = JgVo.getType(typeStr);
                        text.append(code).append(",");
                        text.append(type).append(",");
                        text.append(name).append("\n");
                        if (name.isEmpty()) {
                            System.out.println(line);
                        }
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Error ", e);
            }
        }
        FileUtil.writeFile(JG_FILE, text.toString());
    }

    private void load() {
        try (BufferedReader br = new BufferedReader(new FileReader(JG_FILE))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] strs = line.split(",");
                    if (strs.length == 3) {
                        String code = strs[0];
                        int type = Integer.parseInt(strs[1]);
                        String name = strs[2];
                        JgVo vo = new JgVo(code, type, name);
                        jgList.add(vo);
                        jgMap.put(code, vo);
                    } else {
                        System.out.println(line);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error ", e);
        }
    }

    //
    public static JgLoader getInst() {
        if (inst == null) {
            inst = new JgLoader();
            inst.init();
        }
        return inst;
    }

    private static JgLoader inst = null;

    private JgLoader() {
    }

    public static void main(String[] args) {
        JgLoader.getInst();
    }
}
