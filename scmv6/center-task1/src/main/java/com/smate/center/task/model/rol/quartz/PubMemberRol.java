package com.smate.center.task.model.rol.quartz;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 成果/人员信息.
 * 
 * @author zjh
 * 
 */
@Entity
@Table(name = "PUB_MEMBER")
public class PubMemberRol implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6206126695861455383L;

  // 主键唯一标识
  private Long id;
  // Publication ID
  private Long pubId;
  // Sequence no for display
  private Integer seqNo;
  // member name
  private String name;
  // 成员在ScholarMate中的psn_id
  private Long psnId;
  private String psnName;
  // 作者所在单位个数，单位详细存放在XML
  private Integer insCount;
  // 0:作者,1:通讯作者
  private Integer authorPos;
  // 作者指派到人列表
  private List<PubPsnRol> assignedPsnList;
  // 部门名称列表
  private List<String> insNameList;
  // 成果所属单位，冗余
  private Long insId;
  // 认领结果0为未确认认领、1为已确认认领（供单位个人用户确认使用）
  private Integer confirmResult;

  // 成果作者机构ID。可能是本机构；也是能是其他机构；如果地址未确定机构，则设置为1；地址空则设置为空，如果匹配上了作者，设置为本机构，否则为空
  private Long pmInsId;
  // 如果member_psn_id为空，则为0，不为空为1
  private Integer isConfirm;
  // 冗余member_psn_id的unit_id
  private Long unitId;
  // 冗余member_psn_id的unit_id的父部门ID
  private Long parentUnitId;
  // 成果所在市级地区，根据pm_ins_id计算；如果未确定，则设置为1；pm_ins_id为空则为空
  private Long pmCity;
  // 成果所在省（直辖市），根据pm_ins_id计算；如果未确定，则设置为1；pm_ins_id为空则为空
  private Long pmPrv;
  // 成果所在市级管辖区ID，根据pm_ins_id计算；如果未确定，则设置为1；pm_ins_id为空则为空
  private Long pmDis;

  public PubMemberRol() {
    super();
  }

  public PubMemberRol(Long pubId, Integer seqNo, String name, Long psnId, Integer authorPos, Long insId, Long pmInsId,
      Integer isConfirm) {
    super();
    this.pubId = pubId;
    this.seqNo = seqNo;
    this.name = name;
    this.psnId = psnId;
    this.authorPos = authorPos;
    this.insId = insId;
    this.pmInsId = pmInsId;
    this.isConfirm = isConfirm;
    this.insCount = 0;
  }

  @Id
  @Column(name = "PM_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_MEMBER", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }

  @Column(name = "MEMBER_NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "MEMBER_PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {

    this.psnId = psnId;
  }

  @Column(name = "INS_COUNT")
  public Integer getInsCount() {
    return insCount;
  }

  public void setInsCount(Integer insCount) {
    this.insCount = insCount;
  }

  @Column(name = "AUTHOR_POS")
  public Integer getAuthorPos() {
    return authorPos;
  }

  @Column(name = "PM_DIS")
  public Long getPmDis() {
    return pmDis;
  }

  public void setPmDis(Long pmDis) {
    this.pmDis = pmDis;
  }

  public void setAuthorPos(Integer authorPos) {
    this.authorPos = authorPos;
  }

  /**
   * @return the assignedPsnList
   */
  @Transient
  public List<PubPsnRol> getAssignedPsnList() {
    return assignedPsnList;
  }

  /**
   * @param assignedPsnList the assignedPsnList to set
   */
  public void setAssignedPsnList(List<PubPsnRol> assignedPsnList) {
    this.assignedPsnList = assignedPsnList;
  }

  @Transient
  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  @Transient
  public List<String> getInsNameList() {
    return insNameList;
  }

  public void setInsNameList(List<String> insNameList) {
    this.insNameList = insNameList;
  }

  /**
   * @return the insId
   */
  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "PM_INS_ID")
  public Long getPmInsId() {
    return pmInsId;
  }

  @Column(name = "IS_CONFIRM")
  public Integer getIsConfirm() {
    return isConfirm;
  }

  @Column(name = "UNIT_ID")
  public Long getUnitId() {
    return unitId;
  }

  @Column(name = "PARENT_UNIT_ID")
  public Long getParentUnitId() {
    return parentUnitId;
  }

  @Column(name = "PM_CITY")
  public Long getPmCity() {
    return pmCity;
  }

  @Column(name = "PM_PRV")
  public Long getPmPrv() {
    return pmPrv;
  }

  public void setPmInsId(Long pmInsId) {
    this.pmInsId = pmInsId;
  }

  public void setIsConfirm(Integer isConfirm) {
    this.isConfirm = isConfirm;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  public void setParentUnitId(Long parentUnitId) {
    this.parentUnitId = parentUnitId;
  }

  public void setPmCity(Long pmCity) {
    this.pmCity = pmCity;
  }

  public void setPmPrv(Long pmPrv) {
    this.pmPrv = pmPrv;
  }

  /**
   * @param insId the insId to set
   */
  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Transient
  public Integer getConfirmResult() {
    return confirmResult;
  }

  public void setConfirmResult(Integer confirmResult) {
    this.confirmResult = confirmResult;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
