package com.smate.web.psn.model.influence;

import java.io.Serializable;

/**
 * 人员访问统计
 * 
 * @author wsn
 * @date 2018年6月11日
 */
public class PsnVisitLineInfo implements Serializable, Comparable<PsnVisitLineInfo> {

  private static final long serialVersionUID = 7949882947571283553L;

  private String countDate; // 统计的时间点
  private Long visitSum; // 访问总数

  public PsnVisitLineInfo() {
    super();
  }

  public PsnVisitLineInfo(String countDate, Long visitSum) {
    super();
    this.countDate = countDate;
    this.visitSum = visitSum;
  }

  public String getCountDate() {
    return countDate;
  }

  public void setCountDate(String countDate) {
    this.countDate = countDate;
  }

  public Long getVisitSum() {
    return visitSum;
  }

  public void setVisitSum(Long visitSum) {
    this.visitSum = visitSum;
  }

  @Override
  public int compareTo(PsnVisitLineInfo o) {
    String date = o.getCountDate();
    return date.compareTo(this.countDate);
  }

}
