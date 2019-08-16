package com.smate.center.task.model.rol.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 用户确认过的成果的关键词或用户在研究领域中添加的关键词.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_PM_KEYWORD")
public class PsnPmKeyWord implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1208708392057516234L;

  private Long id;
  // 关键词(小写)
  private String keyword;
  // 关键词hash
  private Integer kwhash;
  // 用户ID
  private Long psnId;
  // 用户确认成果的关键词次数
  private Integer kcount = 1;

  public PsnPmKeyWord() {
    super();
  }

  public PsnPmKeyWord(String keyword, Integer kwhash, Long psnId) {
    super();
    this.keyword = keyword;
    this.kwhash = kwhash;
    this.psnId = psnId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PM_KEYWORD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "KEYWORD")
  public String getKeyword() {
    return keyword;
  }

  @Column(name = "KW_HASH")
  public Integer getKwhash() {
    return kwhash;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "KCOUNT")
  public Integer getKcount() {
    return kcount;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setKwhash(Integer kwhash) {
    this.kwhash = kwhash;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setKcount(Integer kcount) {
    this.kcount = kcount;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((kcount == null) ? 0 : kcount.hashCode());
    result = prime * result + ((kwhash == null) ? 0 : kwhash.hashCode());
    result = prime * result + ((psnId == null) ? 0 : psnId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PsnPmKeyWord other = (PsnPmKeyWord) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (kcount == null) {
      if (other.kcount != null)
        return false;
    } else if (!kcount.equals(other.kcount))
      return false;
    if (kwhash == null) {
      if (other.kwhash != null)
        return false;
    } else if (!kwhash.equals(other.kwhash))
      return false;
    if (psnId == null) {
      if (other.psnId != null)
        return false;
    } else if (!psnId.equals(other.psnId))
      return false;
    return true;
  }
}
