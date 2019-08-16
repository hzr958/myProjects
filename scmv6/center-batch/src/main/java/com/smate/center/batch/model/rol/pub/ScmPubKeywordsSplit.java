package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SCM_PUB_KEYWORDS_SPLIT.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "SCM_PUB_KEYWORDS_SPLIT")
public class ScmPubKeywordsSplit implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3143335951558527400L;

  private Long id;
  private String keyWord;
  private Integer kwHash;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  @Column(name = "KEYWORD")
  public String getKeyWord() {
    return keyWord;
  }

  @Column(name = "KW_HASH")
  public Integer getKwHash() {
    return kwHash;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setKeyWord(String keyWord) {
    this.keyWord = keyWord;
  }

  public void setKwHash(Integer kwHash) {
    this.kwHash = kwHash;
  }

}
