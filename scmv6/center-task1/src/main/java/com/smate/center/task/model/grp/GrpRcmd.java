package com.smate.center.task.model.grp;

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
 * 群组推荐表
 * 
 * @author AiJiangBin
 *
 */

@Entity
@Table(name = "V_GRP_RCMD")
public class GrpRcmd implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8716235840209547285L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_SEQ_GRP_RCMD_STORE", sequenceName = "SEQ_V_GRP_RCMD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SEQ_GRP_RCMD_STORE")
  private Long id; // 主键id

  @Column(name = "PSN_ID")
  private Long psnId; // 人员id

  @Column(name = "GRP_ID")
  private Long grpId; // 群组id

  @Column(name = "RCMD_DATE")
  private Date rcmdDate; // 群组推荐时间

  @Column(name = "STATUS")
  private String status; // 0：推荐状态 ， 9忽略状态 ， 1加入状态

  @Column(name = "UPDATE_DATE")
  private Date updateDate; // 处理时间

  @Column(name = "RCMD_SCORE")
  private Integer rcmdScore; // 推荐度

  public GrpRcmd() {
    super();
  }

  public GrpRcmd(Long psnId, Long grpId, Date rcmdDate, Integer rcmdScore) {
    super();
    this.psnId = psnId;
    this.grpId = grpId;
    this.rcmdDate = rcmdDate;
    this.rcmdScore = rcmdScore;
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

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public Date getRcmdDate() {
    return rcmdDate;
  }

  public void setRcmdDate(Date rcmdDate) {
    this.rcmdDate = rcmdDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Integer getRcmdScore() {
    return rcmdScore;
  }

  public void setRcmdScore(Integer rcmdScore) {
    this.rcmdScore = rcmdScore;
  }

}
