/**
 * 
 */
package com.smate.web.management.model.journal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author cwli
 * 
 */
@Embeddable
public class PsnJnlKeyId implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4621032335342255399L;
  private Long psnId;
  private Long jnlId;

  public PsnJnlKeyId() {
    super();
  }

  public PsnJnlKeyId(Long psnId, Long jnlId) {
    super();
    this.psnId = psnId;
    this.jnlId = jnlId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "JNL_ID")
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

}
