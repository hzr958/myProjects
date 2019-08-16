package com.smate.web.dyn.model.grp;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 群组统计信息表
 * 
 * @author AiJiangBin
 *
 */
@Entity
@Table(name = "V_GRP_STATISTICS")
public class GrpStatistics implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5082414779050510980L;

  @Id
  @Column(name = "GRP_ID")
  private Long grpId; // 群组 id

  @Column(name = "SUM_MEMBER")
  private Integer sumMember = 0; // 群组成员统计

  @Column(name = "SUM_PUBS")
  private Integer sumPubs = 0; // 群组成果统计

  @Column(name = "SUM_FILE")
  private Integer sumFile = 0; // 群组文件统计

  @Column(name = "VISIT_COUNT")
  private Integer visitCount = 0; // 群组访问统计

  @Column(name = "SUM_TO_MEMBER")
  private Integer sumToMember = 0; // 带确认的成员数

  public GrpStatistics() {}

  public GrpStatistics(Integer sumMember, Integer sumPubs) {
    this.sumMember = sumMember;
    this.sumPubs = sumPubs;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public Integer getSumMember() {
    return sumMember;
  }

  public void setSumMember(Integer sumMember) {
    this.sumMember = sumMember;
  }

  public Integer getSumPubs() {
    return sumPubs;
  }

  public void setSumPubs(Integer sumPubs) {
    this.sumPubs = sumPubs;
  }

  public Integer getSumFile() {
    return sumFile;
  }

  public void setSumFile(Integer sumFile) {
    this.sumFile = sumFile;
  }

  public Integer getVisitCount() {
    return visitCount;
  }

  public void setVisitCount(Integer visitCount) {
    this.visitCount = visitCount;
  }

  public Integer getSumToMember() {
    return sumToMember;
  }

  public void setSumToMember(Integer sumToMember) {
    this.sumToMember = sumToMember;
  }

}
