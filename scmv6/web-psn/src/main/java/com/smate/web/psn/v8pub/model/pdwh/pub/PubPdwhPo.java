package com.smate.web.psn.v8pub.model.pdwh.pub;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author houchuanjie
 * @date 2018/06/01 16:47
 */
@Entity
@Table(name = "V_PUB_PDWH")
public class PubPdwhPo extends PubPo {

  /**
   * 
   */
  private static final long serialVersionUID = -2554792987772270894L;
  /**
   * 成果id
   */
  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_PDWH_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "PUB_ID")
  protected Long pubId;

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PubPdwhPo)) {
      return false;
    }
    PubPdwhPo that = (PubPdwhPo) o;
    return pubId.equals(that.pubId);
  }

  @Override
  public int hashCode() {
    return 17 * 1 + Objects.hashCode(pubId);
  }

  @Override
  public String toString() {
    return "PubPdwhPO{" + "pubId=" + pubId + ", publishYear=" + publishYear + ", countryId=" + countryId + ", title='"
        + title + '\'' + ", authorNames='" + authorNames + '\'' + ", briefDesc='" + briefDesc + '\'' + ", createPsnId="
        + createPsnId + ", gmtCreate=" + gmtCreate + ", gmtModified=" + gmtModified + '}';
  }
}
