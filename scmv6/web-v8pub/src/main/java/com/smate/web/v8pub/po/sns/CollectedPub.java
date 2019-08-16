package com.smate.web.v8pub.po.sns;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.smate.core.base.pub.enums.PubDbEnum;

/**
 * 论文收藏表
 * 
 * @author WCW
 *
 */
@Entity
@Table(name = "V_COLLECTED_PUB")
public class CollectedPub implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -9017258080177357767L;

  private Long id;// 主键id
  private Long psnId;// 人员id
  private Long pubId;// 成果id
  private PubDbEnum pubDb;// 成果所属的库，0-基准库，1-个人库
  private Date createDate;// 收藏成果的时间

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_COLLECTED_PUB", sequenceName = "SEQ_V_COLLECTED_PUB", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COLLECTED_PUB")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "PUB_DB")
  @Enumerated(EnumType.ORDINAL)
  public PubDbEnum getPubDb() {
    return pubDb;
  }

  public void setPubDb(PubDbEnum pubDb) {
    this.pubDb = pubDb;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((psnId == null) ? 0 : psnId.hashCode());
    result = prime * result + ((pubId == null) ? 0 : pubId.hashCode());
    result = prime * result + ((pubDb == null) ? 0 : pubDb.hashCode());
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
    CollectedPub other = (CollectedPub) obj;
    if (psnId == null) {
      if (other.psnId != null)
        return false;
    } else if (!psnId.equals(other.psnId))
      return false;
    if (pubId == null) {
      if (other.pubId != null)
        return false;
    } else if (!pubId.equals(other.pubId))
      return false;
    if (pubDb == null) {
      if (other.pubDb != null)
        return false;
    } else if (!pubDb.equals(other.pubDb))
      return false;
    return true;
  }
}
