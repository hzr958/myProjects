package com.smate.center.open.model.profile;



import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.core.base.utils.string.ServiceUtil;



/**
 * 成果推荐读者.
 * 
 * @author WeiLong Peng
 * 
 */
public class ReaderCmd implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 6295067732274765050L;

  private Long psnId;
  private String des3PsnId;
  /** 头像地址. */
  private String avatars;
  private String zhName;
  private String enName;
  private String viewName;
  private String email;
  private String emailLanguageVersion;
  /** 首要工作单位. */
  private String primaryWorkUnit;
  /** 用户的第一个学科领域. */
  private String psnFirstDisc;
  /** 包含多个学科领域的字符串. */
  private String psnDiscStr;
  /** 读者头衔. */
  private String titolo;

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    if (psnId != null && des3PsnId == null) {
      des3PsnId = ServiceUtil.encodeToDes3(this.psnId.toString());
    }
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public String getViewName() {
    String locale = LocaleContextHolder.getLocale().toString();
    if ("zh_CN".equals(locale)) {
      this.viewName = StringUtils.isBlank(zhName) ? enName : zhName;
    } else {
      this.viewName = StringUtils.isBlank(enName) ? zhName : enName;
    }
    return viewName;
  }

  public void setViewName(String viewName) {
    this.viewName = viewName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmailLanguageVersion() {
    return emailLanguageVersion;
  }

  public void setEmailLanguageVersion(String emailLanguageVersion) {
    this.emailLanguageVersion = emailLanguageVersion;
  }

  public String getPrimaryWorkUnit() {
    return primaryWorkUnit;
  }

  public void setPrimaryWorkUnit(String primaryWorkUnit) {
    this.primaryWorkUnit = primaryWorkUnit;
  }

  public String getPsnFirstDisc() {
    return psnFirstDisc;
  }

  public void setPsnFirstDisc(String psnFirstDisc) {
    this.psnFirstDisc = psnFirstDisc;
  }

  public String getPsnDiscStr() {
    return psnDiscStr;
  }

  public void setPsnDiscStr(String psnDiscStr) {
    this.psnDiscStr = psnDiscStr;
  }

  public String getTitolo() {
    return titolo;
  }

  public void setTitolo(String titolo) {
    this.titolo = titolo;
  }

}
