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

@Entity
@Table(name = "PSN_UPDATE_DISC_LOG")
public class PsnUpdateDiscLog implements Serializable {

  /**
   * 新增研究领域日志，用于更新研究领域后，发邮件通知好友
   */
  private static final long serialVersionUID = 9202964004134864762L;
  private Long id;
  private Long psnId;
  private Long discId;
  private Date createDate;
  private Integer status;
  private String keyWords;

  @Id
  @Column(name = "id")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_UPDATE_DISC_LOG", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "psn_id")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "disc_id")
  public Long getDiscId() {
    return discId;
  }

  public void setDiscId(Long discId) {
    this.discId = discId;
  }

  @Column(name = "create_date")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "status")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "keywords")
  public String getKeyWords() {
    return keyWords;
  }

  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

}
