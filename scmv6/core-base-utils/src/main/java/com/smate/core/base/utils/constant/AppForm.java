package com.smate.core.base.utils.constant;

import java.io.Serializable;
import java.util.Map;

/**
 * app基础信息
 * 
 * @author zzx
 *
 */
public class AppForm implements Serializable {
  private static final long serialVersionUID = 1L;
  private String appMsg = "操作成功";
  private String appStatus = IOSHttpStatus.OK;
  private Integer appTotal = 0;
  private Object data;
  private String errorDesc;
  private String actionKey;
  private String action;
  private String version;
  private String token;
  private Long currentPsnId;
  private boolean authority = false;
  private Map<String, Object> param;

  public String getAppMsg() {
    return appMsg;
  }

  public void setAppMsg(String appMsg) {
    this.appMsg = appMsg;
  }

  public String getAppStatus() {
    return appStatus;
  }

  public void setAppStatus(String appStatus) {
    this.appStatus = appStatus;
  }

  public Integer getAppTotal() {
    return appTotal;
  }

  public void setAppTotal(Integer appTotal) {
    this.appTotal = appTotal;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public String getErrorDesc() {
    return errorDesc;
  }

  public void setErrorDesc(String errorDesc) {
    this.errorDesc = errorDesc;
  }

  public String getActionKey() {
    return actionKey;
  }

  public void setActionKey(String actionKey) {
    this.actionKey = actionKey;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Long getCurrentPsnId() {
    return currentPsnId;
  }

  public void setCurrentPsnId(Long currentPsnId) {
    this.currentPsnId = currentPsnId;
  }

  public boolean isAuthority() {
    return authority;
  }

  public void setAuthority(boolean authority) {
    this.authority = authority;
  }

  public Map<String, Object> getParam() {
    return param;
  }

  public void setParam(Map<String, Object> param) {
    this.param = param;
  }

}
