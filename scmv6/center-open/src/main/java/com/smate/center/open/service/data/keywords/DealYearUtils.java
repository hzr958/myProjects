package com.smate.center.open.service.data.keywords;

public final class DealYearUtils {

  public static String dealYears(Integer startYear, Integer endYear) {
    StringBuffer years = new StringBuffer();
    years.append("(");
    years.append(startYear);
    for (int i = 1; i <= endYear - startYear; i++) {
      years.append(" ");
      years.append(startYear + i);
    }
    years.append(")");
    return years.toString();
  }

}
