package com.smate.center.task.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SCM_USER_CLASSIFICATION")
public class ScmUserForClassification implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -1979725287354552037L;

  @Id
  @Column(name = "PSN_ID")
  private Long psnId;

  @Column(name = "INFO_SOURCE")
  private Integer infoSource; // 1.依靠nsfc分类信息分类。2依靠scm系统成果信息分类

  @Column(name = "STATUS")
  private Integer status;

  public ScmUserForClassification() {
    super();
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getInfoSource() {
    return infoSource;
  }

  public void setInfoSource(Integer infoSource) {
    this.infoSource = infoSource;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
