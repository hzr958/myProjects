package com.smate.core.base.psn.model.info;

import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.ServiceUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
  private String zhName; // 人员显示姓名
  private String EnName; // 人员显示姓名
  private String insInfo; // 首要单位信息及职称
  private String avatarUrl; // 人员头像地址
  private String profileUrl; // 人员站外地址
  private String insAndDep; // 人员单位和部门信息
  private String posAndTitolo; // 职称和头衔信息
  private List<PsnDisciplineKey> discList; // 人员关键词list
  private boolean isFriend; // 是否是好友
  private String psnShortUrl;

  // 统计数据
  private Integer pubSum; // 成果数
  private Integer citedSum; // 引用数
  private Integer hindex; // H指数
  private Integer prjSum; // 项目数
  private String department; // 部门名称
  private String insName; // 机构名称
  private String position; // 职称
  private String scienceAreaStr; // 科技领域信息

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
    if (psnId != null && psnId > 0L && StringUtils.isBlank(des3PsnId)) {
      des3PsnId = ServiceUtil.encodeToDes3(psnId.toString());
    }
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
    if (StringUtils.isBlank(avatarUrl)) {
      return "/resmod/smate-pc/img/logo_psndefault.png";
    }
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

  public String getInsAndDep() {
    return insAndDep;
  }

  public void setInsAndDep(String insAndDep) {
    this.insAndDep = insAndDep;
  }

  public String getPosAndTitolo() {
    return posAndTitolo;
  }

  public void setPosAndTitolo(String posAndTitolo) {
    this.posAndTitolo = posAndTitolo;
  }

  public List<PsnDisciplineKey> getDiscList() {
    return discList;
  }

  public void setDiscList(List<PsnDisciplineKey> discList) {
    this.discList = discList;
  }

  public Integer getPrjSum() {
    return prjSum;
  }

  public void setPrjSum(Integer prjSum) {
    this.prjSum = prjSum;
  }

  public boolean getIsFriend() {
    return isFriend;
  }

  public void setIsFriend(boolean isFriend) {
    this.isFriend = isFriend;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public String getEnName() {
    return EnName;
  }

  public void setEnName(String enName) {
    EnName = enName;
  }

  public String getPsnShortUrl() {
    return psnShortUrl;
  }

  public void setPsnShortUrl(String psnShortUrl) {
    this.psnShortUrl = psnShortUrl;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getScienceAreaStr() {
    return scienceAreaStr;
  }

  public void setScienceAreaStr(String scienceAreaStr) {
    this.scienceAreaStr = scienceAreaStr;
  }

}
