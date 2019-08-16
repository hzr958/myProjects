package com.smate.web.psn.form;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 
 * 账号邮箱表单
 * 
 * @author aijiangbin
 *
 */
public class AccountEmailForm {

  private Long currentPsnId; // 当前人的psnId
  private String des3CurrentPsnId; // 当前人的psnId

  private Long validatePsnId; // 验证的psnId
  private String des3validatePsnId;// 验证的psnId

  private String validateCodeBig; // 邮箱验证码

  private String validateCode; // 邮箱，弹框验证码

  private String newEmail; // 邮箱

  private String confirmResult; // 邮箱链接 确认结果

  private String des3Id; // 邮箱链接的id
  private Long id; // 邮箱链接的id
  private Long mailId; // 邮箱的id
  private Boolean resend = false; // 重新发送
  private Boolean hasConfirm = false; // 已经确认

  private Long sendEmailDate = 0L;// 发送邮件的时间 单位毫秒
  private Long delaySendDate = 0L;// 延迟发送的时间

  private String mobileNumber = "";
  private String mobileValidateCode = "";
  private boolean showMobileBox = false ; //显示手机弹框

  public boolean isShowMobileBox() {
    return showMobileBox;
  }

  public void setShowMobileBox(boolean showMobileBox) {
    this.showMobileBox = showMobileBox;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getMobileValidateCode() {
    return mobileValidateCode;
  }

  public void setMobileValidateCode(String mobileValidateCode) {
    this.mobileValidateCode = mobileValidateCode;
  }

  public String getValidateCodeBig() {
    return validateCodeBig;
  }

  public void setValidateCodeBig(String validateCodeBig) {
    this.validateCodeBig = validateCodeBig;
  }

  public Long getCurrentPsnId() {

    if (currentPsnId == null || currentPsnId == 0L) {
      this.currentPsnId = SecurityUtils.getCurrentUserId();
    }
    if (currentPsnId == null || currentPsnId == 0L && StringUtils.isNotBlank(des3CurrentPsnId)) {
      currentPsnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3CurrentPsnId), 0L);
    }

    return currentPsnId;
  }

  public void setCurrentPsnId(Long currentPsnId) {
    this.currentPsnId = currentPsnId;
  }

  public String getValidateCode() {
    return validateCode;
  }

  public void setValidateCode(String validateCode) {
    this.validateCode = validateCode;
  }



  public String getNewEmail() {
    return newEmail;
  }

  public void setNewEmail(String newEmail) {
    this.newEmail = newEmail;
  }

  public String getDes3CurrentPsnId() {
    return des3CurrentPsnId;
  }

  public void setDes3CurrentPsnId(String des3CurrentPsnId) {
    this.des3CurrentPsnId = des3CurrentPsnId;
  }

  public Long getValidatePsnId() {

    if (validatePsnId == null || validatePsnId == 0L && StringUtils.isNotBlank(des3validatePsnId)) {
      validatePsnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3validatePsnId), 0L);
    }
    return validatePsnId;
  }

  public void setValidatePsnId(Long validatePsnId) {
    this.validatePsnId = validatePsnId;
  }

  public String getDes3validatePsnId() {
    return des3validatePsnId;
  }

  public void setDes3validatePsnId(String des3validatePsnId) {
    this.des3validatePsnId = des3validatePsnId;
  }

  public String getConfirmResult() {
    return confirmResult;
  }

  public void setConfirmResult(String confirmResult) {
    this.confirmResult = confirmResult;
  }

  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  public Long getId() {
    if (id == null && StringUtils.isNotBlank(this.des3Id)) {
      id = NumberUtils.toLong(Des3Utils.decodeFromDes3(this.des3Id), 0L);
    }
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getMailId() {
    return mailId;
  }

  public void setMailId(Long mailId) {
    this.mailId = mailId;
  }

  public Boolean getResend() {
    return resend;
  }

  public void setResend(Boolean resend) {
    this.resend = resend;
  }

  public Boolean getHasConfirm() {
    return hasConfirm;
  }

  public void setHasConfirm(Boolean hasConfirm) {
    this.hasConfirm = hasConfirm;
  }

  public Long getSendEmailDate() {
    return sendEmailDate;
  }

  public void setSendEmailDate(Long sendEmailDate) {
    this.sendEmailDate = sendEmailDate;
  }

  public Long getDelaySendDate() {
    return delaySendDate;
  }

  public void setDelaySendDate(Long delaySendDate) {
    this.delaySendDate = delaySendDate;
  }



}
