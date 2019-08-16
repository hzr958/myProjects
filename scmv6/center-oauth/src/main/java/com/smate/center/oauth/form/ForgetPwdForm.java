package com.smate.center.oauth.form;

import com.smate.core.base.utils.security.Des3Utils;

public class ForgetPwdForm {
  private String email;
  private String returnUrl;
  private String key;
  private String gen;
  private String newpwd;
  private String rolTitle;

  public String getEmail() {
    String decodeEmail = Des3Utils.decodeFromDes3(email);
    if (decodeEmail == null)
      return email;
    else
      return decodeEmail;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getReturnUrl() {
    return returnUrl;
  }

  public void setReturnUrl(String returnUrl) {
    this.returnUrl = returnUrl;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getGen() {
    return gen;
  }

  public void setGen(String gen) {
    this.gen = gen;
  }

  public String getNewpwd() {
    return newpwd;
  }

  public void setNewpwd(String newpwd) {
    this.newpwd = newpwd;
  }

  public String getRolTitle() {
    return rolTitle;
  }

  public void setRolTitle(String rolTitle) {
    this.rolTitle = rolTitle;
  }

}
