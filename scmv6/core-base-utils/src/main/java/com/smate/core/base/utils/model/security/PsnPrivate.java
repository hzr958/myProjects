package com.smate.core.base.utils.model.security;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统隐私人员
 * 
 * @author zk
 *
 */
@Entity
@Table(name = "PSN_PRIVATE")
public class PsnPrivate implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 571485221173537612L;
  // 人员id
  private Long psnId;
  // 创建时间
  private Date createDate;

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

}
