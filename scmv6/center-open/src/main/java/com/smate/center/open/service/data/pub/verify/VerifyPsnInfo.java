package com.smate.center.open.service.data.pub.verify;

import com.smate.core.base.utils.string.StringUtils;

/**
 * @author aijiangbin
 * @create 2018-11-13 14:22
 **/
public class VerifyPsnInfo {

  public String name = "";
  public String email = "";
  public String phone = "";

  public String getName() {
    if (StringUtils.isNotBlank(name)) {
      name = name.trim();
    }
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    if (StringUtils.isNotBlank(email)) {
      email = email.toLowerCase().trim();
    }
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    if (StringUtils.isNotBlank(phone)) {
      phone = phone.trim();
    }
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }
}
