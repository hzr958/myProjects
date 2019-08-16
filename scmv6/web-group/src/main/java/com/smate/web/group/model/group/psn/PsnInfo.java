package com.smate.web.group.model.group.psn;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.group.model.grp.member.PsnScienceArea;

/**
 * 人员信息对象
 * 
 * @author zk
 *
 */
public class PsnInfo implements Comparable<PsnInfo>, Serializable {

  private static final long serialVersionUID = -1166702514186218995L;
  // 数据来源
  private Person person; // 人员表
  // 人员统计信息
  private PsnStatistics psnStatistics; // 人员统计信息
  // 首要单位信息
  private Map<String, String> primaryIns; // 首要单位信息
                                          // primaryIns["INSID"] 单位id
                                          // primaryIns["INSNAME"]单位名称/学校名称
                                          // primaryIns["INSDPT"] 部门/专业
                                          // primaryIns["INSPOS"] 职称/学位

  // 数据显示
  private Long psnId; // 人员id
  private String des3PsnId; // 人员加密id
  private String name; // 人员显示姓名
  private String insInfo; // 首要单位信息及职称
  private String avatarUrl; // 人员头像地址
  private String profileUrl; // 人员站外地址
  private String insName;
  // 统计数据
  private Integer pubSum; // 成果数
  private Integer citedSum; // 引用数
  private Integer hindex; // H指数

  private Integer prjSum;// 项目数
  // 群组中的角色[1=创建人,2=管理员, 3=组员]
  private String groupRole = "3";
  private Long invitePsnId;// 群组与人员的关系ID
  private List<String> keyWords;// 人员关键词
  private Integer role;// 群组角色
  private Integer isFriend;// 是否是朋友，1=是；0=否
  private Integer isSetAdmin;// 是否设置管理员 1=设置管理员；2设置为普通成员；0=不显示
  private Integer isRemove;// 是否移除此人 1 =显示；2=不显示
  private Integer isGrpOwner;// 是否能变更群组拥有者 1=可以，2=隐藏
  private Integer isGrpMember;// 是否是群组成员1=是；0=否
  private String psnShortUrl;
  private List<PsnDisciplineKey> psnDisciplineKeyList;
  private List<PsnScienceArea> psnScienceAreaList;
  private Integer isSelf; // 是否是本人 1=是，0=不是

  public PsnInfo() {
    super();
  }

  public PsnInfo(Person person) {
    super();
    this.person = person;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getInsInfo() {
    return insInfo;
  }

  public void setInsInfo(String insInfo) {
    this.insInfo = insInfo;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  @Override
  public int compareTo(PsnInfo psnInfo) {
    Person thisPsn = this.getPerson();
    Person comparePsn = psnInfo.getPerson();
    if (thisPsn != null) {
      return thisPsn.compareTo(comparePsn);
    } else if (comparePsn == null) {
      return -1;
    }
    return 0;
  }

  public PsnStatistics getPsnStatistics() {
    return psnStatistics;
  }

  public void setPsnStatistics(PsnStatistics psnStatistics) {
    this.psnStatistics = psnStatistics;
  }

  public Integer getPubSum() {
    return pubSum;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum;
  }

  public Integer getCitedSum() {
    return citedSum;
  }

  public void setCitedSum(Integer citedSum) {
    this.citedSum = citedSum;
  }

  public Integer getHindex() {
    return hindex;
  }

  public void setHindex(Integer hindex) {
    this.hindex = hindex;
  }

  public Map<String, String> getPrimaryIns() {
    return primaryIns;
  }

  public void setPrimaryIns(Map<String, String> primaryIns) {
    this.primaryIns = primaryIns;
  }

  public String getProfileUrl() {
    return profileUrl;
  }

  public void setProfileUrl(String profileUrl) {
    this.profileUrl = profileUrl;
  }

  public Integer getPrjSum() {
    return prjSum;
  }

  public void setPrjSum(Integer prjSum) {
    this.prjSum = prjSum;
  }

  public String getGroupRole() {
    return groupRole;
  }

  public void setGroupRole(String groupRole) {
    this.groupRole = groupRole;
  }

  public Long getInvitePsnId() {
    return invitePsnId;
  }

  public void setInvitePsnId(Long invitePsnId) {
    this.invitePsnId = invitePsnId;
  }

  public List<String> getKeyWords() {
    return keyWords;
  }

  public void setKeyWords(List<String> keyWords) {
    this.keyWords = keyWords;
  }

  public Integer getRole() {
    return role;
  }

  public void setRole(Integer role) {
    this.role = role;
  }

  public Integer getIsFriend() {
    return isFriend;
  }

  public void setIsFriend(Integer isFriend) {
    this.isFriend = isFriend;
  }

  public Integer getIsSetAdmin() {
    return isSetAdmin;
  }

  public void setIsSetAdmin(Integer isSetAdmin) {
    this.isSetAdmin = isSetAdmin;
  }

  public Integer getIsRemove() {
    return isRemove;
  }

  public void setIsRemove(Integer isRemove) {
    this.isRemove = isRemove;
  }

  public Integer getIsGrpOwner() {
    return isGrpOwner;
  }

  public void setIsGrpOwner(Integer isGrpOwner) {
    this.isGrpOwner = isGrpOwner;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public List<PsnDisciplineKey> getPsnDisciplineKeyList() {
    return psnDisciplineKeyList;
  }

  public void setPsnDisciplineKeyList(List<PsnDisciplineKey> psnDisciplineKeyList) {
    this.psnDisciplineKeyList = psnDisciplineKeyList;
  }

  public Integer getIsGrpMember() {
    return isGrpMember;
  }

  public void setIsGrpMember(Integer isGrpMember) {
    this.isGrpMember = isGrpMember;
  }

  public String getPsnShortUrl() {
    return psnShortUrl;
  }

  public void setPsnShortUrl(String psnShortUrl) {
    this.psnShortUrl = psnShortUrl;
  }

  public List<PsnScienceArea> getPsnScienceAreaList() {
    return psnScienceAreaList;
  }

  public void setPsnScienceAreaList(List<PsnScienceArea> psnScienceAreaList) {
    this.psnScienceAreaList = psnScienceAreaList;
  }

  public Integer getIsSelf() {
    return isSelf;
  }

  public void setIsSelf(Integer isSelf) {
    this.isSelf = isSelf;
  }

}
