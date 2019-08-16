package com.smate.web.group.model.group.homepage;

import java.io.Serializable;
import java.util.List;

/**
 * 群组主页设置项JSON实体.
 * 
 * @author liqinghua
 * 
 */
public class GhpConfigItem implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3760171972513520016L;
  /**
   * 
   */
  private Integer isShow = 0;
  private Integer isAll = 0;
  private List<GhpConfigElement> configElements;
  private Integer seqNum = 1;

  public GhpConfigItem() {
    super();
  }

  public GhpConfigItem(Integer isShow) {
    super();
    this.isShow = isShow;
  }

  public GhpConfigItem(Integer isShow, Integer isAll, Integer seqNum) {
    super();
    this.isShow = isShow;
    this.isAll = isAll;
    this.seqNum = seqNum;
  }

  public GhpConfigItem(Integer isShow, Integer isAll) {
    super();
    this.isShow = isShow;
    this.isAll = isAll;
  }

  public GhpConfigItem(Integer isShow, Integer isAll, List<GhpConfigElement> configElements, Integer seqNum) {
    super();
    this.isShow = isShow;
    this.isAll = isAll;
    this.configElements = configElements;
    this.seqNum = seqNum;
  }

  public Integer getIsShow() {
    return isShow;
  }

  public void setIsShow(Integer isShow) {
    this.isShow = isShow;
  }

  public Integer getIsAll() {
    return isAll;
  }

  public void setIsAll(Integer isAll) {
    this.isAll = isAll;
  }

  public List<GhpConfigElement> getConfigElements() {
    return configElements;
  }

  public void setConfigElements(List<GhpConfigElement> resumeElements) {
    this.configElements = resumeElements;
  }

  public Integer getSeqNum() {
    return seqNum;
  }

  public void setSeqNum(Integer seqNum) {
    this.seqNum = seqNum;
  }

}
