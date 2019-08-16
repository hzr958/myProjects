package com.smate.center.open.model.resume;

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
 * 公开信息设置表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "RESUME_CONFIG")
public class ResumeConfig implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3833558117567543472L;

  private Long confId;
  // 用户id
  private Long psnId;
  // 模板id
  private Long tmpId;
  // 设置数据json格式
  private String confData;
  // 是否关闭
  private Integer enabled;
  // 配置实体，编辑时使用
  private Config config;
  // 主页地址冗余，与cas sys_use_url相同
  private String url;

  // 1:需要重新修复JSON数据结构；0:不需要刷新
  private Integer fixed;
  // 最后修复的时间
  private Date fixedDate;

  @Id
  @Column(name = "CONF_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_RESUME_CONFIG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getConfId() {
    return confId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "TMP_ID")
  public Long getTmpId() {
    return tmpId;
  }

  @Column(name = "CONF_DATA_JSON")
  public String getConfData() {
    return confData;
  }

  @Column(name = "ENABLED")
  public Integer getEnabled() {
    return enabled;
  }

  @Column(name = "URL")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setConfId(Long confId) {
    this.confId = confId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setTmpId(Long tmpId) {
    this.tmpId = tmpId;
  }

  public void setConfData(String confData) {
    this.confData = confData;
  }

  public void setEnabled(Integer enabled) {
    this.enabled = enabled;
  }

  @Column(name = "FIXED")
  public Integer getFixed() {
    return fixed;
  }

  @Column(name = "FIXED_DATE")
  public Date getFixedDate() {
    return fixedDate;
  }

  public void setFixed(Integer fixed) {
    this.fixed = fixed;
  }

  public void setFixedDate(Date fixedDate) {
    this.fixedDate = fixedDate;
  }

  @Transient
  public Config getConfig() {
    return config;
  }

  public void setConfig(Config config) {
    this.config = config;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((confId == null) ? 0 : confId.hashCode());
    result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
    result = prime * result + ((psnId == null) ? 0 : psnId.hashCode());
    result = prime * result + ((tmpId == null) ? 0 : tmpId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ResumeConfig other = (ResumeConfig) obj;
    if (confId == null) {
      if (other.confId != null)
        return false;
    } else if (!confId.equals(other.confId))
      return false;
    if (enabled == null) {
      if (other.enabled != null)
        return false;
    } else if (!enabled.equals(other.enabled))
      return false;
    if (psnId == null) {
      if (other.psnId != null)
        return false;
    } else if (!psnId.equals(other.psnId))
      return false;
    if (tmpId == null) {
      if (other.tmpId != null)
        return false;
    } else if (!tmpId.equals(other.tmpId))
      return false;
    return true;
  }

}
