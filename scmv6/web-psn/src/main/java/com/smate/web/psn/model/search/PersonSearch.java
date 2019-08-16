package com.smate.web.psn.model.search;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.web.psn.model.keyword.PsnScienceArea;

/**
 * 人员(检索).
 * 
 * @author xys
 *
 */
public class PersonSearch {

  private Long psnId;// 用户id
  private String des3PsnId;// 加密的用户id
  private String name;// 姓名
  private String position;// 职称
  private String titolo;// 头衔
  private String avatars;// 头像地址
  private String insName;// 机构
  private Integer prjSum; // 项目数
  private String department; // 部门名称
  private String scienceAreaStr; // 科技领域信息
  private List<PsnScienceArea> scienceList;// 科技领域信息
  private Integer pubSum; // 成果数
  private List<PsnDisciplineKey> discList;// 关键词
  private String insAndDepart; // 单位和部门
  private boolean needButton = true;// 是否显示添加好友按钮
  private Integer psnInfoIntegrity; // 个人信息完整度
  private String psnShortUrl; // 人员短地址
  private Long openId;// 科研号

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getAvatars() {
    if (StringUtils.isBlank(avatars)) {
      avatars = "/resmod/smate-pc/img/logo_psndefault.png";
    }
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public Integer getPrjSum() {
    return prjSum;
  }

  public void setPrjSum(Integer prjSum) {
    this.prjSum = prjSum;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getScienceAreaStr() {
    return scienceAreaStr;
  }

  public void setScienceAreaStr(String scienceAreaStr) {
    this.scienceAreaStr = scienceAreaStr;
  }

  public Integer getPubSum() {
    return pubSum;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum;
  }

  public List<PsnDisciplineKey> getDiscList() {
    return discList;
  }

  public void setDiscList(List<PsnDisciplineKey> discList) {
    this.discList = discList;
  }

  public String getInsAndDepart() {
    return insAndDepart;
  }

  public void setInsAndDepart(String insAndDepart) {
    this.insAndDepart = insAndDepart;
  }

  public boolean getNeedButton() {
    return needButton;
  }

  public void setNeedButton(boolean needButton) {
    this.needButton = needButton;
  }

  public List<PsnScienceArea> getScienceList() {
    return scienceList;
  }

  public void setScienceList(List<PsnScienceArea> scienceList) {
    this.scienceList = scienceList;
  }

  public Integer getPsnInfoIntegrity() {
    return psnInfoIntegrity;
  }

  public void setPsnInfoIntegrity(Integer psnInfoIntegrity) {
    this.psnInfoIntegrity = psnInfoIntegrity;
  }

  public String getPsnShortUrl() {
    return psnShortUrl;
  }

  public void setPsnShortUrl(String psnShortUrl) {
    this.psnShortUrl = psnShortUrl;
  }

  public String getTitolo() {
    return titolo;
  }

  public void setTitolo(String titolo) {
    this.titolo = titolo;
  }

  public Long getOpenId() {
    return openId;
  }

  public void setOpenId(Long openId) {
    this.openId = openId;
  }

}
