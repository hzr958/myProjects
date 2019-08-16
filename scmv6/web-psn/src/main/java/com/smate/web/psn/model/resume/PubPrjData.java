package com.smate.web.psn.model.resume;

import java.util.List;
import java.util.Map;

/**
 * 成果项目信息--导出简历用
 * 
 * @author lhd
 *
 */
public class PubPrjData {

  private String title;// 标题
  private List<Map<String, Object>> objectContent;// 拼接的信息

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<Map<String, Object>> getObjectContent() {
    return objectContent;
  }

  public void setObjectContent(List<Map<String, Object>> objectContent) {
    this.objectContent = objectContent;
  }

}
