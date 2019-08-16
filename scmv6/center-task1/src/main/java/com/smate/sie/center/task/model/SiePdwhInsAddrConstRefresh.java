package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * SIE库标准单位地址信息常量表中的附属表，用于往主表中新增数据。
 * 
 * @author YEXINGYUAN
 * @date 2018年7月17日
 */
@Entity
@Table(name = "PDWH_INS_ADDR_CONST_REFRESH")
public class SiePdwhInsAddrConstRefresh implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1778487109188409785L;
  private SiePdwhInsAddrConstRefreshPK pk;
  private Integer refreshStatus;// 0不可用，1可用 和institution表相同
  private Integer language;// 单位语言，1中文，2英文

  public SiePdwhInsAddrConstRefresh() {
    super();
  }

  @EmbeddedId
  public SiePdwhInsAddrConstRefreshPK getPk() {
    return pk;
  }

  public void setPk(SiePdwhInsAddrConstRefreshPK pk) {
    this.pk = pk;
  }

  @Column(name = "REFRESH_STATUS")
  public Integer getRefreshStatus() {
    return refreshStatus;
  }

  public void setRefreshStatus(Integer refreshStatus) {
    this.refreshStatus = refreshStatus;
  }

  @Column(name = "LANGUAGE")
  public Integer getLanguage() {
    return language;
  }

  public void setLanguage(Integer language) {
    this.language = language;
  }

}
