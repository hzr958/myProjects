package com.smate.center.merge.model.sns.pub;

import javax.persistence.*;
import java.util.Date;

/**
 * 个人成果关系表.
 * 
 * @author yhx
 * @date 2018年5月31日
 */
@Entity
@Table(name = "V_PSN_PUB")
public class PsnPub {
  @Id
  @SequenceGenerator(name = "SEQ_PSN_PUB_ID", sequenceName = "SEQ_PSN_PUB_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PSN_PUB_ID")
  @Column(name = "ID")
  private Long id; // 主键id
  @Column(name = "PUB_ID")
  private Long pubId; // 成果编号
  @Column(name = "OWNER_PSN_ID")
  private Long ownerPsnId;// 拥有者
  @Column(name = "STATUS", columnDefinition = "INT default 0", nullable = false)
  private Integer status;// 状态 0: 未删除，1: 已删除
  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 创建日期
  @Column(name = "GMT_MODIFIED")
  private Date gmtModified; // 更新时间

  public PsnPub() {
    super();
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getOwnerPsnId() {
    return ownerPsnId;
  }

  public void setOwnerPsnId(Long ownerPsnId) {
    this.ownerPsnId = ownerPsnId;
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
