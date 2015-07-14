package com.an.sfs.crawler;

import java.util.ArrayList;
import java.util.List;

public class SfsConf {
    // CWFX start date
    public static final String CWFX_UP_START_DATE = "2012-01-01";
    // Last year
    public static final String LAST_YEAR = "2014-12-31";
    // Last Season
    public static final String LAST_SEASON = "2014-12-31";
    // Current Season
    public static final String CURRENT_SEASON = "2015-03-31";
    // Current YJYG season
    public static final String YJYG_SEASON = "2015-06-30";
    // GDRS start season
    public static final String GDRS_START_SEASON = "2014-06-30";
    // YJYG compare by date
    public static final boolean YJYG_COMPARE_BY_DATE = true;
    // GDRS sort by count diff value
    public static final boolean GDRS_SORT_BY_COUNT_DIFF_VALUE = true;

    private static List<String> seasonList = new ArrayList<>();
    static {
        seasonList.add("2015-03-31");

        seasonList.add("2014-12-31");
        seasonList.add("2014-09-30");
        seasonList.add("2014-06-30");
        seasonList.add("2014-03-31");

        seasonList.add("2013-12-31");
        seasonList.add("2013-09-30");
        seasonList.add("2013-06-30");
        seasonList.add("2013-03-31");

        seasonList.add("2012-12-31");
        seasonList.add("2012-09-30");
        seasonList.add("2012-06-30");
        seasonList.add("2012-03-31");

        seasonList.add("2011-12-31");
        seasonList.add("2011-09-30");
        seasonList.add("2011-06-30");
        seasonList.add("2011-03-31");

        seasonList.add("2010-12-31");
        seasonList.add("2010-09-30");
        seasonList.add("2010-06-30");
        seasonList.add("2010-03-31");

        seasonList.add("2009-12-31");
        seasonList.add("2009-09-30");
        seasonList.add("2009-06-30");
        seasonList.add("2009-03-31");

        seasonList.add("2008-12-31");
        seasonList.add("2008-09-30");
        seasonList.add("2008-06-30");
        seasonList.add("2008-03-31");

        seasonList.add("2007-12-31");
        seasonList.add("2007-09-30");
        seasonList.add("2007-06-30");
        seasonList.add("2007-03-31");

        seasonList.add("2006-12-31");
        seasonList.add("2006-09-30");
        seasonList.add("2006-06-30");
        seasonList.add("2006-03-31");

        seasonList.add("2005-12-31");
        seasonList.add("2005-09-30");
        seasonList.add("2005-06-30");
        seasonList.add("2005-03-31");
    }

    public static List<String> getSeasonList() {
        return seasonList;
    }

    public static boolean isValidSeason(String date) {
        return seasonList.contains(date);
    }
}
