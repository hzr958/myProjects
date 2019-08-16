package com.smate.web.v8pub.dom;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smate.core.base.pub.enums.PubPatentAreaEnum;
import com.smate.core.base.pub.enums.PubPatentTransitionStatusEnum;

/**
 * 专利信息
 * 
 * @author houchuanjie
 * @date 2018/05/30 16:59
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatentInfoBean extends PubTypeInfoBean implements Serializable {

  private static final long serialVersionUID = -847234542756756302L;

  private String applicationNo = new String(); // 申请号

  private String publicationOpenNo = new String(); // 专利公开（公告）号

  private PubPatentAreaEnum area = PubPatentAreaEnum.OTHER; // 专利国家或地区

  private String type = new String(); // 专利类型，发明专利51/实用新型52/外观设计53/植物专利54

  private String applicationDate = new String(); // 申请日期

  private String startDate = new String(); // 专利生效起始日期

  private String endDate = new String(); // 专利失效结束日期

  private String issuingAuthority = new String(); // 专利授权组织，签发机关

  private Integer status = 0; // 专利状态，0:申请/1:授权

  private String applier = new String(); // 申请人 (专利为申请状态)

  private PubPatentTransitionStatusEnum transitionStatus = PubPatentTransitionStatusEnum.NONE; // 专利转换状态

  private String price = new String(); // 交易金额(万元)

  private String IPC = new String(); // IPC号

  private String CPC = new String(); // cpc号

  private String patentee = new String(); // 专利权人 (专利为授权状态)

  /**
   * 申请日期
   * 
   * @return
   */
  public String getApplicationDate() {
    return applicationDate;
  }

  public void setApplicationDate(String applicationDate) {
    this.applicationDate = applicationDate;
  }

  /**
   * 专利类型
   * 
   * @return
   */
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /**
   * 生效日期
   * 
   * @return
   */
  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  /**
   * 失效日期
   * 
   * @return
   */
  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  /**
   * 专利国家或地区
   * 
   * @return
   */
  public PubPatentAreaEnum getArea() {
    return area;
  }

  public void setArea(PubPatentAreaEnum area) {
    this.area = area;
  }

  /**
   * 签发机关
   * 
   * @return
   */
  public String getIssuingAuthority() {
    return issuingAuthority;
  }

  public void setIssuingAuthority(String issuingAuthority) {
    this.issuingAuthority = issuingAuthority;
  }

  /**
   * 专利申请号
   * 
   * @return
   */
  public String getApplicationNo() {
    return applicationNo;
  }

  public void setApplicationNo(String applicationNo) {
    this.applicationNo = applicationNo;
  }

  /**
   * 专利公开（告）号
   * 
   * @return
   */
  public String getPublicationOpenNo() {
    return publicationOpenNo;
  }

  public void setPublicationOpenNo(String publicationOpenNo) {
    this.publicationOpenNo = publicationOpenNo;
  }

  /**
   * 专利状态
   * 
   * @return
   */
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  /**
   * 申请人
   * 
   * @return
   */
  public String getApplier() {
    return applier;
  }

  public void setApplier(String applier) {
    this.applier = applier;
  }

  /**
   * 专利转换状态
   * 
   * @return
   */
  public PubPatentTransitionStatusEnum getTransitionStatus() {
    return transitionStatus;
  }

  public void setTransitionStatus(PubPatentTransitionStatusEnum transitionStatus) {
    this.transitionStatus = transitionStatus;
  }

  /**
   * 专利交易金额
   * 
   * @return
   */
  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getIPC() {
    return IPC;
  }

  public void setIPC(String iPC) {
    IPC = iPC;
  }

  public String getCPC() {
    return CPC;
  }

  public void setCPC(String cPC) {
    CPC = cPC;
  }

  public String getPatentee() {
    return patentee;
  }

  public void setPatentee(String patentee) {
    this.patentee = patentee;
  }

  @Override
  public String toString() {
    return "PatentInfoBean{" + "applicationNo='" + applicationNo + '\'' + ", publicationOpenNo='" + publicationOpenNo
        + '\'' + ", IPC='" + IPC + '\'' + ", area=" + area + ", type='" + type + '\'' + ", applicationDate='"
        + applicationDate + '\'' + ", startDate=" + startDate + ", endDate=" + endDate + ", issuingAuthority='"
        + issuingAuthority + '\'' + ", status='" + status + '\'' + ", applier='" + applier + '\''
        + ", transitionStatus='" + transitionStatus + '\'' + ", price='" + price + '\'' + '}';
  }
}
