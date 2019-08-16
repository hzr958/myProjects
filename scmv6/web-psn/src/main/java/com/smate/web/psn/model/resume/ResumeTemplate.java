package com.smate.web.psn.model.resume;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 公开信息设置模板.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "RESUME_TEMPLATE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ResumeTemplate implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3278551000806541848L;

  // 1公众用户、2科研之友用户、3好友、4机构用户
  private Long tmpId;
  // 模板名称
  private String name;

  @Id
  @Column(name = "TMP_ID")
  public Long getTmpId() {
    return tmpId;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  public void setTmpId(Long tmpId) {
    this.tmpId = tmpId;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    ResumeTemplate other = (ResumeTemplate) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (tmpId == null) {
      if (other.tmpId != null)
        return false;
    } else if (!tmpId.equals(other.tmpId))
      return false;
    return true;
  }

}
