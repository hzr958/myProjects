package com.smate.sie.center.open.model.dept;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 第三方人员信息主键.
 * 
 * @author xys
 *
 */
@Embeddable
public class ImportThirdPsnsPK implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7848516979733320631L;

  @Column(name = "EMAIL")
  private String email;// 邮箱
  @Column(name = "INS_ID")
  private Long insId;// 单位ID

  public ImportThirdPsnsPK() {
    super();
  }

  public ImportThirdPsnsPK(String email, Long insId) {
    super();
    this.email = email;
    this.insId = insId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

}
