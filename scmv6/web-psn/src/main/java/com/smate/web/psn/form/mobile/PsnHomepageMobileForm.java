package com.smate.web.psn.form.mobile;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.project.model.Project;
import com.smate.core.base.psn.dto.profile.KeywordIdentificationDTO;
import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.psn.model.keyword.CategoryMapBaseInfo;
import com.smate.web.psn.model.keyword.PsnKeyword;
import com.smate.web.psn.model.keyword.PsnScienceAreaInfo;
import com.smate.web.psn.model.profile.PsnScienceAreaForm;
import com.smate.web.psn.model.psncnf.PsnCnfBuild;
import com.smate.web.psn.model.psninfo.PsnInfo;

/**
 * 移动端 个人主页 form对象
 * 
 * @author tsz
 */
/**
 * @author Administrator
 *
 */
public class PsnHomepageMobileForm {

  private Long psnId; // 当前用户PsnId
  private String des3PsnId; // 当前用户加密的PsnId
  private Long viewPsnId; // 查看别人的主页的psnId
  private String des3ViewPsnId; // 查看别人的主页的加密的psnId
  private Person person; // 当前用户的，一些基本信息
  private List<WorkHistory> psnWorkList; // 工作历史
  private List<EducationHistory> psnEduList; // 教育历史
  private List<PsnKeyword> psnKeywordList; // 关键词
  private PsnInfo psnInfo; // 人员信息操作类
  private boolean isMyself = false;// 判断是不是自己 true是自己
  private List<KeywordIdentificationDTO> keywordIdentificationForm; // 封装了关键词，等一些基本的信息
  private List<PsnScienceAreaForm> psnScienceAreaFormList; // 科研领域
  private PsnStatistics psnStatistics; // 关于 ， 赞 ，分享 ，评论等一些信息
  private String domain; // 当前域名
  private String outHomePage; // 是否 他人主页
  private PsnCnfBuild psnCnfBuild; // 个人主页相关信息 权限配置
  private Integer anyUser; // 查看人 于被查看人 关系
  private String showName; // 显示的人名
  private String positionAndInsName;
  private boolean wechatBrowser = false; // 是否是微信端内置浏览器
  private String loginTargetUrl; // 超时登录，作为目标url跳回来的url(加密)
  private Integer hasLogin = 0; // 是否已登录，0：未登录，1：已登录
  private List<Project> prjList; // 代表性项目
  private Integer isFriend = 0; // 是否是好友, 1:是，0：否
  private Long currentPsnId; // 当前人员ID
  private String des3CurrentPsnId; // 加密的当前人员ID
  private Integer payAttention = 0; // 是否已关注过， 0：未关注， 1：已关注
  private Long attentionId; // 关注的记录ID
  private List<PsnDisciplineKey> keywords;// 人员关键词
  private String module; // 跳转到对应的模块，pub ，和pc端功能相同
  private String showDialogPub; // 弹出对应的模块 ，比如成果认领
  private String filterPubType; // 过滤的成果类型，比如 专利 == 5
  private String psnAvatarsUrl; // 人员头像地址
  private List<CategoryMapBaseInfo> firstLevel; // 一级科技领域
  private List<CategoryMapBaseInfo> secondLevel; // 二级科技领域
  private String psnKeyStr; // 人员关键词
  private Integer isHomepageEdit = 0; // 是否是从主页触发的编辑动作, 0: 不是， 1：是
  private List<PsnScienceAreaInfo> areaList; // 人员科技领域
  private String tel; // 非好友查询的电话
  private String email;// 非好友查询的邮件
  private boolean picIsBase64;// 头像图片是否是Base64编码
  private String des3OtherPsnId;
  private String searchKey;// 查询的关键词
  private Integer keySize;// 提示的关键词数量
  private String mobileNumber;// 手机号
  private Long pdwhPubId;
  private String des3PdwhPubId;
  private Long oneKeyWordId; // 一个关键词ID
  private String[] encodeParams; // 为了页面取值或链接需要，进行编码后的参数

  private String addToRepresentPrjIds;// 添加的代表项目的id

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

  public Long getViewPsnId() {
    return viewPsnId;
  }

  public void setViewPsnId(Long viewPsnId) {
    this.viewPsnId = viewPsnId;
  }

  public String getDes3ViewPsnId() {
    return des3ViewPsnId;
  }

