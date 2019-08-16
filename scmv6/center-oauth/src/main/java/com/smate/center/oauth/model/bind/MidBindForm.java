package com.smate.center.oauth.model.bind;

import java.util.HashMap;
import java.util.Map;

import com.smate.center.oauth.model.consts.MidBindConsts;
import com.smate.center.oauth.model.profile.PersonRegisterForm;

/**
 * 微信QQ绑定form
 * 
 * @author zzx
 *
 */
public class MidBindForm {
  private Map<String, String> resultMap;// 返回结果封装
  private String unionid;// 微信、QQ关联id
  private Long scmOpenId;// 科研之友openId
  private String accessToken;// 微信、QQ验证token
  private String userName;// 账号
  private String passwords;// 密码
  private String nickName = "";// 昵称
  private Long psnId;// 当前人员id
  private String des3PsnId;// 加密当前人员id
  private String lastName;// 名
  private String firstName;// 姓
  private PersonRegisterForm registerForm;// 注册form
  private String qqOpenId;// QQopenId
  private String midToken;// 移动端权限token
  private String wcOpenId;// 微信openid
  private String termspage;



  public Map<String, String> getResultMap() {
    return resultMap;
  }

  public void setResultMap(Map<String, String> resultMap) {
    this.resultMap = resultMap;
  }

  public String getUnionid() {
    return unionid;
  }

  public void setUnionid(String unionid) {
    this.unionid = unionid;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPasswords() {
    return passwords;
  }

  public void setPasswords(String passwords) {
    this.passwords = passwords;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public PersonRegisterForm getRegisterForm() {
    return registerForm;
  }

  public void setRegisterForm(PersonRegisterForm registerForm) {
    this.registerForm = registerForm;
  }

  public Long getScmOpenId() {
    return scmOpenId;
  }

  public void setScmOpenId(Long scmOpenId) {
    this.scmOpenId = scmOpenId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getQqOpenId() {
    return qqOpenId;
  }

  public void setQqOpenId(String qqOpenId) {
    this.qqOpenId = qqOpenId;
  }

  public String getMidToken() {
    return midToken;
  }

  public void setMidToken(String midToken) {
    this.midToken = midToken;
  }

  public String getWcOpenId() {
    return wcOpenId;
  }

  public void setWcOpenId(String wcOpenId) {
    this.wcOpenId = wcOpenId;
  }

  public Map<String, Object> getResult() {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, String> mapSub = this.getResultMap();
    if (mapSub != null) {
      map.put(MidBindConsts.STATUS, mapSub.get(MidBindConsts.STATUS));
      mapSub.remove(MidBindConsts.STATUS);
      if (MidBindConsts.ERROR.equals(mapSub.get(MidBindConsts.STATUS))) {
        map.put(MidBindConsts.MSG, mapSub.get(MidBindConsts.MSG));
        mapSub.remove(MidBindConsts.MSG);
      }
      map.put("results", mapSub);
    } else {
      map.put(MidBindConsts.STATUS, MidBindConsts.ERROR);
    }
    return map;
  }

  public String getTermspage() {
    return termspage;
  }

  public void setTermspage(String termspage) {
    this.termspage = termspage;
  }
}
