package com.smate.sie.center.task.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 动态接收者
 */
@Entity
@Table(name = "DYN_RECEIVER")
public class SieDynReceiver {

  @EmbeddedId
  private SieDynReceiverPk pk;

  public SieDynReceiverPk getPk() {
    return pk;
  }

  public void setPk(SieDynReceiverPk pk) {
    this.pk = pk;
  }

}
