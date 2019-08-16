package com.smate.sie.web.application.form.analysis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 开题分析表单参数
 * 
 * @author sjzhou
 *
 */
public class ProblemAnalysisForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2147529698235230050L;

  private String title;
  private String summary;
  private String keyword;
  private List<Map<String, Object>> keyWordsList; // 抽取出来的关键词
  private List<Map<String, Object>> researchersList; // 科研人员

  public String getTitle() {
    return title;
  }

  public String getSummary() {
    return summary;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public List<Map<String, Object>> getResearchersList() {
    return researchersList;
  }

  public void setResearchersList(List<Map<String, Object>> researchersList) {
    this.researchersList = researchersList;
  }

  public List<Map<String, Object>> getKeyWordsList() {
    return keyWordsList;
  }

  public void setKeyWordsList(List<Map<String, Object>> keyWordsList) {
    this.keyWordsList = keyWordsList;
  }

}
