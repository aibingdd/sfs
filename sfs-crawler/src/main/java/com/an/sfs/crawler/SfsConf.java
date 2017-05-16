package com.an.sfs.crawler;

import java.util.ArrayList;
import java.util.List;

public class SfsConf {
    public static final String CWFX_UP_START_DATE = "2014-01-01";
    public static final String LAST_YEAR = "2016-12-31";
    public static final String LAST_SEASON = "2016-12-31";
    public static final String CURRENT_SEASON = "2017-03-31";
    public static final String YJYG_SEASON = "2017-06-30";
    public static final String GDRS_START_SEASON = "2016-06-30";
    public static final int MIN_SEASON_COUNT = 4;
    public static final int MAX_GDRS_NUMBER = 20000;
    public static final int MIN_FLOAT_SHARE = 4000;
    public static final boolean YJYG_COMPARE_BY_DATE = true;

    private static List<String> seasonList = new ArrayList<>();
    static {
        seasonList.add("2017-03-31");

        seasonList.add("2016-12-31");
        seasonList.add("2016-09-30");
        seasonList.add("2016-06-30");
        seasonList.add("2016-03-31");

        seasonList.add("2015-12-31");
        seasonList.add("2015-09-30");
        seasonList.add("2015-06-30");
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
