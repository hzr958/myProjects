package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果和单位（INSTITUTION）的关系表.
 * 
 * @author liqinghua
 *
 */
@Entity
@Table(name = "PUB_INS")
public class PubIns implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8273139936803475741L;

  // 主键
  private PubInsId id;
  // 添加日期
  private Long psnId;
  // 成果状态（冗余，删除状态不更新）
  private Integer pubStatus;
  // 成果所有人ID
  private Date createAt;


  @Id
  @AttributeOverrides({@AttributeOverride(name = "pubId", column = @Column(name = "PUB_ID")),
      @AttributeOverride(name = "insId", column = @Column(name = "INS_ID"))})
  public PubInsId getId() {
    return id;
  }

  public void setId(PubInsId id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "CREATE_AT")
  public Date getCreateAt() {
    return createAt;
  }

  public void setCreateAt(Date createAt) {
    this.createAt = createAt;
  }

  @Column(name = "PUB_STATUS")
  public Integer getPubStatus() {
    return pubStatus;
  }

  public void setPubStatus(Integer pubStatus) {
    this.pubStatus = pubStatus;
  }



}
