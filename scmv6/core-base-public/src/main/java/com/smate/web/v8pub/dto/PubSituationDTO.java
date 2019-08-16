package com.smate.web.v8pub.dto;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 成果收录位置情况
 * 
 * @author houchuanjie
 * @date 2018/05/30 17:33
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PubSituationDTO implements Serializable {

  private static final long serialVersionUID = 2821743287118989079L;

  private Long pubId; // 成果id，主键
  /**
   * 收录库名
   */
  private String libraryName;

  public String showName = "";

  /**
   * 收录情况
   */
  private boolean sitStatus;
  /**
   * 原始收录情况
   */
  private boolean sitOriginStatus;

  private String srcUrl; // 来源URL

  private String srcDbId; // 来源dbid

  private String srcId; // 来源唯一标识

  /**
   * 成果被收录的库
   * 
   * @return
   */
  public String getLibraryName() {
    return libraryName.toUpperCase();
  }

  public PubSituationDTO() {
    super();
  }

  public PubSituationDTO(String libraryName, String srcDbId, String showName) {
    super();
    this.libraryName = libraryName;
    this.srcDbId = srcDbId;
    this.showName = showName;
  }

  public PubSituationDTO(String libraryName) {
    super();
    this.libraryName = libraryName;
  }

  public void setLibraryName(String libraryName) {
    this.libraryName = libraryName;
  }

  /**
   * 是否收录
   * 
   * @return
   */
  public boolean isSitStatus() {
    return sitStatus;
  }

  public void setSitStatus(boolean sitStatus) {
    this.sitStatus = sitStatus;
  }

  /**
   * 是否被原始收录
   * 
   * @return
   */
  public boolean isSitOriginStatus() {
    return sitOriginStatus;
  }

  public void setSitOriginStatus(boolean sitOriginStatus) {
    this.sitOriginStatus = sitOriginStatus;
  }

  /**
   * 来源url
   * 
   * @return
   */
  public String getSrcUrl() {
    return srcUrl;
  }

  public void setSrcUrl(String srcUrl) {
    this.srcUrl = srcUrl;
  }

  /**
   * 来源唯一标识
   * 
   * @return
   */
  public String getSrcId() {
    return srcId;
  }

  public void setSrcId(String srcId) {
    this.srcId = srcId;
  }

  public String getSrcDbId() {
    return srcDbId;
  }

  public void setSrcDbId(String srcDbId) {
    this.srcDbId = srcDbId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PubSituationDTO that = (PubSituationDTO) o;
    return libraryName == that.libraryName;
  }

  @Override
  public int hashCode() {
    return Objects.hash(libraryName);
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getShowName() {
    return showName;
  }

  public void setShowName(String showName) {
    this.showName = showName;
  }

  @Override
  public String toString() {
    return "PubSituationDTO{ pubId=" + pubId + ",libraryName=" + libraryName + ", sitStatus=" + sitStatus
        + ", sitOriginStatus=" + sitOriginStatus + ", srcUrl='" + srcUrl + '\'' + ", srcDbId='" + srcDbId + '\''
        + ", srcId='" + srcId + '\'' + '}';
  }
}
