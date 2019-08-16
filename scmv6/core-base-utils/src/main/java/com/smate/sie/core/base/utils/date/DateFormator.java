package com.smate.sie.core.base.utils.date;

public enum DateFormator {
  YEAR_MONTH_DAY_HH_MM_SS("yyyy-MM-dd HH:mm:ss"), YEAR_MONTH("yyyy-MM"), SPLIT_CHAR("-"), YEAR_MONTH_DAY(
      "yyyy-MM-dd"), MONTH_DAY("MM-dd"), YEAR_MONTH_EU("yyyy/MM"), MONTH_DAY_YEAR_EU("MM/dd/yyyy"), YEAR_MONTH_DAY_EU(
          "yyyy/MM/dd"), YEAR_MONTH_DAY_H_M_S_EU("yyyy/MM/dd HH:mm:ss"), YMDHMS("yyyy-MM-dd HH:mm:ss"), YMDHMSP(
              "yyyy.MM.dd HH:mm:ss"), YEARMONTHDAYHHMMSS("yyyyMMddHHmmss"), YEARMONTHDAY("yyyyMMdd");
  private String str;

  private DateFormator(String s) {
    this.str = s;
  }

  @Override
  public String toString() {
    return str;
  }

}
