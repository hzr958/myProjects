package com.smate.web.v8pub.po.sns;

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
 * 论文推荐操作记录
 * 
 * @author yhx
 *
 */
@Entity
@Table(name = "V_PUB_RECOMMEND_RECORD")
public class PubRecommendRecordPO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "V_SEQ_PUB_RECOMMEND_RECORD", sequenceName = "V_SEQ_PUB_RECOMMEND_RECORD",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "V_SEQ_PUB_RECOMMEND_RECORD")
  @Column(name = "ID")
  private Long id; // 主键

  @Column(name = "PSN_ID")
  private Long psnId; // 人员id

  @Column(name = "PUB_ID")
  private Long pubId; // 成果id

  @Column(name = "STATUS")
  private Integer status; // 状态：0正常，1不感兴趣

  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 创建时间

  @Column(name = "GMT_MODIFIED")
  private Date gmtModified; // 被更新时间

  public PubRecommendRecordPO() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

}
