package com.smate.web.mobile.group.vo;

import java.io.Serializable;

/**
 * @description 保存选择后的好友
 * @author xiexing
 * @date 2019年5月17日
 */
public class PsnInfoShow implements Serializable {

  /**
   * 序列号
   */
  private static final long serialVersionUID = 4954956410061906943L;

  /**
   * 人员加密id
   */
  private String desPsnId;

  /**
   * 人员名称
   */
  private String name;

  public String getDesPsnId() {
    return desPsnId;
  }

  public void setDesPsnId(String desPsnId) {
    this.desPsnId = desPsnId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


}
