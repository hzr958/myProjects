package com.smate.center.task.model.rol.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用于标记是否发送指派数据到用户，默认冗余psn_ins表is_login.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_PUB_SEND_FLAG")
public class PsnPubSendFlag implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 696338437992413067L;

  private Long psnId;
  /**
   * 否允许发送成果指派(一般只记录1的情况)，默认冗余psn_ins表is_login.
   */
  private Integer flag;

  public PsnPubSendFlag() {
    super();
  }

  public PsnPubSendFlag(Long psnId, Integer flag) {
    super();
    this.psnId = psnId;
    this.flag = flag;
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "SEND_FLAG")
  public Integer getFlag() {
    return flag;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

}
