package com.smate.center.oauth.form;

import java.io.Serializable;

/**
 * 操作会话所用form
 * 
 * @author wsn
 *
 */
public class SessionRefreshForm implements Serializable {

  private static final long serialVersionUID = -5367867609714321554L;
  private Integer deleteCache = 0; // 是否删除缓存的权限， 1：删除，0：不删除
  private Integer sessionInvalidate = 0; // 是否让会话失效， 1：是，0：不失效
  private Integer clearContext = 0; // 是否清空Spring Security中权限信息, 1:清空，0：不清空
  private Integer clearThreadInfo = 0; // 是否清空线程中的信息， 1：清空， 0：不清空
  private Integer deleteCookieAID = 0; // 是否删除cookie中的AID, 1: 是， 0：否
  private Integer deleteCookieOauthLogin = 0; // 是否删除cookie中的oauth_login参数，
  // 1：是， 0：否
  private Integer deleteFundCache = 0;// 是否删除资助机构左侧统计数缓存， 1：删除，0：不删除

  public SessionRefreshForm() {
    super();
  }

  public Integer getDeleteCache() {
    return deleteCache;
  }

  public void setDeleteCache(Integer deleteCache) {
    this.deleteCache = deleteCache;
  }

  public Integer getSessionInvalidate() {
    return sessionInvalidate;
  }

  public void setSessionInvalidate(Integer sessionInvalidate) {
    this.sessionInvalidate = sessionInvalidate;
  }

  public Integer getClearContext() {
    return clearContext;
  }

  public void setClearContext(Integer clearContext) {
    this.clearContext = clearContext;
  }

  public Integer getClearThreadInfo() {
    return clearThreadInfo;
  }

  public void setClearThreadInfo(Integer clearThreadInfo) {
    this.clearThreadInfo = clearThreadInfo;
  }

  public Integer getDeleteCookieAID() {
    return deleteCookieAID;
  }

  public void setDeleteCookieAID(Integer deleteCookieAID) {
    this.deleteCookieAID = deleteCookieAID;
  }

  public Integer getDeleteCookieOauthLogin() {
    return deleteCookieOauthLogin;
  }

  public void setDeleteCookieOauthLogin(Integer deleteCookieOauthLogin) {
    this.deleteCookieOauthLogin = deleteCookieOauthLogin;
  }

  public Integer getDeleteFundCache() {
    return deleteFundCache;
  }

  public void setDeleteFundCache(Integer deleteFundCache) {
    this.deleteFundCache = deleteFundCache;
  }

}
