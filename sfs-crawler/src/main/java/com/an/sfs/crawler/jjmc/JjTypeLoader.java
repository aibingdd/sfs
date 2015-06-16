package com.an.sfs.crawler.jjmc;

import java.util.ArrayList;
import java.util.List;

public class JjTypeLoader {
    private static List<JjType> jjTypes = new ArrayList<>();
    static {
        jjTypes.add(new JjType(0, "全部", 135));
        jjTypes.add(new JjType(1, "股票型", 42));
        jjTypes.add(new JjType(2, "混合型", 31));
        jjTypes.add(new JjType(3, "债券型", 30));
        jjTypes.add(new JjType(4, "LOF", 7));
        jjTypes.add(new JjType(5, "ETF", 6));
        jjTypes.add(new JjType(6, "QDII", 7));
    }

    public static List<JjType> getJjTypes() {
        return jjTypes.subList(1, jjTypes.size());
    }

    public static void main(String[] args) {
        System.out.println(JjTypeLoader.getJjTypes());
    }
}
