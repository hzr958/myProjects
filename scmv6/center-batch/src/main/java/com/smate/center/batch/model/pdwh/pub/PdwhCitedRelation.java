package com.smate.center.batch.model.pdwh.pub;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 
 * @author zjh 成果引用关系表
 *
 */
@Entity
@Table(name = "PDWH_CITED_RELATION")
public class PdwhCitedRelation implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7204027451969816116L;

  private Long citedId;// id
  private Long pdwdPubId;// 被引用的成果id
  private Long pdwhCitedPubId;// 引用该成果的成果id

  public PdwhCitedRelation() {
    super();
  }

  public PdwhCitedRelation(Long citedId, Long pdwdPubId, Long pdwhCitedPubId) {
    super();
    this.citedId = citedId;
    this.pdwdPubId = pdwdPubId;
    this.pdwhCitedPubId = pdwhCitedPubId;
  }

  @Id
  @Column(name = "CITED_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_CITED_RELATION", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getCitedId() {
    return citedId;
  }

  public void setCitedId(Long citedId) {
    this.citedId = citedId;
  }

  @Column(name = "PDWH_PUB_ID")
  public Long getPdwdPubId() {
    return pdwdPubId;
  }


  public void setPdwdPubId(Long pdwdPubId) {
    this.pdwdPubId = pdwdPubId;
  }

  @Column(name = "PDWH_CITED_PUB_ID")
  public Long getPdwhCitedPubId() {
    return pdwhCitedPubId;
  }

  public void setPdwhCitedPubId(Long pdwhCitedPubId) {
    this.pdwhCitedPubId = pdwhCitedPubId;
  }

}
