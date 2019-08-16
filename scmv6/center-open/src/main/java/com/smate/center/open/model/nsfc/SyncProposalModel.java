package com.smate.center.open.model.nsfc;

import java.io.Serializable;

/**
 * 同步申报书信息
 * 
 * 
 */

public class SyncProposalModel implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -96089808212580191L;
  // isis申请人guid
  private String psnGuid;
  // isis申请书guid
  private String guid;
  // 成果ID
  private String pubId;
  // 申请书中文名
  private String ctitle;
  // 申请书英文名
  private String etitle;
  // 业务规则代码
  private String code;
  private String status;
  private String year;
  private Long psnId;
  private Boolean isRollBack = false;
  private String grantCode;
  private String des3GrantCode;

  /**
   * @return the psnGuid
   */
  public String getPsnGuid() {
    return psnGuid;
  }

  /**
   * @param psnGuid the psnGuid to set
   */
  public void setPsnGuid(String psnGuid) {
    this.psnGuid = psnGuid;
  }

  /**
   * @return the guid
   */
  public String getGuid() {
    return guid;
  }

  /**
   * @param guid the guid to set
   */
  public void setGuid(String guid) {
    this.guid = guid;
  }

  /**
   * @return the pubId
   */
  public String getPubId() {
    return pubId;
  }

  /**
   * @param pubId the pubId to set
   */
  public void setPubId(String pubId) {
    this.pubId = pubId;
  }

  /**
   * @return the ctitle
   */
  public String getCtitle() {
    return ctitle;
  }

  /**
   * @param ctitle the ctitle to set
   */
  public void setCtitle(String ctitle) {
    this.ctitle = ctitle;
  }

  /**
   * @return the etitle
   */
  public String getEtitle() {
    return etitle;
  }

  /**
   * @param etitle the etitle to set
   */
  public void setEtitle(String etitle) {
    this.etitle = etitle;
  }

  /**
   * @return the code
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code the code to set
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * @return the psnId
   */
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
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * @return the year
   */
  public String getYear() {
    return year;
  }

  /**
   * @param year the year to set
   */
  public void setYear(String year) {
    this.year = year;
  }

  /**
   * @return the isRollBack
   */
  public Boolean getIsRollBack() {
    return isRollBack;
  }

  /**
   * @param isRollBack the isRollBack to set
   */
  public void setIsRollBack(Boolean isRollBack) {
    this.isRollBack = isRollBack;
  }

  /**
   * @return the grantCode
   */
  public String getGrantCode() {
    return grantCode;
  }

  /**
   * @param grantCode the grantCode to set
   */
  public void setGrantCode(String grantCode) {
    this.grantCode = grantCode;
  }

  /**
   * @return the des3GrantCode
   */
  public String getDes3GrantCode() {
    return des3GrantCode;
  }

  /**
   * @param des3GrantCode the des3GrantCode to set
   */
  public void setDes3GrantCode(String des3GrantCode) {
    this.des3GrantCode = des3GrantCode;
  }

}
