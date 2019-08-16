package com.smate.center.task.model.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 数据来源表主键
 * 
 * @author hd
 *
 */
@Embeddable
public class InsDataFromId implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2680766389352567420L;


  private Long orgCode;
  private String token;

  public InsDataFromId() {}

  public InsDataFromId(Long orgCode, String token) {
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
    if (!(other instanceof InsDataFromId))
      return false;
    InsDataFromId castOther = (InsDataFromId) other;

    return (this.getOrgCode() == castOther.getOrgCode() && this.getToken() == castOther.getToken());
  }


}
