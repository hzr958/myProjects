package com.smate.web.psn.model.resume;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 用户联系信息查看配置表主键.
 * 
 * @author liqinghua
 * 
 */
@Embeddable
public class ContactConfigKey implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3827035311694602986L;

  // 1公众用户、2科研之友用户、3好友、4机构用户
  private Long tmpId;
  private Long psnId;

  public ContactConfigKey() {
    super();
  }

  public ContactConfigKey(Long tmpId, Long psnId) {
    super();
    this.tmpId = tmpId;
    this.psnId = psnId;
  }

  @Column(name = "TMP_ID")
  public Long getTmpId() {
    return tmpId;
  }

  public void setTmpId(Long tmpId) {
    this.tmpId = tmpId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
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
    ContactConfigKey other = (ContactConfigKey) obj;
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
