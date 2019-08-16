package com.smate.web.management.model.institution.sns;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.smate.core.base.utils.model.security.PsnInsPk;


/**
 * SNS端的 个人与单位的关系.
 * 
 * @author LY
 * 
 */
@Entity
@Table(name = "PSN_INS")
public class PsnInsSns implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1391166624886416725L;
  private PsnInsPk pk;
  private Long notInJob;
  private Integer allowSubmitPub;
  // 人员状态，0申请加入、1已加入、2单位拒绝、9删除
  private Integer status = 1;

  public PsnInsSns() {
    super();
  }

  public PsnInsSns(Long psnId, Long insId, Long notInJob, Integer allowSubmitPub) {
    super();
    this.pk = new PsnInsPk(psnId, insId);
    this.notInJob = notInJob;
    this.allowSubmitPub = allowSubmitPub;
  }

  /**
   * @return pk
   */
  @EmbeddedId
  public PsnInsPk getPk() {
    return pk;
  }

  /**
   * @param pk
   */
  public void setPk(PsnInsPk pk) {
    this.pk = pk;
  }

  @Column(name = "ALLOW_SUBMIT_PUB")
  public Integer getAllowSubmitPub() {
    return allowSubmitPub;
  }

  public void setAllowSubmitPub(Integer allowSubmitPub) {
    this.allowSubmitPub = allowSubmitPub;
  }

  @Column(name = "NOT_IN_JOB")
  public Long getNotInJob() {
    return notInJob;
  }

  public void setNotInJob(Long notInJob) {
    this.notInJob = notInJob;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
