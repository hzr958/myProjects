package com.smate.center.task.model.search;

import java.io.Serializable;
import java.util.List;

import com.smate.center.task.model.pub.seo.PubIndexSecondLevel;


/**
 * 成果检索form.
 * 
 * @author zyx
 * 
 */
public class PubSearchForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7119036242712672745L;
  public static final Integer MAX_LOAD_SIZE = 20; // 每次加载的数量
  public static final Integer MAX_SIZE = 1000;// 总共可以显示的数量
  private Integer totalSize = 0;

  private List<PubIndexSecondLevel> pubList;
  private Integer start = 0;
  private String pubName;
  private Boolean more;

  public List<PubIndexSecondLevel> getPubList() {
    return pubList;
  }

  public Integer getStart() {
    if (start > MAX_SIZE) {
      start = MAX_SIZE;
    }
    return start;
  }

  public String getPubName() {
    return pubName;
  }

  public void setPubList(List<PubIndexSecondLevel> pubList) {
    this.pubList = pubList;
  }

  public void setStart(Integer start) {
    this.start = start;
  }

  public void setPubName(String pubName) {
    this.pubName = pubName;
  }

  public Boolean getMore() {
    if (start < MAX_SIZE && pubList != null && pubList.size() > 0) {
      more = true;
    } else {
      more = false;
    }
    return more;
  }

  public Integer getTotalSize() {
    return totalSize;
  }

  public void setTotalSize(Integer totalSize) {
    this.totalSize = totalSize;
  }
}
