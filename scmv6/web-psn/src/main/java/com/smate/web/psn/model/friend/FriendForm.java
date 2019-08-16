package com.smate.web.psn.model.friend;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.model.security.Person;
import com.smate.web.psn.model.psninfo.PsnInfo;

/**
 * 个人好友.
 * 
 * @author WSN
 * 
 */
public class FriendForm implements Serializable {

  private static final long serialVersionUID = 8406216618944570415L;

  // 用户ID
  private Long psnId;
  // 加密的用户ID
  private String des3PsnId;
  // 好友信息list
  private List<Person> psnList;
  // 页面显示的封装的好友信息list
  private List<PsnInfo> psnInfoList;
  // 人员按姓名排序分类好的Map
  private Map<String, List<PsnInfo>> psnMap;
  private String other; // 是否他们好友列表
  private boolean androidSys = false;// 是否是Android
  private String[] nameLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
      "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
  private String local; // 当前语言环境
  private String psnName; // 人员姓名
  private Integer pageNo = 1;
  private int permission = 0;// 0 表示所有可以看,1表示设置了隐私
  private String loginTargetUrl; // 超时登录，作为目标url跳回来的url(加密)
  private Integer hasLogin = 0; // 是否已登录，0：未登录，1：已登录
  private String des3Id; // 加密的人员ID

  public FriendForm() {
    super();
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public List<Person> getPsnList() {
    return psnList;
  }

  public void setPsnList(List<Person> psnList) {
    this.psnList = psnList;
  }

  public List<PsnInfo> getPsnInfoList() {
    return psnInfoList;
  }

  public void setPsnInfoList(List<PsnInfo> psnInfoList) {
    this.psnInfoList = psnInfoList;
  }

  public String[] getNameLetters() {
    return nameLetters;
  }

  public void setNameLetters(String[] nameLetters) {
    this.nameLetters = nameLetters;
  }

  public Map<String, List<PsnInfo>> getPsnMap() {
    return psnMap;
  }

  public void setPsnMap(Map<String, List<PsnInfo>> psnMap) {
    this.psnMap = psnMap;
  }

  public String getOther() {
    return other;
  }

  public void setOther(String other) {
    this.other = other;
  }

  public String getLocal() {
    return local;
  }

  public void setLocal(String local) {
    this.local = local;
  }

  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  public Integer getPageNo() {
    return pageNo;
  }

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;
  }

  public int getPermission() {
    return permission;
  }

  public void setPermission(int permission) {
    this.permission = permission;
  }

  public boolean isAndroidSys() {
    return androidSys;
  }

  public void setAndroidSys(boolean androidSys) {
    this.androidSys = androidSys;
  }

  public String getLoginTargetUrl() {
    return loginTargetUrl;
  }

  public void setLoginTargetUrl(String loginTargetUrl) {
    this.loginTargetUrl = loginTargetUrl;
  }

  public Integer getHasLogin() {
    return hasLogin;
  }

  public void setHasLogin(Integer hasLogin) {
    this.hasLogin = hasLogin;
  }

  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }



}
