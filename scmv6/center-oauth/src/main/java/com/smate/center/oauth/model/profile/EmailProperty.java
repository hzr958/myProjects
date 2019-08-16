package com.smate.center.oauth.model.profile;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于临时封装拼凑email模版的属性
 * 
 * @author zzx
 *
 */
public class EmailProperty {
  // 首要邮件
  private String firsEmails;
  // 邮件标题
  private String subject;
  private String userName;
  // 邮件模版
  private String ftlTemplate;
  private String ftlSimpleTemplate;
  // 输入的邮件或帐号
  private String emailOrLogin;
  private String serverURL;
  // 获取当前时间
  private String gen;
  // 当前国际化类型
  private String languageVersion;
  // 封装模版ftlTemplate所需的属性
  private HashMap context;
  // 封装模版ftlSimpleTemplate所需的属性
  private Map<String, Object> simpleMap;


  public EmailProperty() {}

  public HashMap getContext() {
    return context;
  }

  public void setContext(HashMap context) {
    this.context = context;
  }

  public Map<String, Object> getSimpleMap() {
    return simpleMap;
  }

  public void setSimpleMap(Map<String, Object> simpleMap) {
    this.simpleMap = simpleMap;
  }

  public String getFirstEmails() {
    return firsEmails;
  }

  public void setFirstEmails(String firsEmails) {
    this.firsEmails = firsEmails;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getFtlTemplate() {
    return ftlTemplate;
  }

  public void setFtlTemplate(String ftlTemplate) {
    this.ftlTemplate = ftlTemplate;
  }

  public String getFtlSimpleTemplate() {
    return ftlSimpleTemplate;
  }

  public void setFtlSimpleTemplate(String ftlSimpleTemplate) {
    this.ftlSimpleTemplate = ftlSimpleTemplate;
  }

  public String getEmailOrLogin() {
    return emailOrLogin;
  }

  public void setEmailOrLogin(String emailOrLogin) {
    this.emailOrLogin = emailOrLogin;
  }

  public String getServerURL() {
    return serverURL;
  }

  public void setServerURL(String serverURL) {
    this.serverURL = serverURL;
  }

  public String getGen() {
    return gen;
  }

  public void setGen(String gen) {
    this.gen = gen;
  }

  public String getLanguageVersion() {
    return languageVersion;
  }

  public void setLanguageVersion(String languageVersion) {
    this.languageVersion = languageVersion;
  }

}
