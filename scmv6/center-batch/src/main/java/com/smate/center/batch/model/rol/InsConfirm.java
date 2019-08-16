package com.smate.center.batch.model.rol;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 需要确认导入成果的单位配置表
 * 
 * @author Scy
 * 
 */
@Entity
@Table(name = "INS_CONFIRM")
public class InsConfirm implements Serializable {

  private static final long serialVersionUID = 1145773615255479286L;

  public static final Integer NOT_NEED_CONFIRM = 0;
  public static final Integer NEED_CONFIRM = 1;
  // 单位ID
  @Id
  @Column(name = "INS_ID")
  private Long insId;

  // 0：不需要确认 1：需要确认
  @Column(name = "STAUTS")
  private Integer status;

  public Long getInsId() {
    return insId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setStauts(Integer status) {
    this.status = status;
  }

}
