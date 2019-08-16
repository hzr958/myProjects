package com.smate.web.psn.form;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.model.psninfo.PersonEmailRegister;
import com.smate.web.psn.model.setting.ConstMailType;
import com.smate.web.psn.model.setting.PsnMailSet;
import com.smate.web.psn.model.setting.UserSettings;

/**
 * 人员设置表单
 * 
 * @author aijiangbin
 * 
 */
public class PsnSettingForm {

  // 跳转到那个模块
  private String model;

  // 原密码
  private String oldpassword;
  // 新密码
  private String newpassword;
  // 确认新密码
  private String renewpassword;
  // 用户名
  private String username;
  // 消息
  private String msg;

  private String email;

  private Long mailId;
  private String des3MailId;
  // 用户设置
  private UserSettings userSettings = new UserSettings();
  private List<ConstMailType> constMailTypeList;
  // 加密的psnId集合，逗号分隔
  private String des3PsnIds;

  private PsnMailSet psnMailSet = new PsnMailSet();

  // email列表，显示时使用.
  private List<PersonEmailRegister> personEmailRegister;
  private List<PersonEmailInfo> personEmailInfoList;
  // 关注人员
  private List<AttPersonInfo> attPersonInfoList;
  private String des3RefPsnId;
  private Long attPersonId;
  private Boolean bindQQ = false; // 是否绑定qq
  private Boolean bindWX = false;// 是否绑定微信
  private Boolean bindMobile = false;// 是否绑定手机号
  private String mobileNum;// 手机号
  private String nickNameQQ;// qq昵称
  private String nickNameWC;// 微信昵称
  private Long psnId;
  private String des3PsnId;
  private String occupy; // QQ/WX被占用的提示

  // 取消订阅邮件
  private String psnid;
  private String typeid;
  private String cancleMark;
  private String mail;
  private String unsubscribeMailUrl;
  private String mobileNumber;
  private String ipCheck;// 1.手机验证 2. 只能邮箱验证

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public Boolean getBindMobile() {
    return bindMobile;
  }

  public void setBindMobile(Boolean bindMobile) {
    this.bindMobile = bindMobile;
  }

  public String getMobileNum() {
    return mobileNum;
  }

  public void setMobileNum(String mobileNum) {
    this.mobileNum = mobileNum;
  }

  // 取消订阅邮件
  public String getOldpassword() {
    return oldpassword;
  }

  public void setOldpassword(String oldpassword) {
    this.oldpassword = oldpassword;
  }

  public String getNewpassword() {
    return newpassword;
  }

  public void setNewpassword(String newpassword) {
    this.newpassword = newpassword;
  }

  public String getRenewpassword() {
    return renewpassword;
  }

  public void setRenewpassword(String renewpassword) {
    this.renewpassword = renewpassword;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Long getMailId() {
    return mailId;
  }

  public void setMailId(Long mailId) {
    if (mailId == null && StringUtils.isNotBlank(des3MailId)) {
      mailId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3MailId));
    }
    this.mailId = mailId;
  }

  public String getDes3MailId() {
    return des3MailId;
  }

  public void setDes3MailId(String des3MailId) {
    this.des3MailId = des3MailId;
  }

  public List<ConstMailType> getConstMailTypeList() {
    return constMailTypeList;
  }

  public void setConstMailTypeList(List<ConstMailType> constMailTypeList) {
    this.constMailTypeList = constMailTypeList;
  }

  public PsnMailSet getPsnMailSet() {
    return psnMailSet;
  }

  public void setPsnMailSet(PsnMailSet psnMailSet) {
    this.psnMailSet = psnMailSet;
  }

  public String getDes3PsnIds() {
    return des3PsnIds;
  }

  public void setDes3PsnIds(String des3PsnIds) {
    this.des3PsnIds = des3PsnIds;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public List<PersonEmailRegister> getPersonEmailRegister() {
    return personEmailRegister;
  }

  public void setPersonEmailRegister(List<PersonEmailRegister> personEmailRegister) {
    this.personEmailRegister = personEmailRegister;
  }

  public UserSettings getUserSettings() {
    return userSettings;
  }

  public void setUserSettings(UserSettings userSettings) {
    this.userSettings = userSettings;
  }

  public List<AttPersonInfo> getAttPersonInfoList() {
    return attPersonInfoList;
  }

  public void setAttPersonInfoList(List<AttPersonInfo> attPersonInfoList) {
    this.attPersonInfoList = attPersonInfoList;
  }

  public String getDes3RefPsnId() {
    return des3RefPsnId;
  }

  public void setDes3RefPsnId(String des3RefPsnId) {
    this.des3RefPsnId = des3RefPsnId;
  }

  public Long getAttPersonId() {
    return attPersonId;
  }

  public void setAttPersonId(Long attPersonId) {
    this.attPersonId = attPersonId;
  }

  public Boolean getBindQQ() {
    return bindQQ;
  }

  public void setBindQQ(Boolean bindQQ) {
    this.bindQQ = bindQQ;
  }

  public Boolean getBindWX() {
    return bindWX;
  }

  public void setBindWX(Boolean bindWX) {
    this.bindWX = bindWX;
  }

  public String getNickNameQQ() {
    return nickNameQQ;
  }

  public void setNickNameQQ(String nickNameQQ) {
    this.nickNameQQ = nickNameQQ;
  }

  public String getNickNameWC() {
    return nickNameWC;
  }

  public void setNickNameWC(String nickNameWC) {
    this.nickNameWC = nickNameWC;
  }

  public Long getPsnId() {
    if (psnId == null || psnId == 0L) {
      psnId = SecurityUtils.getCurrentUserId();
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    if (StringUtils.isBlank(des3PsnId)) {
      this.des3PsnId = Des3Utils.encodeToDes3(this.psnId.toString());
    }
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getOccupy() {
    return occupy;
  }

  public void setOccupy(String occupy) {
    this.occupy = occupy;
  }

  public String getPsnid() {
    return psnid;
  }

  public void setPsnid(String psnid) {
    this.psnid = psnid;
  }

  public String getTypeid() {
    return typeid;
  }

  public void setTypeid(String typeid) {
    this.typeid = typeid;
  }

  public String getCancleMark() {
    return cancleMark;
  }

  public void setCancleMark(String cancleMark) {
    this.cancleMark = cancleMark;
  }

  public String getMail() {
    return mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public List<PersonEmailInfo> getPersonEmailInfoList() {
    return personEmailInfoList;
  }

  public void setPersonEmailInfoList(List<PersonEmailInfo> personEmailInfoList) {
    this.personEmailInfoList = personEmailInfoList;
  }

  public String getUnsubscribeMailUrl() {
    return unsubscribeMailUrl;
  }

  public void setUnsubscribeMailUrl(String unsubscribeMailUrl) {
    this.unsubscribeMailUrl = unsubscribeMailUrl;
  }

  public String getIpCheck() {
    return ipCheck;
  }

  public void setIpCheck(String ipCheck) {
    this.ipCheck = ipCheck;
  }

}
