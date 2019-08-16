package com.smate.web.psn.model.resume;

import java.util.List;

/**
 * 工作经历与教育经历信息 --导出简历用
 * 
 * @author lhd
 *
 */

public class WorkEduData {
  private String title;// 标题
  private List<String> items;// 经历

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<String> getItems() {
    return items;
  }

  public void setItems(List<String> items) {
    this.items = items;
  }

}
