package com.smate.core.base.utils.model.msg;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 手机白名单
 * @author aijiangbin
 * @create 2018-12-25 14:12
 **/
@Entity
@Table (name = "V_MOBILE_WHITELIST")
public class MobileWhitelist {

  @Id
  @Column (name = "ID")
  private Long id ;

  @Column (name = "MOBILE")
  private String mobile ;

  @Column (name = "STATUS")
  private Integer status ;



  public Long getId() {
    return id;
  }

  public MobileWhitelist() {
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
}
