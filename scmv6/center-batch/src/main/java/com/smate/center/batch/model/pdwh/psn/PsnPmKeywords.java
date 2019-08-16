package com.smate.center.batch.model.pdwh.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 用户使用过的关键词信息表.
 * 
 * @author mjg
 * 
 */
@Entity
@Table(name = "PSN_PM_KEYWORDS")
public class PsnPmKeywords implements Serializable {

  private static final long serialVersionUID = -7501402226509566813L;
  private Long id;// 个人主键.
  private Long psnId;// 人员ID.
  private String keyword;// 用户使用过的关键词(小写).
  private Long kwHash;// 关键词Hash.
  private Integer pubKw = 0;// 成果关键词.
  private Integer prjKw = 0;// 项目关键词.
  private Integer ztKw = 0;//

  public PsnPmKeywords() {
    super();
  }

  public PsnPmKeywords(Long id, Long psnId, String keyword, Long kwHash, Integer pubKw, Integer prjKw, Integer ztKw) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.keyword = keyword;
    this.kwHash = kwHash;
    this.pubKw = pubKw;
    this.prjKw = prjKw;
    this.ztKw = ztKw;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_PM_KEYWORDS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "KEYWORD")
  public String getKeyword() {
    return keyword;
  }

  @Column(name = "KW_HASH")
  public Long getKwHash() {
    return kwHash;
  }

  @Column(name = "PUB_KW")
  public Integer getPubKw() {
    return pubKw;
  }

  @Column(name = "PRJ_KW")
  public Integer getPrjKw() {
    return prjKw;
  }

  @Column(name = "ZT_KW")
  public Integer getZtKw() {
    return ztKw;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setKwHash(Long kwHash) {
    this.kwHash = kwHash;
  }

  public void setPubKw(Integer pubKw) {
    this.pubKw = pubKw;
  }

  public void setPrjKw(Integer prjKw) {
    this.prjKw = prjKw;
  }

  public void setZtKw(Integer ztKw) {
    this.ztKw = ztKw;
  }

}
