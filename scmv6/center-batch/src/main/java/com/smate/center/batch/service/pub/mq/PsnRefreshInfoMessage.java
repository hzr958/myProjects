package com.smate.center.batch.service.pub.mq;

/**
 * 人员冗余信息刷新消息.
 * 
 * @author liqinghua
 * 
 */
public class PsnRefreshInfoMessage {

  /**
   * 
   */
  private static final long serialVersionUID = -1391067552608844925L;

  private Long psnId;
  // 见表PSN_REFRESH_PUB_INFO刷新
  private Integer pub = 0;
  // 自填关键词刷新
  private Integer kwZt = 0;
  // 人员单位信息
  private Integer ins = 0;
  // 人员职称信息
  private Integer position = 0;
  // 需要刷新的成果ID
  private Long refshPubId = null;
  // 是否删除成果
  private Integer isDelPub = 0;

  // 用户姓名刷新
  private Integer nameFlag = 0;
  // 用户EMAIL刷新
  private Integer emailFlag = 0;
  // 用户工作经历刷新
  private Integer workFlag = 0;
  // 用户所教课程刷新
  private Integer courseFlag = 0;

  private Integer pubToPdwh = 0;// lgk:人员与成果信息
  // 学位
  private Integer degree = 0;
  // 人员领域大类
  private Integer areaClf = 0;
  // 参考文献刷新
  private Integer refc = 0;

  public PsnRefreshInfoMessage() {
    super();
  }

  public PsnRefreshInfoMessage(Long psnId) {
    super();
    this.psnId = psnId;
  }

  /**
   * 构造实例.
   * 
   * @param psnId
   * @return
   */
  public static PsnRefreshInfoMessage getInstance(Long psnId) {

    PsnRefreshInfoMessage msg = new PsnRefreshInfoMessage(psnId);
    return msg;
  }

  public PsnRefreshInfoMessage(Long psnId, Integer nodeId) {
    this.psnId = psnId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public Integer getPub() {
    return pub;
  }

  public Integer getKwZt() {
    return kwZt;
  }

  public Integer getIns() {
    return ins;
  }

  public Integer getPosition() {
    return position;
  }

  public Long getRefshPubId() {
    return refshPubId;
  }

  public Integer getIsDelPub() {
    return isDelPub;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setPub(Integer pub) {
    this.pub = pub;
  }

  public void setKwZt(Integer kwZt) {
    this.kwZt = kwZt;
  }

  public void setIns(Integer ins) {
    this.ins = ins;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  public void setRefshPubId(Long refshPubId) {
    this.refshPubId = refshPubId;
  }

  public void setIsDelPub(Integer isDelPub) {
    this.isDelPub = isDelPub;
  }

  public Integer getNameFlag() {
    return nameFlag;
  }

  public Integer getEmailFlag() {
    return emailFlag;
  }

  public Integer getWorkFlag() {
    return workFlag;
  }

  public void setNameFlag(Integer nameFlag) {
    this.nameFlag = nameFlag;
  }

  public void setEmailFlag(Integer emailFlag) {
    this.emailFlag = emailFlag;
  }

  public void setWorkFlag(Integer workFlag) {
    this.workFlag = workFlag;
  }

  public Integer getPubToPdwh() {
    return pubToPdwh;
  }

  public void setPubToPdwh(Integer pubToPdwh) {
    this.pubToPdwh = pubToPdwh;
  }

  public Integer getDegree() {
    return degree;
  }

  public void setDegree(Integer degree) {
    this.degree = degree;
  }

  public Integer getRefc() {
    return refc;
  }

  public void setRefc(Integer refc) {
    this.refc = refc;
  }

  public Integer getAreaClf() {
    return areaClf;
  }

  public void setAreaClf(Integer areaClf) {
    this.areaClf = areaClf;
  }

  public Integer getCourseFlag() {
    return courseFlag;
  }

  public void setCourseFlag(Integer courseFlag) {
    this.courseFlag = courseFlag;
  }

}
