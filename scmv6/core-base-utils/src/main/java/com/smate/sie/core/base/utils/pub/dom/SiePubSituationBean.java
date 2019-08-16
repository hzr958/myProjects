package com.smate.sie.core.base.utils.pub.dom;

import java.io.Serializable;
import java.util.Objects;

/**
 * 收录情况
 * 
 * @author sjzhou
 *
 */
public class SiePubSituationBean implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7469197329556818191L;
  /**
   * 收录库名
   */
  private String libraryName = new String();
  /**
   * 收录情况
   */
  private boolean sitStatus = false;
  /**
   * 原始收录情况
   */
  private boolean sitOriginStatus = false;

  private String srcUrl = new String(); // 来源URL

  private String srcDbId = new String(); // 来源dbid

  private String srcId = new String(); // 来源唯一标识

  /**
   * 成果被收录的库
   * 
   * @return
   */
  public String getLibraryName() {
    return libraryName;
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
    SiePubSituationBean that = (SiePubSituationBean) o;
    return libraryName == that.libraryName;
  }

  @Override
  public int hashCode() {
    return Objects.hash(libraryName);
  }

  @Override
  public String toString() {
    return "PubSituationBean{" + "libraryName=" + libraryName + ", sitStatus=" + sitStatus + ", sitOriginStatus="
        + sitOriginStatus + ", srcUrl='" + srcUrl + '\'' + ", srcDbId='" + srcDbId + '\'' + ", srcId='" + srcId + '\''
        + '}';
  }

}
