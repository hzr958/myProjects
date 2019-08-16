package com.smate.web.group.model.group.homepage;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 群组主页设置表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "GROUP_HOMEPAGE_CONFIG")
public class GroupHomePageConfig implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6636523612168392713L;
  /**
   * 
   */
  private Long confId;
  // 用户id
  private Long groupId;
  // 模板id
  private Long tmpId;
  // 设置数据json格式
  private String confData;
  // 是否关闭
  private Integer enabled;
  // 配置实体，编辑时使用
  private GhpConfig config;
  // 主页地址冗余，与CAS sys_group_url相同
  private String url;

  public GroupHomePageConfig() {
    super();
  }

  public GroupHomePageConfig(Long confId, Long groupId, Long tmpId, String confData, Integer enabled, GhpConfig config,
      String url) {
    super();
    this.confId = confId;
    this.groupId = groupId;
    this.tmpId = tmpId;
    this.confData = confData;
    this.enabled = enabled;
    this.config = config;
    this.url = url;
  }

  @Id
  @Column(name = "CONF_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_GROUP_HOMEPAGE_CONFIG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getConfId() {
    return confId;
  }

  @Column(name = "TMP_ID")
  public Long getTmpId() {
    return tmpId;
  }

  @Column(name = "CONF_DATA")
  public String getConfData() {
    return confData;
  }

  @Column(name = "ENABLED")
  public Integer getEnabled() {
    return enabled;
  }

  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  @Column(name = "URL")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public void setConfId(Long confId) {
    this.confId = confId;
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

  @Transient
  public GhpConfig getConfig() {
    return config;
  }

  public void setConfig(GhpConfig config) {
    this.config = config;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((confId == null) ? 0 : confId.hashCode());
    result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
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
    GroupHomePageConfig other = (GroupHomePageConfig) obj;
    if (confId == null) {
      if (other.confId != null)
        return false;
    } else if (!confId.equals(other.confId))
      return false;
    if (groupId == null) {
      if (other.groupId != null)
        return false;
    } else if (!groupId.equals(other.groupId))
      return false;
    if (tmpId == null) {
      if (other.tmpId != null)
        return false;
    } else if (!tmpId.equals(other.tmpId))
      return false;
    return true;
  }

}
