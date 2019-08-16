package com.smate.center.open.model.interconnection;

import java.io.Serializable;

/**
 * 互联互通 用于缓存
 * 
 * @author AiJiangBin
 *
 */
public class UnionGroupCodeCache implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4363367326618927703L;
  Long groupId;
  Long openId;

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public Long getOpenId() {
    return openId;
  }

  public void setOpenId(Long openId) {
    this.openId = openId;
  }


}
