package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 自动提示英文列表.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "NSFC_DISCIPLINE_KEY_ENGLISH")
public class AcDiscKeyEn implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1817329150593992006L;
  private Long id;
  private Long keyId;
  private String disKeyEn;

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "KEY_ID")
  public Long getKeyId() {
    return keyId;
  }

  public void setKeyId(Long keyId) {
    this.keyId = keyId;
  }

  @Column(name = "KEY_ENGLISH")
  public String getDisKeyEn() {
    return disKeyEn;
  }

  public void setDisKeyEn(String disKeyEn) {
    this.disKeyEn = disKeyEn;
  }
}
