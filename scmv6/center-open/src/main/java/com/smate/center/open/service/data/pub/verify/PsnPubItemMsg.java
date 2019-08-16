package com.smate.center.open.service.data.pub.verify;

/**
 * @author aijiangbin
 * @create 2018-11-20 14:39
 **/
public class PsnPubItemMsg {
  /**
   * 1 通过 2 不通过 3 不确定
   */
  public String title = "";
  public String authorNames = "";

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }
}
