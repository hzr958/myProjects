package com.smate.center.batch.context;

import java.util.List;
import java.util.Locale;

import com.smate.center.batch.model.sns.prj.OpenPrjMember;
import com.smate.center.batch.model.sns.prj.OpenProject;
import com.smate.center.batch.oldXml.prj.PrjXmlDocument;

public class OpenProjectContext {

  /**
   * 当前目标ID.
   */
  private Long currentPrjId;
  /**
   * 当前语言.
   */
  private String currentLanguage;
  /**
   * 当前用户ID.
   */
  private Long currentUserId;
  /**
   * 当前语言.
   */
  private Locale locale;
  /**
   * 项目对象
   */
  private OpenProject openProject;
  private Long openId;
  private String token;
  private PrjXmlDocument xmlDocument;
  private List<OpenPrjMember> openPrjMembers;

  public String getCurrentLanguage() {
    return currentLanguage;
  }

  public void setCurrentLanguage(String currentLanguage) {
    this.currentLanguage = currentLanguage;
  }

  public Long getCurrentUserId() {
    return currentUserId;
  }

  public void setCurrentUserId(Long currentUserId) {
    this.currentUserId = currentUserId;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public OpenProject getOpenProject() {
    return openProject;
  }

  public void setOpenProject(OpenProject openProject) {
    this.openProject = openProject;
  }

  public Long getOpenId() {
    return openId;
  }

  public void setOpenId(Long openId) {
    this.openId = openId;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public PrjXmlDocument getXmlDocument() {
    return xmlDocument;
  }

  public void setXmlDocument(PrjXmlDocument xmlDocument) {
    this.xmlDocument = xmlDocument;
  }

  public List<OpenPrjMember> getOpenPrjMembers() {
    return openPrjMembers;
  }

  public void setOpenPrjMembers(List<OpenPrjMember> openPrjMembers) {
    this.openPrjMembers = openPrjMembers;
  }

  public Long getCurrentPrjId() {
    return currentPrjId;
  }

  public void setCurrentPrjId(Long currentPrjId) {
    this.currentPrjId = currentPrjId;
  }



}
