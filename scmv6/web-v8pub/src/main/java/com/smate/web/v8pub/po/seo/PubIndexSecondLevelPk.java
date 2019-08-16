package com.smate.web.v8pub.po.seo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PubIndexSecondLevelPk implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8203888533532176372L;
  // 成果id
  private Long pubId;

  public PubIndexSecondLevelPk() {}

  public PubIndexSecondLevelPk(Long pubId) {
    this.pubId = pubId;
  }


  @Column(name = "pub_id")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }



  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((pubId == null) ? 0 : pubId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof PubIndexSecondLevelPk)) {
      return false;
    }
    PubIndexSecondLevelPk other = (PubIndexSecondLevelPk) obj;
    if (pubId == null) {
      if (other.pubId != null) {
        return false;
      }
    } else if (!pubId.equals(other.pubId)) {
      return false;
    }
    return true;
  }

}
