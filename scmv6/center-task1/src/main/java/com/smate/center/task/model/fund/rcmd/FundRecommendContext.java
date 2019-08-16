package com.smate.center.task.model.fund.rcmd;

import java.io.Serializable;
import java.util.List;

import com.smate.center.task.model.sns.psn.PsnPrjGrant;
import com.smate.core.base.utils.model.security.Person;

/**
 * 基金推荐上下文对象<加分条件>.
 * 
 * @author liangguokeng
 * 
 */
public class FundRecommendContext implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8039232774815108403L;

  private Person person;
  private Integer age;
  private ConstFundCategory fund;
  private List<String> psnCodeList;
  private List<PsnPrjGrant> psnGrantList;
  private List<PsnPrjGrant> psnJoinList;
  private List<String> psnKeyList;
  private List<PsnPrjGrant> friendGrantList;// 好友获得基金
  private String scienceAreaZhNames; // 科技领域中文
  private String scienceAreaEnNames; // 科技领域英文
  private String agencyIds;
  private Integer grade; // 人员身份
  private String seniority; // 申请资格， 1：企业，2：科研机构, code1,code2形式
  private String scienceAreaIds; // 科技领域 code1,code2,code3形式

  public FundRecommendContext() {
    super();
  }

  public Person getPerson() {
    return person;
  }

  public ConstFundCategory getFund() {
    return fund;
  }

  public List<String> getPsnCodeList() {
    return psnCodeList;
  }

  public List<PsnPrjGrant> getPsnGrantList() {
    return psnGrantList;
  }

  public List<PsnPrjGrant> getPsnJoinList() {
    return psnJoinList;
  }

  public List<String> getPsnKeyList() {
    return psnKeyList;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public void setFund(ConstFundCategory fund) {
    this.fund = fund;
  }

  public void setPsnCodeList(List<String> psnCodeList) {
    this.psnCodeList = psnCodeList;
  }

  public void setPsnGrantList(List<PsnPrjGrant> psnGrantList) {
    this.psnGrantList = psnGrantList;
  }

  public void setPsnJoinList(List<PsnPrjGrant> psnJoinList) {
    this.psnJoinList = psnJoinList;
  }

  public void setPsnKeyList(List<String> psnKeyList) {
    this.psnKeyList = psnKeyList;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public List<PsnPrjGrant> getFriendGrantList() {
    return friendGrantList;
  }

  public void setFriendGrantList(List<PsnPrjGrant> friendGrantList) {
    this.friendGrantList = friendGrantList;
  }

  public String getScienceAreaZhNames() {
    return scienceAreaZhNames;
  }

  public void setScienceAreaZhNames(String scienceAreaZhNames) {
    this.scienceAreaZhNames = scienceAreaZhNames;
  }

  public String getScienceAreaEnNames() {
    return scienceAreaEnNames;
  }

  public void setScienceAreaEnNames(String scienceAreaEnNames) {
    this.scienceAreaEnNames = scienceAreaEnNames;
  }

  public String getAgencyIds() {
    return agencyIds;
  }

  public void setAgencyIds(String agencyIds) {
    this.agencyIds = agencyIds;
  }

  public Integer getGrade() {
    return grade;
  }

  public void setGrade(Integer grade) {
    this.grade = grade;
  }

  public String getSeniority() {
    return seniority;
  }

  public void setSeniority(String seniority) {
    this.seniority = seniority;
  }

  public String getScienceAreaIds() {
    return scienceAreaIds;
  }

  public void setScienceAreaIds(String scienceAreaIds) {
    this.scienceAreaIds = scienceAreaIds;
  }

}
