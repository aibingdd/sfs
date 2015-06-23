package com.an.sfs.crawler.jjmc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JgTypeLoader {
    // ShiDaLiuTongGuDong JiGou Name
    private static Set<String> jgNameSet = new HashSet<>();
    static {
        jgNameSet.add("证券公司");
        jgNameSet.add("证券投资基金");
        jgNameSet.add("全国社保基金");
        jgNameSet.add("保险公司");
        jgNameSet.add("信托计划");
    }

    /**
     * @param jgName
     * @return
     */
    public static boolean isJg(String jgName) {
        return jgNameSet.contains(jgName);
    }

    private static List<JgType> jjTypes = new ArrayList<>();
    static {
        jjTypes.add(new JgType(0, "全部", 135));
        jjTypes.add(new JgType(1, "股票型", 42));
        jjTypes.add(new JgType(2, "混合型", 31));
        jjTypes.add(new JgType(3, "债券型", 30));
        jjTypes.add(new JgType(4, "LOF", 7));
        jjTypes.add(new JgType(5, "ETF", 6));
        jjTypes.add(new JgType(6, "QDII", 7));
    }

    public static List<JgType> getJjTypes() {
        return jjTypes.subList(1, jjTypes.size());
    }
}
