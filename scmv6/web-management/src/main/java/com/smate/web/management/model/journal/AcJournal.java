package com.smate.web.management.model.journal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 期刊.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "JOURNAL")
public class AcJournal implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7769899890187992475L;

  // Journal ID (system generated key)
  private Long code;
  // 期刊中文名
  private String zhName;
  // 期刊英文名
  private String enName;
  // 最终选定名称
  private String name;
  // ISSN
  private String issn;
  // 添加人
  private Long psnId;
  // 状态0: Registered; 1: Approved; 2: Deleted 记录状态；Researcher填写的默认为0, 审核后为1
  private Integer status;

  @Id
  @Column(name = "JID")
  public Long getCode() {
    return code;
  }

  public void setCode(Long code) {
    this.code = code;
  }

  @Column(name = "ZH_NAME")
  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  @Column(name = "EN_NAME")
  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "REG_PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "ISSN")
  public String getIssn() {
    return issn;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  @Transient
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
