package com.smate.web.v8pub.po.journal;

import org.apache.commons.lang.StringUtils;

public enum BaseJnlCategoryRankEnum {
  A(1), B(2), C(3), D(4), Q1(5), Q2(6), Q3(7), Q4(8), Q5(9), Q6(10), Q7(11), Q8(12), Q9(13);

  private Integer value;

  private BaseJnlCategoryRankEnum(Integer value) {
    this.value = value;
  }

  @Override
  public String toString() {

    return String.valueOf(this.value);

  }

  public static Integer getRankValue(String name) {
    Integer rank = 19;
    if (StringUtils.isEmpty(name)) {
      return rank;
    }

    for (BaseJnlCategoryRankEnum en : BaseJnlCategoryRankEnum.values()) {
      if (name.equalsIgnoreCase(en.name())) {
        rank = en.value;
      }
    }
    return rank;
  }
}
