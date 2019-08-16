package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 单位信息临时表
 * 
 * @author hd
 *
 */
@Embeddable
public class ImportInsDataInfoId implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = -7192751667005448831L;
  // 单位编码
  private Long orgCode;
  // 数据来源
  private String token;

  public ImportInsDataInfoId() {}

  public ImportInsDataInfoId(Long orgCode, String token) {
    this.orgCode = orgCode;
    this.token = token;
  }

  @Column(name = "ORG_CODE")
  public Long getOrgCode() {
    return orgCode;
  }

  public void setOrgCode(Long orgCode) {
    this.orgCode = orgCode;
  }

  @Column(name = "TOKEN")
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (orgCode ^ (orgCode >>> 32));
    result = prime * result + ((token == null) ? 0 : token.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other)
      return true;
    if (other == null)
      return false;
    if (getClass() != other.getClass())
      return false;
    if (!(other instanceof ImportInsDataInfoId))
      return false;
    ImportInsDataInfoId castOther = (ImportInsDataInfoId) other;

    return (this.getOrgCode() == castOther.getOrgCode() && this.getToken() == castOther.getToken());
  }



}
