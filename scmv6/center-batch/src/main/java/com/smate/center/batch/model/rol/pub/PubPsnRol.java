package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.smate.center.batch.model.rol.psn.RolPsnIns;

/**
 * 成果和人的关系表(指派).
 * 
 * @author yamingd
 * 
 */
@Entity
@Table(name = "PUB_PSN")
public class PubPsnRol implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  // 主键
  private Long id;
  // 指派的成果ID
  private Long pubId;
  // 成果所属单位ID
  private Long insId;
  // 冗余person的unit_id,(在指派、保存成果、批准成果时冗余，在人员管理修改部门时同步)
  private Long unitId;
  // 冗余psn_ins的unit_id的父部门ID,(在指派、保存成果、批准成果时冗余，在人员管理修改部门时同步)
  private Long parentUnitId;
  // 名称
  private String psnName;
  // 人员EMAIL
  private String psnEmail;
  // 指派到的研究人员ID
  private Long psnId;
  // 研究人成果的确认状态：0未确认/1已确认/2已拒绝
  private Integer confirmResult;
  // 确认日期
  private Date confirmDate;
  // 单位发送认领email次数
  private Integer sendEmail;
  // 指派方式：0手动/1后台自动/2RO批准通过时建立/3录入成果时指派
  private Integer assignMode;
  // 指派的作者标识，对应到pub_member.pm_id
  private Long authorPMId;
  // 通过psnId找到的人信息
  private RolPsnIns personRol;
  // 分数
  private Float score;
  // 是否发送MQ到人员同步，1为已发送，0为未发送
  private Integer isSend = 0;
  // 指派成果后，个人端认领成果，记录个人端的成果ID
  private Long psnPubId;
  // 对应pubMember中的id.
  private Long pmId;
  // 对应pubMember中的序号.
  private Integer seqNo;
  // 判断人员是否登录，只有登录过的用户才发送到科研之友确认，要求与PSN_PUB_SEND_FLAG保持一致
  private Integer psnLogin = 0;

  public PubPsnRol() {
    super();
  }

  public PubPsnRol(Long pubId, Long insId, Long psnId, Integer confirmResult, Integer assignMode, Long authorPMId) {
    super();
    this.sendEmail = 0;
    this.pubId = pubId;
    this.insId = insId;
    this.psnId = psnId;
    this.confirmResult = confirmResult;
    this.assignMode = assignMode;
    this.authorPMId = authorPMId;
    this.isSend = 0;
    this.psnLogin = 0;
  }

  /**
   * @return the id
   */
  @Id
  @Column(name = "ASSIGN_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_PSN", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the pubId
   */
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  /**
   * @param pubId the pubId to set
   */
  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  /**
   * @return the insId
   */
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  /**
   * @param insId the insId to set
   */
  public void setInsId(Long insId) {
    this.insId = insId;
  }

  /**
   * @return the unitId
   */
  @Column(name = "UNIT_ID")
  public Long getUnitId() {
    return unitId;
  }

  /**
   * @param unitId the unitId to set
   */
  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  /**
   * @return the psnId
   */
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  /**
   * @param psnId the psnId to set
   */
  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  /**
   * @return the parentUnitId
   */
  @Column(name = "PARENT_UNIT_ID")
  public Long getParentUnitId() {
    return parentUnitId;
  }

  /**
   * @param parentUnitId the parentUnitId to set
   */
  public void setParentUnitId(Long parentUnitId) {
    this.parentUnitId = parentUnitId;
  }

  /**
   * @return the confirmResult
   */
  @Column(name = "CONFIRM_RESULT")
  public Integer getConfirmResult() {
    return confirmResult;
  }

  /**
   * @param confirmResult the confirmResult to set
   */
  public void setConfirmResult(Integer confirmResult) {
    this.confirmResult = confirmResult;
  }

  /**
   * @return the confirmDate
   */
  @Column(name = "CONFIRM_DATE")
  public Date getConfirmDate() {
    return confirmDate;
  }

  /**
   * @param confirmDate the confirmDate to set
   */
  public void setConfirmDate(Date confirmDate) {
    this.confirmDate = confirmDate;
  }

  /**
   * @return the sendEmail
   */
  @Column(name = "SEND_EMAIL")
  public Integer getSendEmail() {
    return sendEmail;
  }

  /**
   * @param sendEmail the sendEmail to set
   */
  public void setSendEmail(Integer sendEmail) {
    this.sendEmail = sendEmail;
  }

  /**
   * @return the assignMode
   */
  @Column(name = "ASSIGN_MODE")
  public Integer getAssignMode() {
    return assignMode;
  }

  /**
   * @param assignMode the assignMode to set
   */
  public void setAssignMode(Integer assignMode) {
    this.assignMode = assignMode;
  }

  /**
   * @return the authorPMId
   */
  @Column(name = "AUTHOR_PM_ID")
  public Long getAuthorPMId() {
    return authorPMId;
  }

  /**
   * @param authorPMId the authorPMId to set
   */
  public void setAuthorPMId(Long authorPMId) {
    this.authorPMId = authorPMId;
  }

  @Column(name = "SCORE")
  public Float getScore() {
    return score;
  }

  public void setScore(Float score) {
    this.score = score;
  }

  @Column(name = "PSN_LOGIN")
  public Integer getPsnLogin() {
    return psnLogin;
  }

  public void setPsnLogin(Integer psnLogin) {
    this.psnLogin = psnLogin;
  }

  /**
   * @return the personRol
   */
  @Transient
  public RolPsnIns getPersonRol() {
    return personRol;
  }

  /**
   * @param personRol the personRol to set
   */
  public void setPersonRol(RolPsnIns personRol) {
    this.personRol = personRol;
  }

  @Transient
  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  @Transient
  public String getPsnEmail() {
    return psnEmail;
  }

  public void setPsnEmail(String psnEmail) {
    this.psnEmail = psnEmail;
  }

  @Column(name = "IS_SEND")
  public Integer getIsSend() {
    return isSend;
  }


  public void setIsSend(Integer isSend) {
    this.isSend = isSend;
  }

  @Column(name = "PSN_PUB_ID")
  public Long getPsnPubId() {
    return psnPubId;
  }

  public void setPsnPubId(Long psnPubId) {
    this.psnPubId = psnPubId;
  }

  @Transient
  public Long getPmId() {
    return pmId;
  }

  public void setPmId(Long pmId) {
    this.pmId = pmId;
  }

  @Transient
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

}
