package com.smate.web.dyn.form.main;

import org.apache.commons.lang3.StringUtils;

public class MainForm {
  private Long psnId;
  private String avatars;
  private String des3PsnId;
  private Integer status;// 是否付费标识，0未付费，1已付费 显示菜单
  private String ip;

  public String getAvatars() {
    if (StringUtils.isBlank(avatars)) {
      return "/resmod/smate-pc/img/logo_psndefault.png";
    }
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

}
