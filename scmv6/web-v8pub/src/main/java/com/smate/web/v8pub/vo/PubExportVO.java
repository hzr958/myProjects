package com.smate.web.v8pub.vo;

import java.util.List;

import com.smate.core.base.pub.vo.PubInfo;

/**
 * 成果导出
 *
 */
public class PubExportVO {

  private String pubIds;// 成果ids
  private String exportType;// 导出类型 "txt":TXT文件
  private String exportScope;// 导出范围 "common":常用字段/共用字段(标题,作者,来源) "all":全部字段
  // 被导出东西的类型 1:publication<成果> 2:reference<文献> 3:file<文件> 4:project<项目>
  private Integer articleType;
  private List<PubInfo> pubInfoList; // 成果列表

  public String getPubIds() {
    return pubIds;
  }

  public void setPubIds(String pubIds) {
    this.pubIds = pubIds;
  }

  public String getExportType() {
    return exportType;
  }

  public void setExportType(String exportType) {
    this.exportType = exportType;
  }

  public String getExportScope() {
    return exportScope;
  }

  public void setExportScope(String exportScope) {
    this.exportScope = exportScope;
  }

  public Integer getArticleType() {
    return articleType;
  }

  public void setArticleType(Integer articleType) {
    this.articleType = articleType;
  }

  public List<PubInfo> getPubInfoList() {
    return pubInfoList;
  }

  public void setPubInfoList(List<PubInfo> pubInfoList) {
    this.pubInfoList = pubInfoList;
  }

}
