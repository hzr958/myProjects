package com.smate.center.open.model.consts;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 邮件类型
 * 
 * @author YPH
 * 
 */
@Entity
@Table(name = "CONST_MAIL_TYPE")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class ConstMailType implements Serializable {
  // Id
  private Long mailTypeId;
  // 类型中文名
  private String typeZhName;
  // 类型英文名
  private String typeEnName;
  // 备注
  private String remark;
  // 状态 0关闭(可以用)，1开放(不可以用)
  private Long status;

  @Id
  @Column(name = "MAIL_TYPE_ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_MAIL_TYPE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getMailTypeId() {
    return mailTypeId;
  }

  public void setMailTypeId(Long mailTypeId) {
    this.mailTypeId = mailTypeId;
  }

  @Column(name = "TYPE_ZH_NAME")
  public String getTypeZhName() {
    return typeZhName;
  }

  public void setTypeZhName(String typeZhName) {
    this.typeZhName = typeZhName;
  }

  @Column(name = "TYPE_EN_NAME")
  public String getTypeEnName() {
    return typeEnName;
  }

  public void setTypeEnName(String typeEnName) {
    this.typeEnName = typeEnName;
  }

  @Column(name = "REMARK")
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Column(name = "STATUS")
  public Long getStatus() {
    return status;
  }

  public void setStatus(Long status) {
    this.status = status;
  }

}
