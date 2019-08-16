package com.smate.center.batch.model.sns.pub;

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

/**
 * 
 * @author oyh
 * 
 */
@Entity
@Table(name = "NSFC_PROPOSAL")
public class NsfcProposal implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7200749475525360968L;
  /**
   * 申报书id
   */
  private Long prpId;
  /**
   * 申请书guid
   */
  private String isisGuid;
  /**
   * 申请书中文名称
   */
  private String ctitle;
  /**
   * 申请书真正使用的名称
   */
  private String title;
  /**
   * 申请书英文名称
   */
  private String etitle;
  /**
   * 业务类别
   */
  private Long code;
  // 业务类别名
  private String codeName;
  /**
   * 申请书年份
   */
  private Integer prpYear;
  /**
   * 申请书状态（0--未提交 1--已提交 2-暂存状态）
   */
  private Integer status;
  /**
   * 申报人psnId
   */
  private Long prpPsnId;
  // 更新申报书日期
  private Date prpDate;
  // 用于判断当前申请书成果排序是否是最新的
  private Integer version;

  /**
   * @return the prpId
   */
  @Id
  @Column(name = "PRP_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_NSFC_PROPOSAL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getPrpId() {
    return prpId;
  }

  /**
   * @param prpId the prpId to set
   */
  public void setPrpId(Long prpId) {
    this.prpId = prpId;
  }

  /**
   * @return the isisGuid
   */
  @Column(name = "ISIS_GUID")
  public String getIsisGuid() {
    return isisGuid;
  }

  /**
   * @param isisGuid the isisGuid to set
   */
  public void setIsisGuid(String isisGuid) {
    this.isisGuid = isisGuid;
  }

  /**
   * @return the ctitle
   */
  @Column(name = "CTITLE")
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
  @Column(name = "ETITLE")
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
   * @return the title
   */
  @Transient
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the code
   */
  @Column(name = "CODE")
  public Long getCode() {
    return code;
  }

  /**
   * @param code the code to set
   */
  public void setCode(Long code) {
    this.code = code;
  }

  /**
   * @return the codeName
   */
  @Transient
  public String getCodeName() {
    return codeName;
  }

  /**
   * @param codeName the codeName to set
   */
  public void setCodeName(String codeName) {
    this.codeName = codeName;
  }

  /**
   * @return the prpYear
   */
  @Column(name = "PRP_YEAR")
  public Integer getPrpYear() {
    return prpYear;
  }

  /**
   * @param prpYear the prpYear to set
   */
  public void setPrpYear(Integer prpYear) {
    this.prpYear = prpYear;
  }

  /**
   * @return the status
   */
  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  /**
   * @return the prpPsnId
   */
  @Column(name = "PRP_PSN_ID")
  public Long getPrpPsnId() {
    return prpPsnId;
  }

  /**
   * @param prpPsnId the prpPsnId to set
   */
  public void setPrpPsnId(Long prpPsnId) {
    this.prpPsnId = prpPsnId;
  }

  /**
   * @return the prpDate
   */
  @Column(name = "PRP_DATE")
  public Date getPrpDate() {
    return prpDate;
  }

  /**
   * @param prpDate the prpDate to set
   */
  public void setPrpDate(Date prpDate) {
    this.prpDate = prpDate;
  }

  @Column(name = "PROPOSAL_VERSION")
  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

}
