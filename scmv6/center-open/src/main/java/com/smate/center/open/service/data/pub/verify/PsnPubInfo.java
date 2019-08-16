package com.smate.center.open.service.data.pub.verify;

/**
 * @author aijiangbin
 * @create 2018-11-12 16:29
 **/
public class PsnPubInfo {
  public String title = "";// 成果标题
  public String authorNames = "";// 成果作者 ，英文分号分隔

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
