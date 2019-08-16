/**
 * 
 */
package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.List;

/**
 * 用户设置类型
 * 
 * @author oyh
 * 
 */

public class UserSettings implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5420428356252373311L;
  /**
   * 
   */
  private List<ConstDictionary> pravicyList;
  private String initPrivateJson;
  private List<ConstDictionary> attList;
  private String initAttTypeJson;
  private Integer dynEmail;
  private String privacyConfig;
  private String addJson;
  private String delJson;
  private Integer begin;
  private Integer fetchSize;
  private Long totalCount;
  // 取消关注人主键
  private Long cancelId;
  // ljj 搜索的关键字
  private String queryKwcmd;
  private String des3PsnId;// 被取消关注人员id

  /**
   * @return the pravicyList
   */
  public List<ConstDictionary> getPravicyList() {
    return pravicyList;
  }

  /**
   * @param pravicyList the pravicyList to set
   */
  public void setPravicyList(List<ConstDictionary> pravicyList) {
    this.pravicyList = pravicyList;
  }

  /**
   * @return the initPrivateJson
   */
  public String getInitPrivateJson() {
    return initPrivateJson;
  }

  /**
   * @param initPrivateJson the initPrivateJson to set
   */
  public void setInitPrivateJson(String initPrivateJson) {
    this.initPrivateJson = initPrivateJson;
  }

  /**
   * @return the attList
   */
  public List<ConstDictionary> getAttList() {
    return attList;

  }

  /**
   * @param attList the attList to set
   */
  public void setAttList(List<ConstDictionary> attList) {
    this.attList = attList;
  }

  /**
   * @return the initAttTypeJson
   */
  public String getInitAttTypeJson() {
    return initAttTypeJson;
  }

  /**
   * @param initAttTypeJson the initAttTypeJson to set
   */
  public void setInitAttTypeJson(String initAttTypeJson) {
    this.initAttTypeJson = initAttTypeJson;
  }

  /**
   * @return the privacyConfig
   */
  public String getPrivacyConfig() {
    return privacyConfig;
  }

  /**
   * @param privacyConfig the privacyConfig to set
   */
  public void setPrivacyConfig(String privacyConfig) {
    this.privacyConfig = privacyConfig;
  }

  /**
   * @return the addJson
   */
  public String getAddJson() {
    return addJson;
  }

  /**
   * @param addJson the addJson to set
   */
  public void setAddJson(String addJson) {
    this.addJson = addJson;
  }

  /**
   * @return the delJson
   */
  public String getDelJson() {
    return delJson;
  }

  /**
   * @param delJson the delJson to set
   */
  public void setDelJson(String delJson) {
    this.delJson = delJson;
  }

  /**
   * @return the dynEmail
   */
  public Integer getDynEmail() {
    return dynEmail;
  }

  /**
   * @param dynEmail the dynEmail to set
   */
  public void setDynEmail(Integer dynEmail) {
    this.dynEmail = dynEmail;
  }

  /**
   * @return the begin
   */
  public Integer getBegin() {
    return begin;
  }

  /**
   * @param begin the begin to set
   */
  public void setBegin(Integer begin) {
    this.begin = begin;
  }

  /**
   * @return the fetchSize
   */
  public Integer getFetchSize() {
    return fetchSize;
  }

  /**
   * @param fetchSize the fetchSize to set
   */
  public void setFetchSize(Integer fetchSize) {
    this.fetchSize = fetchSize;
  }

  /**
   * @return the cancelId
   */
  public Long getCancelId() {
    return cancelId;
  }

  /**
   * @param cancelId the cancelId to set
   */
  public void setCancelId(Long cancelId) {
    this.cancelId = cancelId;
  }

  /**
   * @return the totalCount
   */
  public Long getTotalCount() {
    return totalCount;
  }

  /**
   * @param totalCount the totalCount to set
   */
  public void setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
  }

  public String getQueryKwcmd() {
    return queryKwcmd;
  }

  public void setQueryKwcmd(String queryKwcmd) {
    this.queryKwcmd = queryKwcmd;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

}
