package com.smate.center.task.model.thirdparty;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 第三方信息来源记录.
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "V_THIRD_SOURCES")
public class ThirdSources {
  @Id
  @Column(name = "SOURCE_ID")
  private Long sourceId;// 主键

  @Column(name = "FROM_SYS")
  private String fromSys;// 来源系统

  @Column(name = "FROM_URL")
  private String fromUrl;// 来源系统接口地址

  @Column(name = "STATUS")
  private Integer status;// 状态：0可用，1不可用

  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建时间

  @Column(name = "TOKEN")
  private String token;

  @Column(name = "AGENCY_ID")
  private Long agencyId;

  @Transient
  private List<ThirdSourcesType> tsType;

  public List<ThirdSourcesType> getTsType() {
    return tsType;
  }

  public void setTsType(List<ThirdSourcesType> tsType) {
    this.tsType = tsType;
  }

  public Long getSourceId() {
    return sourceId;
  }

  public void setSourceId(Long sourceId) {
    this.sourceId = sourceId;
  }

  public String getFromSys() {
    return fromSys;
  }

  public void setFromSys(String fromSys) {
    this.fromSys = fromSys;
  }

  public String getFromUrl() {
    return fromUrl;
  }

  public void setFromUrl(String fromUrl) {
    this.fromUrl = fromUrl;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  @Override
  public String toString() {
    return "ThirdSources [sourceId=" + sourceId + ", fromSys=" + fromSys + ", fromUrl=" + fromUrl + ", status=" + status
        + ", createDate=" + createDate + ", token=" + token + ", tsType=" + tsType + "]";
  }

  public Long getAgencyId() {
    return agencyId;
  }

  public void setAgencyId(Long agencyId) {
    this.agencyId = agencyId;
  }

}
