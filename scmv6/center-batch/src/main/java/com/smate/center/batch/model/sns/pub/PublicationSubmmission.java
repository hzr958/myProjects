package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果提交实体.
 * 
 * @author LY
 * 
 */
@Entity
@Table(name = "PUB_SUBMISSION")
public class PublicationSubmmission implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1383644922950230062L;
  private Long id;
  private Long insId;
  private Long pubId;
  private Long psnId;
  private Integer state;
  private Integer versionNo;
  private Date lastSync;
  private Date createAt;
  private Publication publication;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_SUBMISSION", allocationSize = 1)
  @GeneratedValue(generator = "SEQ_STORE", strategy = GenerationType.SEQUENCE)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  // 多对一定义，cascade操作避免定义CascadeType.REMOVE
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
  // 关联定义
  @JoinColumn(name = "PUB_ID", referencedColumnName = "PUB_ID", insertable = false, updatable = false)
  @OrderBy("id")
  // 集合中对象id的缓存. insert="false" update="false"
  public Publication getPublication() {
    return publication;
  }

  public void setPublication(Publication publication) {
    this.publication = publication;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "STATE")
  public Integer getState() {
    return state;
  }

  @Column(name = "VERSION_NO")
  public Integer getVersionNo() {
    return versionNo;
  }

  @Column(name = "LAST_SYNC")
  public Date getLastSync() {
    return lastSync;
  }

  @Column(name = "CREATE_AT")
  public Date getCreateAt() {
    return createAt;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public void setVersionNo(Integer versionNo) {
    this.versionNo = versionNo;
  }

  public void setLastSync(Date lastSync) {
    this.lastSync = lastSync;
  }

  public void setCreateAt(Date createAt) {
    this.createAt = createAt;
  }

}
