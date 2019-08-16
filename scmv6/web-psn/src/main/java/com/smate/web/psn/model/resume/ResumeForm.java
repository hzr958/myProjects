package com.smate.web.psn.model.resume;



import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.smate.core.base.psn.dto.profile.Personal;
import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.psn.model.StationFile;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.model.friend.FriendTempSys;
import com.smate.web.psn.model.personal.resume.PersonalResume;
import com.smate.web.psn.model.profile.PersonTaught;


/**
 * 设置公开信息actionForm.
 * 
 * @author liqinghua
 * 
 */
public class ResumeForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4423204299933107552L;

  // 模板
  private Long tmpId;
  // 设置Id
  private Long confId;
  // 设置的数据
  private String confData;
  // 是否开启
  private Integer enabled;
  // 公众用户设置公开信息可以自己设置URL
  private String urlPath;
  // 之前使用的URL.
  private String preUrlPath;
  // ID列表
  private String pubIds;
  private String prjIds;
  private String refIds;
  private String eduIds;
  private String workIds;
  private String fileIds;
  private Integer psnNodeId;
  // 职称
  private String position;
  // 机构
  private String orgation;
  // 部门
  private String department;
  // 院系
  private String insName;
  // 人员ID
  private Long psnId;
  private String des3PsnId;
  // 当前用户PSNID
  private Long cpsnId;

  private Long totalCount;
  private String des3CpsnId;
  // 配置实体
  private Config config;

  // 基本信息
  private String name;
  private String ename;
  private String firstName;
  private String lastName;
  private String titolo;
  private String address;// 国别+省+市
  private Integer sex;
  private String birthday;
  // 头像地址
  private String avatars;
  // 联系信息
  private String qqNo;
  private String mobile;
  private String tel;
  private String email;
  private String msnNo;
  private String skype;
  // 个人简介
  private String brief;
  // 所教课程
  private PersonTaught personTaught;
  // 好友留言
  private String dynMsg;

  // 研究领域
  private Personal personal;
  // 工作经历列表
  private List<WorkHistory> workHistoryList;
  // 教育经历列表
  private List<EducationHistory> eduHistoryList;
  // 模板风格
  private String formModel;
  private String forwardUrl;
  // 是否关注了此人
  private Long attId;
  // 分页
  private Integer pageNo;

  private long friendCount;
  private Integer vFrdListPemission;

  private Integer first;
  private Integer pageSize;
  private Integer listSize;
  private Integer isAll;
  private List<StationFile> fileList;
  private Page<StationFile> filePage;
  private List<FriendTempSys> friendTmpList;

  // 个人基本信息处 显示研究领域
  private String baseKeyStr;

  private String result;

  private Long userId;
  private String userName;
  // 是否显示好友列表
  private int viewPsnOfFriends;
  // 是否显示群组列表
  private int viewPsnGroups;
  // 我的公开简历
  private PersonalResume pcvResume;
  // 个人关键词列表
  private List<PsnDisciplineKey> psnKeyList;
  // 是否预览
  private Integer isPreview;// 1=是预览
  private String psnKeyword;// 人员关键词_MJG_SCM-4388.

  private Integer anyUser;

  private String regionShow;

  public Personal getPersonal() {
    return personal;
  }

  public void setPersonal(Personal personal) {
    this.personal = personal;
  }

  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }

  public Long getTmpId() {
    return tmpId;
  }

  public Long getConfId() {
    return confId;
  }

  public String getConfData() {
    return confData;
  }

  public Integer getEnabled() {
    return enabled;
  }

  public String getPubIds() {
    return pubIds;
  }

  public String getPrjIds() {
    return prjIds;
  }

  public String getRefIds() {
    return refIds;
  }

  /**
   * @return the attId
   */
  public Long getAttId() {
    return attId;
  }

  /**
   * @param attId the attId to set
   */
  public void setAttId(Long attId) {
    this.attId = attId;
  }

  public String getEduIds() {
    return eduIds;
  }

  public String getWorkIds() {
    return workIds;
  }

  public String getFileIds() {
    return fileIds;
  }

  public List<WorkHistory> getWorkHistoryList() {
    return workHistoryList;
  }

  public List<EducationHistory> getEduHistoryList() {
    return eduHistoryList;
  }

  public Long getPsnId() {
    return psnId;
  }

  public String getDes3PsnId() {
    if (this.psnId != null) {
      try {
        des3PsnId = URLEncoder.encode(ServiceUtil.encodeToDes3(this.psnId.toString()), "utf-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }

    }
    return des3PsnId;
  }

  public void setTmpId(Long tmpId) {
    this.tmpId = tmpId;
  }

  public void setConfId(Long confId) {
    this.confId = confId;
  }

  public void setConfData(String confData) {
    this.confData = confData;
  }

  public void setEnabled(Integer enabled) {
    this.enabled = enabled;
  }

  public void setPubIds(String pubIds) {
    this.pubIds = pubIds;
  }

  public void setPrjIds(String prjIds) {
    this.prjIds = prjIds;
  }

  public void setRefIds(String refIds) {
    this.refIds = refIds;
  }

  public void setEduIds(String eduIds) {
    this.eduIds = eduIds;
  }

  public void setWorkIds(String workIds) {
    this.workIds = workIds;
  }

  public void setFileIds(String fileIds) {
    this.fileIds = fileIds;
  }

  public void setWorkHistoryList(List<WorkHistory> workHistoryList) {
    this.workHistoryList = workHistoryList;
  }

  public void setEduHistoryList(List<EducationHistory> eduHistoryList) {
    this.eduHistoryList = eduHistoryList;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getCpsnId() {
    return cpsnId;
  }

  public String getDes3CpsnId() {
    return des3CpsnId;
  }

  public void setCpsnId(Long cpsnId) {
    this.cpsnId = cpsnId;
  }

  public void setDes3CpsnId(String des3CpsnId) {
    this.des3CpsnId = des3CpsnId;
  }

  public Config getConfig() {
    return config;
  }

  public void setConfig(Config config) {
    this.config = config;
  }

  public String getName() {
    return name;
  }

  public String getTitolo() {
    return titolo;
  }

  public String getAddress() {
    return address;
  }

  public Integer getSex() {
    return sex;
  }

  public String getQqNo() {
    return qqNo;
  }

  public String getMobile() {
    return mobile;
  }

  public String getTel() {
    return tel;
  }

  public String getEmail() {
    return email;
  }

  public String getMsnNo() {
    return msnNo;
  }

  public String getSkype() {
    return skype;
  }

  public String getBrief() {
    return brief;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setTitolo(String titolo) {
    this.titolo = titolo;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setSex(Integer sex) {
    this.sex = sex;
  }

  public void setQqNo(String qqNo) {
    this.qqNo = qqNo;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setMsnNo(String msnNo) {
    this.msnNo = msnNo;
  }

  public void setSkype(String skype) {
    this.skype = skype;
  }

  public void setBrief(String brief) {
    this.brief = brief;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEname() {
    return ename;
  }

  public void setEname(String ename) {
    this.ename = ename;
  }

  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getUrlPath() {
    return urlPath;
  }

  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  public void setUrlPath(String urlPath) {
    this.urlPath = urlPath;
  }

  public Integer getFirst() {
    return first;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public Integer getListSize() {
    return listSize;
  }

  public Integer getIsAll() {
    return isAll;
  }

  public void setFirst(Integer first) {
    this.first = first;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public void setListSize(Integer listSize) {
    this.listSize = listSize;
  }

  public void setIsAll(Integer isAll) {
    this.isAll = isAll;
  }

  public Long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
  }


  public Integer getPageNo() {
    return pageNo;
  }

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;
  }

  public List<StationFile> getFileList() {
    return fileList;
  }

  public void setFileList(List<StationFile> fileList) {
    this.fileList = fileList;
  }

  public String getFormModel() {
    return formModel;
  }

  public void setFormModel(String formModel) {
    this.formModel = formModel;
  }

  public String getDynMsg() {
    return dynMsg;
  }

  public void setDynMsg(String dynMsg) {
    this.dynMsg = dynMsg;
  }

  public Page<StationFile> getFilePage() {
    return filePage;
  }

  public void setFilePage(Page<StationFile> filePage) {
    this.filePage = filePage;
  }

  public List<FriendTempSys> getFriendTmpList() {
    return friendTmpList;
  }

  public void setFriendTmpList(List<FriendTempSys> friendTmpList) {
    this.friendTmpList = friendTmpList;
  }

  public long getFriendCount() {
    return friendCount;
  }

  public void setFriendCount(long friendCount) {
    this.friendCount = friendCount;
  }

  public Integer getVFrdListPemission() {
    return vFrdListPemission;
  }

  public void setVFrdListPemission(Integer vFrdListPemission) {
    this.vFrdListPemission = vFrdListPemission;
  }

  public Integer getPsnNodeId() {
    return psnNodeId;
  }

  public void setPsnNodeId(Integer psnNodeId) {
    this.psnNodeId = psnNodeId;
  }

  public Long getUserId() {
    userId = SecurityUtils.getCurrentUserId();
    return userId;
  }

  public String getPreUrlPath() {
    return preUrlPath;
  }

  public void setPreUrlPath(String preUrlPath) {
    this.preUrlPath = preUrlPath;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }



  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getOrgation() {
    return orgation;
  }

  public void setOrgation(String orgation) {
    this.orgation = orgation;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public int getViewPsnOfFriends() {
    return viewPsnOfFriends;
  }

  public void setViewPsnOfFriends(int viewPsnOfFriends) {
    this.viewPsnOfFriends = viewPsnOfFriends;
  }

  public int getViewPsnGroups() {
    return viewPsnGroups;
  }

  public void setViewPsnGroups(int viewPsnGroups) {
    this.viewPsnGroups = viewPsnGroups;
  }

  public PersonalResume getPcvResume() {
    return pcvResume;
  }

  public void setPcvResume(PersonalResume pcvResume) {
    this.pcvResume = pcvResume;
  }

  public PersonTaught getPersonTaught() {
    return personTaught;
  }

  public void setPersonTaught(PersonTaught personTaught) {
    this.personTaught = personTaught;
  }

  public List<PsnDisciplineKey> getPsnKeyList() {
    return psnKeyList;
  }

  public void setPsnKeyList(List<PsnDisciplineKey> psnKeyList) {
    this.psnKeyList = psnKeyList;
  }

  public Integer getIsPreview() {
    return isPreview;
  }

  public void setIsPreview(Integer isPreview) {
    this.isPreview = isPreview;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPsnKeyword() {
    return psnKeyword;
  }

  public void setPsnKeyword(String psnKeyword) {
    this.psnKeyword = psnKeyword;
  }

  public String getBaseKeyStr() {
    return baseKeyStr;
  }

  public void setBaseKeyStr(String baseKeyStr) {
    this.baseKeyStr = baseKeyStr;
  }

  /**
   * @return anyUser
   */
  public Integer getAnyUser() {
    return anyUser;
  }

  /**
   * @param anyUser 要设置的 anyUser
   */
  public void setAnyUser(Integer anyUser) {
    this.anyUser = anyUser;
  }

  /**
   * @return regionShow
   */
  public String getRegionShow() {
    return regionShow;
  }

  /**
   * @param regionShow 要设置的 regionShow
   */
  public void setRegionShow(String regionShow) {
    this.regionShow = regionShow;
  }

}
