package com.smate.web.v8pub.po.pdwh;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 存储基准库的分类成果
 * 
 * 
 */
@Entity
@Table(name = "PUB_CATEGORY")
public class PubCategory implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 4051227452290298232L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CONST_FUND_AGENCY", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long Id;

  @Column(name = "PUB_ID")
  private Long pubId;

  @Column(name = "SCM_CATEGORY_ID")
  private Long scmCategoryId;

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getScmCategoryId() {
    return scmCategoryId;
  }

  public void setScmCategoryId(Long scmCategoryId) {
    this.scmCategoryId = scmCategoryId;
  }



}
