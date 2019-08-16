package com.smate.web.psn.model.profile;



import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 推荐关键词删除记录model
 * 
 * @author zyx
 *
 */
@Entity
@Table(name = "RM_KW_DROP_HISTORY")
public class RecommandKwDropHistory implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -907720879243824346L;
  private Long id;
  private Long psnId;
  private String keyword;
  private String kwTxt;
  private Date dropDate;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_RM_KW_DROP_HISTORY", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "KEYWORD")
  public String getKeyword() {
    return keyword;
  }

  @Column(name = "KW_TXT")
  public String getKwTxt() {
    return kwTxt;
  }

  public void setKwTxt(String kwTxt) {
    this.kwTxt = kwTxt;
  }

  @Column(name = "DROP_DATE")
  public Date getDropDate() {
    return dropDate;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public void setDropDate(Date dropDate) {
    this.dropDate = dropDate;
  }

}
