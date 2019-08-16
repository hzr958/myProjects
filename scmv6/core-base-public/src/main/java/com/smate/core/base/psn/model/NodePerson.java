package com.smate.core.base.psn.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 节点所在个人基本信息表.
 * 
 * 
 */
@Entity
@Table(name = "PERSON")
public class NodePerson implements Serializable {

  private static final long serialVersionUID = -6189809076258183815L;
  private Long psnId;
  private String psnName;
  private String psnHeadUrl;
  private String psnTitle;
  private String psnEmail;
  private String des3PsnId;
  private String psnEname;
  private String firstName;
  private String lastName;
  private Long isPrivate;
  private String isSend;
  private String insName;
  private String regionZhName;
  private String regionEnName;
  private Long regionId;
  private Long complete;

  public NodePerson() {
    super();
  }

  public NodePerson(Long psnId, String psnName, String psnHeadUrl, String psnTitle, String psnEmail) {
    super();
    this.psnId = psnId;
    this.psnName = psnName;
    this.psnHeadUrl = psnHeadUrl;
    this.psnTitle = psnTitle;
    this.psnEmail = psnEmail;
  }

  public NodePerson(Long psnId, String psnName, String psnHeadUrl, String psnTitle, String psnEname, String firstName,
      String lastName, String insName, String regionZhName, String regionEnName) {
    super();
    this.psnId = psnId;
    this.psnName = psnName;
    this.psnHeadUrl = psnHeadUrl;
    this.psnTitle = psnTitle;
    this.psnEname = psnEname;
    this.firstName = firstName;
    this.lastName = lastName;
    this.insName = insName;
    this.regionZhName = regionZhName;
    this.regionEnName = regionEnName;
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "NAME")
  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  @Column(name = "AVATARS")
  public String getPsnHeadUrl() {
    return psnHeadUrl;
  }

  public void setPsnHeadUrl(String psnHeadUrl) {
    this.psnHeadUrl = psnHeadUrl;
  }

  @Column(name = "TITOLO")
  public String getPsnTitle() {
    return psnTitle;
  }

  public void setPsnTitle(String psnTitle) {
    this.psnTitle = psnTitle;
  }

  @Column(name = "EMAIL")
  public String getPsnEmail() {
    return psnEmail;
  }

  public void setPsnEmail(String psnEmail) {
    this.psnEmail = psnEmail;
  }

  @Transient
  public String getDes3PsnId() {
    if (this.psnId != null && des3PsnId == null) {
      des3PsnId = ServiceUtil.encodeToDes3(this.psnId.toString());
    }
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  @Column(name = "ENAME")
  public String getPsnEname() {
    return psnEname;
  }

  public void setPsnEname(String psnEname) {
    this.psnEname = psnEname;
  }

  @Column(name = "FIRST_NAME")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Column(name = "LAST_NAME")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Transient
  public Long getIsPrivate() {
    if (isPrivate == null)
      return 0l;
    return isPrivate;
  }

  public void setIsPrivate(Long isPrivate) {
    this.isPrivate = isPrivate;
  }

  @Column(name = "INS_NAME")
  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  @Column(name = "REGION_ID")
  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  @Column(name = "COMPLETE")
  public Long getComplete() {
    return complete;
  }

  public void setComplete(Long complete) {
    this.complete = complete;
  }

  @Transient
  public String getIsSend() {
    return isSend;
  }

  public void setIsSend(String isSend) {
    this.isSend = isSend;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Transient
  public String getRegionZhName() {
    return regionZhName;
  }

  public void setRegionZhName(String regionZhName) {
    this.regionZhName = regionZhName;
  }

  @Transient
  public String getRegionEnName() {
    return regionEnName;
  }

  public void setRegionEnName(String regionEnName) {
    this.regionEnName = regionEnName;
  }

}
