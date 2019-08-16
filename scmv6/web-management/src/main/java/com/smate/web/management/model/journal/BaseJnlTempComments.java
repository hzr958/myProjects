package com.smate.web.management.model.journal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * xys基础期刊临时评论表.
 */
@Entity
@Table(name = "BASE_JOURNAL_TEMP_COMMENTS")
public class BaseJnlTempComments implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1044933612908222091L;
  private Long jnlId;
  private Long tempNum;

  @Id
  @Column(name = "JNL_ID")
  public Long getJnlId() {
    return jnlId;
  }

  public void setJnlId(Long jnlId) {
    this.jnlId = jnlId;
  }

  @Column(name = "TEMP_NUM")
  public Long getTempNum() {
    return tempNum;
  }

  public void setTempNum(Long tempNum) {
    this.tempNum = tempNum;
  }

}
