package com.smate.core.base.utils.service.consts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 系统域名常量类
 * 
 * @author zk
 * @since 6.0.1
 */
@Component("sysDomainConst")
public class SysDomainConst {

  @Value("${domainscm}")
  private String snsDomain; // 科研之友域名
  @Value("${domaincas}")
  private String casDomain; // cas域名地址
  @Value("${domainrol}")
  private String rolDomain; // sie/rol域名
  @Value("${snsContext}")
  private String snsContext; // 科研之友上下文
  @Value("${rolContext}")
  private String rolContext; // sie/rol上下文
  @Value("${domainoauth}")
  private String oauthDomain; // 认证中心域名
  @Value("${domainMobile}")
  private String domainMobile; // 移动端域名

  public String getSnsContext() {
    return snsContext;
  }

  public void setSnsContext(String snsContext) {
    this.snsContext = snsContext;
  }

  public String getSnsDomain() {
    return snsDomain;
  }

  public void setSnsDomain(String snsDomain) {
    this.snsDomain = snsDomain;
  }

  public String getCasDomain() {
    return casDomain;
  }

  public void setCasDomain(String casDomain) {
    this.casDomain = casDomain;
  }

  public String getRolDomain() {
    return rolDomain;
  }

  public void setRolDomain(String rolDomain) {
    this.rolDomain = rolDomain;
  }

  public String getRolContext() {
    return rolContext;
  }

  public void setRolContext(String rolContext) {
    this.rolContext = rolContext;
  }

  public String getOauthDomain() {
    return oauthDomain;
  }

  public void setOauthDomain(String oauthDomain) {
    this.oauthDomain = oauthDomain;
  }

  public String getDomainMobile() {
    return domainMobile;
  }

  public void setDomainMobile(String domainMobile) {
    this.domainMobile = domainMobile;
  }
}
