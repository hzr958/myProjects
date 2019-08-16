package com.smate.core.base.psn.dto.profile;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.psn.dto.PsnSendMail;
import com.smate.core.base.psn.model.profile.PsnDiscipline;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 个人专长.
 * 
 * @author zt
 * 
 */
public class Personal implements java.io.Serializable {
  private static final long serialVersionUID = -9029155593207934802L;

  private Long psnId;
  private String disIds;
  private String strDisc;
  // 加密Id
  private String des3Id;
  private List<PsnDiscipline> pdList;
  private List<PsnDisciplineKey> keyList;// lgk ,拆分学科领域与关键词
  // 研究领域关键词
  private List<KeywordIdentificationDTO> keywordList;

  private Person person;
  // 个人主页地址
  private String profileUrl;

  // 基本信息下方显示的关键词 需要控制长度。 tsz
  private List<PsnDisciplineKey> baseKeyList;
  // 是否有学科，更新档案完整度时使用.
  private boolean discExit = false;
  // 学科代码JSON
  private String disJSON;

  // 学科代码设置的权限
  private Integer permission;
  private PsnSendMail psnSendMail;// 邮件发送
  private Long pdwhPubId;
  private String des3PdwhPubId;

  public Long getPsnId() {
    return psnId;
  }

  public String getDisIds() {
    return disIds;
  }

  public boolean isDiscExit() {
    return discExit;
  }

  public Personal setPsnId(Long psnId) {
    this.psnId = psnId;
    return this;
  }

  public void setDisIds(String disIds) {
    this.disIds = disIds;
  }

  public void setDiscExit(boolean discExit) {
    this.discExit = discExit;
  }

  public List<PsnDiscipline> getPdList() {
    return pdList;
  }

  public String getDisJSON() {
    return disJSON;
  }

  public void setPdList(List<PsnDiscipline> pdList) {
    this.pdList = pdList;
  }

  public void setDisJSON(String disJSON) {
    this.disJSON = disJSON;
  }

  public String getStrDisc() {
    return strDisc;
  }

  public void setStrDisc(String strDisc) {
    this.strDisc = strDisc;
  }

  public List<PsnDisciplineKey> getKeyList() {
    return keyList;
  }

  public void setKeyList(List<PsnDisciplineKey> keyList) {
    this.keyList = keyList;
  }

  public Integer getPermission() {
    return permission;
  }

  public void setPermission(Integer permission) {
    this.permission = permission;
  }

  public List<KeywordIdentificationDTO> getKeywordList() {
    return keywordList;
  }

  public void setKeywordList(List<KeywordIdentificationDTO> keywordList) {
    this.keywordList = keywordList;
  }

  public List<PsnDisciplineKey> getBaseKeyList() {
    return baseKeyList;
  }

  public void setBaseKeyList(List<PsnDisciplineKey> baseKeyList) {
    this.baseKeyList = baseKeyList;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public String getProfileUrl() {
    return profileUrl;
  }

  public void setProfileUrl(String profileUrl) {
    this.profileUrl = profileUrl;
  }

  public String getDes3Id() {
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  public PsnSendMail getPsnSendMail() {
    if (psnSendMail == null) {
      psnSendMail = new PsnSendMail();
    }
    return psnSendMail;
  }

  public void setPsnSendMail(PsnSendMail psnSendMail) {
    this.psnSendMail = psnSendMail;
  }

  public Long getPdwhPubId() {
    if (pdwhPubId == null && StringUtils.isNotBlank(des3PdwhPubId)) {
      pdwhPubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PdwhPubId));
    }
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public String getDes3PdwhPubId() {
    return des3PdwhPubId;
  }

  public void setDes3PdwhPubId(String des3PdwhPubId) {
    this.des3PdwhPubId = des3PdwhPubId;
  }

}
