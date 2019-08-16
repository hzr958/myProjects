package com.smate.center.batch.model.tmp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * isi成果导出表，关键词，abstract
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "TMP_PUB_ID")
public class TmpPubSimpleId implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = 5905799250663707077L;
  @Id
  @Column(name = "PUB_ID")
  private Long pubId;

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

}
