package com.smate.web.management.model.journal;

import java.io.Serializable;
import java.util.List;

/**
 * 论文->期刊推荐-给人员推荐期刊新算法. 用于邮件推荐
 * 
 * @author zk
 * 
 */
public class PsnJnlPsnRecommendList implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4448620926534470705L;

  private List<PsnJnlPsnRecommend> jnlRecomList;

  private Long count;

  public List<PsnJnlPsnRecommend> getJnlRecomList() {
    return jnlRecomList;
  }

  public void setJnlRecomList(List<PsnJnlPsnRecommend> jnlRecomList) {
    this.jnlRecomList = jnlRecomList;
  }

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

}