  public void setDes3ViewPsnId(String des3ViewPsnId) {
    this.des3ViewPsnId = des3ViewPsnId;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public List<WorkHistory> getPsnWorkList() {
    return psnWorkList;
  }

  public void setPsnWorkList(List<WorkHistory> psnWorkList) {
    this.psnWorkList = psnWorkList;
  }

  public List<EducationHistory> getPsnEduList() {
    return psnEduList;
  }

  public void setPsnEduList(List<EducationHistory> psnEduList) {
    this.psnEduList = psnEduList;
  }

  public List<PsnKeyword> getPsnKeywordList() {
    return psnKeywordList;
  }

  public void setPsnKeywordList(List<PsnKeyword> psnKeywordList) {
    this.psnKeywordList = psnKeywordList;
  }

  public PsnStatistics getPsnStatistics() {
    return psnStatistics;
  }

  public void setPsnStatistics(PsnStatistics psnStatistics) {
    this.psnStatistics = psnStatistics;
  }

  public List<KeywordIdentificationDTO> getKeywordIdentificationForm() {
    return keywordIdentificationForm;
  }

  public void setKeywordIdentificationForm(List<KeywordIdentificationDTO> keywordIdentificationForm) {
    this.keywordIdentificationForm = keywordIdentificationForm;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getOutHomePage() {
    return outHomePage;
  }

  public void setOutHomePage(String outHomePage) {
    this.outHomePage = outHomePage;
  }

  public PsnCnfBuild getPsnCnfBuild() {
    return psnCnfBuild;
  }

  public void setPsnCnfBuild(PsnCnfBuild psnCnfBuild) {
    this.psnCnfBuild = psnCnfBuild;
  }

  public Integer getAnyUser() {
    return anyUser;
  }

  public void setAnyUser(Integer anyUser) {
    this.anyUser = anyUser;
  }

  public boolean getIsMyself() {
    return isMyself;
  }

  public void setIsMyself(boolean isMyself) {
    this.isMyself = isMyself;
  }

  public String getShowName() {
    return showName;
  }

  public void setShowName(String showName) {
    this.showName = showName;
  }

  public boolean getWechatBrowser() {
    return wechatBrowser;
  }

  public void setWechatBrowser(boolean wechatBrowser) {
    this.wechatBrowser = wechatBrowser;
  }

  public List<PsnScienceAreaForm> getPsnScienceAreaFormList() {
    return psnScienceAreaFormList;
  }

  public void setPsnScienceAreaFormList(List<PsnScienceAreaForm> psnScienceAreaFormList) {
    this.psnScienceAreaFormList = psnScienceAreaFormList;
  }

  public String getPositionAndInsName() {
    return positionAndInsName;
  }

  public void setPositionAndInsName(String positionAndInsName) {
    this.positionAndInsName = positionAndInsName;
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

  public PsnInfo getPsnInfo() {
    return psnInfo;
  }

  public void setPsnInfo(PsnInfo psnInfo) {
    this.psnInfo = psnInfo;
  }

  public List<Project> getPrjList() {
    return prjList;
  }

  public void setPrjList(List<Project> prjList) {
    this.prjList = prjList;
  }

  public Integer getIsFriend() {
    return isFriend;
  }

  public void setIsFriend(Integer isFriend) {
    this.isFriend = isFriend;
  }

  public Long getCurrentPsnId() {
    return currentPsnId;
  }

  public void setCurrentPsnId(Long currentPsnId) {
    this.currentPsnId = currentPsnId;
  }

  public String getDes3CurrentPsnId() {
    return des3CurrentPsnId;
  }

  public void setDes3CurrentPsnId(String des3CurrentPsnId) {
    this.des3CurrentPsnId = des3CurrentPsnId;
  }

  public Integer getPayAttention() {
    return payAttention;
  }

  public void setPayAttention(Integer payAttention) {
    this.payAttention = payAttention;
  }

  public Long getAttentionId() {
    return attentionId;
  }

  public void setAttentionId(Long attentionId) {
    this.attentionId = attentionId;
  }

  public List<PsnDisciplineKey> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<PsnDisciplineKey> keywords) {
    this.keywords = keywords;
  }

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public String getShowDialogPub() {
    return showDialogPub;
  }

  public void setShowDialogPub(String showDialogPub) {
    this.showDialogPub = showDialogPub;
  }

  public String getFilterPubType() {
    return filterPubType;
  }

  public void setFilterPubType(String filterPubType) {
    this.filterPubType = filterPubType;
  }

  public String getPsnAvatarsUrl() {
    return psnAvatarsUrl;
  }

  public void setPsnAvatarsUrl(String psnAvatarsUrl) {
    this.psnAvatarsUrl = psnAvatarsUrl;
  }

  public List<CategoryMapBaseInfo> getFirstLevel() {
    return firstLevel;
  }

  public void setFirstLevel(List<CategoryMapBaseInfo> firstLevel) {
    this.firstLevel = firstLevel;
  }

  public List<CategoryMapBaseInfo> getSecondLevel() {
    return secondLevel;
  }

  public void setSecondLevel(List<CategoryMapBaseInfo> secondLevel) {
    this.secondLevel = secondLevel;
  }

  public String getPsnKeyStr() {
    return psnKeyStr;
  }

  public void setPsnKeyStr(String psnKeyStr) {
    this.psnKeyStr = psnKeyStr;
  }

  public Integer getIsHomepageEdit() {
    return isHomepageEdit;
  }

  public void setIsHomepageEdit(Integer isHomepageEdit) {
    this.isHomepageEdit = isHomepageEdit;
  }

  public List<PsnScienceAreaInfo> getAreaList() {
    return areaList;
  }

  public void setAreaList(List<PsnScienceAreaInfo> areaList) {
    this.areaList = areaList;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isPicIsBase64() {
    return picIsBase64;
  }

  public void setPicIsBase64(boolean picIsBase64) {
    this.picIsBase64 = picIsBase64;
  }

  public String getDes3OtherPsnId() {
    return des3OtherPsnId;
  }

  public void setDes3OtherPsnId(String des3OtherPsnId) {
    this.des3OtherPsnId = des3OtherPsnId;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public Integer getKeySize() {
    return keySize;
  }

  public void setKeySize(Integer keySize) {
    this.keySize = keySize;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getAddToRepresentPrjIds() {
    return addToRepresentPrjIds;
  }

  public void setAddToRepresentPrjIds(String addToRepresentPrjIds) {
    this.addToRepresentPrjIds = addToRepresentPrjIds;
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

  public Long getOneKeyWordId() {
    return oneKeyWordId;
  }

  public void setOneKeyWordId(Long oneKeyWordId) {
    this.oneKeyWordId = oneKeyWordId;
  }

  public String[] getEncodeParams() {
    return encodeParams;
  }

  public void setEncodeParams(String[] encodeParams) {
    this.encodeParams = encodeParams;
  }



}
