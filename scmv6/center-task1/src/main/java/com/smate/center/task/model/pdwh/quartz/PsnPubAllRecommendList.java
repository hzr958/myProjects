package com.smate.center.task.model.pdwh.quartz;

import java.io.Serializable;
import java.util.List;

/**
 * 人员期刊推荐新算法，推荐出来的文献记录,邮件推荐用
 * 
 * @author zk
 * 
 */
public class PsnPubAllRecommendList implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 9205563394733168418L;

  private List<PsnPubAllRecommend> pubAllRmdList;

  private Long count;

  public List<PsnPubAllRecommend> getPubAllRmdList() {
    return pubAllRmdList;
  }

  public void setPubAllRmdList(List<PsnPubAllRecommend> pubAllRmdList) {
    this.pubAllRmdList = pubAllRmdList;
  }

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

}
