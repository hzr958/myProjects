package com.smate.web.psn.model.homepage;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.project.model.Project;
import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.utils.constant.AppForm;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.psn.model.keyword.CategoryMapBase;
import com.smate.web.psn.model.keyword.PsnScienceArea;
import com.smate.web.psn.model.profile.PsnDiscScm;
import com.smate.web.psn.model.psncnf.PsnCnfBuild;

/**
 * 我的----主页用form
 *
 * @author wsn
 * @createTime 2017年3月9日 上午10:58:16
 *
 */
public class PersonProfileForm {

  private Long psnId; // 人员ID
  private String des3PsnId; // 加密的人员ID
  private String name; // 中文名
  private String firstName; // 英文姓
  private String lastName; // 英文名
  private String insName; // 单位名称
  private Long insId; // 单位ID
  private String department; // 部门名称
  private String position; // 职称
  private String titolo; // 头衔
  private Integer sex; // 性别
  private Long regionId; // 所在地区ID
  private String regionName; // 所在地区名称
  private String email; // 邮箱
  private String tel; // 电话
  private String degreeName; // 学位
  private String psnBriefDesc; // 个人简介信息
  private Long posId; // 职称ID
  private Integer posGrades; // 职称等级
  private boolean needStatistics; // 是否需要统计信息
  private Integer pubSum = 0; // 成果总数
  private Integer prjSum = 0; // 项目总数
  private Integer hIndex = 0; // hindex指数
  private Long readSum = 0L; // 阅读总数
  private Integer citedSum = 0; // 引用总数
  private Integer openPrjSum = 0;// 开放的项目数
  private Long downloadCount = 0L; // 资源下载总数
  private Person psnInfo; // 人员信息
  private List<PsnDisciplineKey> keywords; // 关键词
  private List<String> recommendKeywords; // 推荐关键词
  private List<String> psnKeywords; // 人员当前拥有的关键词
  private String psnKeywordsStr; // 人员当前拥有的关键词拼接的字符串
  private String keywordStr; // 所有关键词拼成的json字符串
  private Integer oneKeyWordId; // 一个关键词ID
  private List<PsnScienceArea> scienceAreaList; // 人员科技领域
  private PsnCnfBuild cnfBuild; // 各个模块配置信息
  private String zhPsnRegionInfo;// 人员所在地区---中文
  private String enPsnRegionInfo;// 人员所在地区---英文
  private String psnRegionInfo; // 人员所在地区------页面显示
  private String insAndDepInfo; // 单位和部门信息
  private String positionAndTitolo; // 职称和头衔信息
  private Integer showBriefConf; // 个人简介显示权限 1：公开， 0：隐私
  private Integer showWorkConf; // 工作经历显示权限 1：公开， 0：隐私
  private Integer showEduConf; // 教育经历显示权限 1：公开， 0：隐私
  private Integer showRepresentPubConf; // 代表性成果显示权限 1：公开， 0：隐私
  private Integer shwoRepresentPrjConf; // 代表性项目显示权限 1：公开， 0：隐私
  private Integer showScienceAreAndKeywordsConf; // 科技领域显示权限 1：公开， 0：隐私
  private Integer showContactInfoConf; // 联系信息显示权限 1：公开， 0：隐私
  private Long cnfAnyMode; // 模块开放权限，对应PSN_CONFIG_MOUDLE表的any_mod字段
  private List<Project> openPrjList; // 公开的项目List
  private List<Project> representPrjList; // 代表性项目List
  private Long cnfId; // 人员cnfId, psn_config的cnfId字段
  private String searchKey; // 查询的字符串
  private Page page = new Page(); // 封装的page对象，分页查询用
  private String representPrjIds; // 人员代表性项目Id拼接成的字符串
  private List<WorkHistory> workList; // 工作经历列表
  private Long workId; // 工作经历ID
  private String encodeWrokId; // 加密的工作经历ID
  private WorkHistory workHistory; // 工作经历
  private List<EducationHistory> eduList; // 教育经历列表
  private Long eduId; // 教育经历ID
  private String encodeEduId; // 加密的教育经历ID
  private EducationHistory eduHistory;
  private List<CategoryMapBase> categoryList; // 科技领域（固定的56个）
  private String scienceAreaIds; // 科技领域ID拼接字符串
  private Integer scienceAreaNum; // 科技领域最多选择数
  private Integer oneScienceAreaId; // 一个科技领域ID
  private Integer idenSum;// 科技领域认同数
  private boolean isMySelf; // 是否是本人
  private String module;// 返回到哪个模块 "pub":成果模块 "prj":项目模块 "influence":影响力模块
                        // "homepage":主页 (默认)
  private boolean isFriend; // 是否是好友
  private Map<String, Object> categoryMap; // 用科技领域构建成的map
  private Integer anyUser; // 查看权限
  private Map<Long, String> workSelect; // 可选择的工作经历
  private Map<Long, String> workAddr; // 工作经历所在地区
  private Long currentWorkId; // 人员当前使用的工作经历ID
  private String currentWorkStr; // 当前使用的工作经历单位、部门和名称拼接的字符串
  private String errorMsg; // 错误信息
  private Integer anyUserEmail; // 邮件信息查看权限
  private Integer anyUserTel; // 电话信息的查看权限
  private String avatars; // 头像地址
  private String psnProfileUrl; // 个人主页公开url
  private String showDialogPub;// “add”表示跳到个人成果的时候是打开添加成果的插件
  private boolean psnFirstEmail; // 首要邮件修改了
  private boolean editKeywords = false;// false表示不弹出关键词
  private String dbUrl; // 更新引用用到
  private boolean isLogin; // 是否已登录
  private Map<String, String> resultMap;
  private String oldShortUrl;// 旧的短地址
  private String newShortUrl;// 新的短地址
  private String jumpto;// 跳转到具体某处
  private String language; // 语言环境
  private String otherName;// 别名
  private String zhFirstName;// 中文的名
  private String zhLastName; // 中文的姓
  private String word; // 需要转成拼音的字
  private String wordType;// 要转换为拼音的字的类型(姓氏，名字，普通文字)，当时姓氏时要对多音字进行处理
  private List<PsnDiscScm> discList; // 人员分类
  private Long superRegionId;
  private String data;
  private boolean needArea = false; // 科技领域没填
  private boolean needKeywords = false; // 关键词没填
  private boolean needWorkEdu = false; // 教育经历和工作经历没填
  private Integer wordLength; // 字符长度
  private boolean PayAttention = false;// 已经关注true 没关注false
  private Long attentionId;// ATT_PERSON的主键ID
  private String opttype = "";// addpub=自动打开添加成果
  private String sortBy;// 成果、项目，进行检索导入、文件导入后，排序字段改为按修改时间 update
  private String frompage;
  private String isEdit;// 是否编辑过成果
  private String filterPubType; // 成果模块，选中成果类型。
  private AppForm appForm;
  private String dbCode; // 文献库code，获取用户别名时用
  private String areaStr;
  private String nowTime;
  private Long psnOpenId;// 科研号
  private String seoTitle;// 页面title不能用js拼接

  public String getIsEdit() {
    return isEdit;
  }

  public void setIsEdit(String isEdit) {
    this.isEdit = isEdit;
  }

  public PersonProfileForm() {
    super();
  }

  public String getJumpto() {
    return jumpto;
  }

  public void setJumpto(String jumpto) {
    this.jumpto = jumpto;
  }

  public Long getPsnId() {
    if (NumberUtils.isNullOrZero(psnId) && StringUtils.isNotBlank(des3PsnId)) {
      psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PsnId));
    }
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

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
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

  public String getTitolo() {
    return titolo;
  }

  public void setTitolo(String titolo) {
    this.titolo = titolo;
  }

  public Integer getSex() {
    return sex;
  }

  public void setSex(Integer sex) {
    this.sex = sex;
  }

  public Long getRegionId() {
    return regionId;
  }

  public void setRegionId(Long regionId) {
    this.regionId = regionId;
  }

  public String getRegionName() {
    return regionName;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public Person getPsnInfo() {
    return psnInfo;
  }

  public void setPsnInfo(Person psnInfo) {
    this.psnInfo = psnInfo;
  }

  public PsnCnfBuild getCnfBuild() {
    return cnfBuild;
  }

  public void setCnfBuild(PsnCnfBuild cnfBuild) {
    this.cnfBuild = cnfBuild;
  }

  public String getZhPsnRegionInfo() {
    return zhPsnRegionInfo;
  }

  public void setZhPsnRegionInfo(String zhPsnRegionInfo) {
    this.zhPsnRegionInfo = zhPsnRegionInfo;
  }

  public String getEnPsnRegionInfo() {
    return enPsnRegionInfo;
  }

  public void setEnPsnRegionInfo(String enPsnRegionInfo) {
    this.enPsnRegionInfo = enPsnRegionInfo;
  }

  public String getInsAndDepInfo() {
    return insAndDepInfo;
  }

  public void setInsAndDepInfo(String insAndDepInfo) {
    this.insAndDepInfo = insAndDepInfo;
  }

  public String getPositionAndTitolo() {
    return positionAndTitolo;
  }

  public void setPositionAndTitolo(String positionAndTitolo) {
    this.positionAndTitolo = positionAndTitolo;
  }

  public Integer getShowBriefConf() {
    return showBriefConf;
  }

  public void setShowBriefConf(Integer showBriefConf) {
    this.showBriefConf = showBriefConf;
  }

  public Integer getShowWorkConf() {
    return showWorkConf;
  }

  public void setShowWorkConf(Integer showWorkConf) {
    this.showWorkConf = showWorkConf;
  }

  public Integer getShowEduConf() {
    return showEduConf;
  }

  public void setShowEduConf(Integer showEduConf) {
    this.showEduConf = showEduConf;
  }

  public Integer getShowRepresentPubConf() {
    return showRepresentPubConf;
  }

  public void setShowRepresentPubConf(Integer showRepresentPubConf) {
    this.showRepresentPubConf = showRepresentPubConf;
  }

  public Integer getShwoRepresentPrjConf() {
    return shwoRepresentPrjConf;
  }

  public void setShwoRepresentPrjConf(Integer shwoRepresentPrjConf) {
    this.shwoRepresentPrjConf = shwoRepresentPrjConf;
  }

  public Integer getShowScienceAreAndKeywordsConf() {
    return showScienceAreAndKeywordsConf;
  }

  public void setShowScienceAreAndKeywordsConf(Integer showScienceAreAndKeywordsConf) {
    this.showScienceAreAndKeywordsConf = showScienceAreAndKeywordsConf;
  }

  public Integer getShowContactInfoConf() {
    return showContactInfoConf;
  }

  public void setShowContactInfoConf(Integer showContactInfoConf) {
    this.showContactInfoConf = showContactInfoConf;
  }

  public Long getCnfAnyMode() {
    return cnfAnyMode;
  }

  public void setCnfAnyMode(Long cnfAnyMode) {
    this.cnfAnyMode = cnfAnyMode;
  }

  public String getPsnBriefDesc() {
    return psnBriefDesc;
  }

  public void setPsnBriefDesc(String psnBriefDesc) {
    this.psnBriefDesc = psnBriefDesc;
  }

  public boolean getNeedStatistics() {
    return needStatistics;
  }

  public void setNeedStatistics(boolean needStatistics) {
    this.needStatistics = needStatistics;
  }

  public Integer getPubSum() {
    return pubSum;
  }

  public void setPubSum(Integer pubSum) {
    this.pubSum = pubSum;
  }

  public Integer getPrjSum() {
    return prjSum;
  }

  public void setPrjSum(Integer prjSum) {
    this.prjSum = prjSum;
  }

  public Integer gethIndex() {
    return hIndex;
  }

  public void sethIndex(Integer hIndex) {
    this.hIndex = hIndex;
  }

  public Long getReadSum() {
    return readSum;
  }

  public void setReadSum(Long readSum) {
    this.readSum = readSum;
  }

  public Integer getCitedSum() {
    return citedSum;
  }

  public void setCitedSum(Integer citedSum) {
    this.citedSum = citedSum;
  }

  public List<PsnDisciplineKey> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<PsnDisciplineKey> keywords) {
    this.keywords = keywords;
  }

  public List<String> getRecommendKeywords() {
    return recommendKeywords;
  }

  public void setRecommendKeywords(List<String> recommendKeywords) {
    this.recommendKeywords = recommendKeywords;
  }

  public String getKeywordStr() {
    return keywordStr;
  }

  public void setKeywordStr(String keywordStr) {
    this.keywordStr = keywordStr;
  }

  public List<String> getPsnKeywords() {
    return psnKeywords;
  }

  public void setPsnKeywords(List<String> psnKeywords) {
    this.psnKeywords = psnKeywords;
  }

  public String getPsnKeywordsStr() {
    return psnKeywordsStr;
  }

  public void setPsnKeywordsStr(String psnKeywordsStr) {
    this.psnKeywordsStr = psnKeywordsStr;
  }

  public List<PsnScienceArea> getScienceAreaList() {
    return scienceAreaList;
  }

  public void setScienceAreaList(List<PsnScienceArea> scienceAreaList) {
    this.scienceAreaList = scienceAreaList;
  }

  public List<Project> getOpenPrjList() {
    return openPrjList;
  }

  public void setOpenPrjList(List<Project> openPrjList) {
    this.openPrjList = openPrjList;
  }

  public List<Project> getRepresentPrjList() {
    return representPrjList;
  }

  public void setRepresentPrjList(List<Project> representPrjList) {
    this.representPrjList = representPrjList;
  }

  public Long getCnfId() {
    return cnfId;
  }

  public void setCnfId(Long cnfId) {
    this.cnfId = cnfId;
  }

  /**
   * @return the searchKey
   */
  public String getSearchKey() {
    return searchKey;
  }

  /**
   * @param searchKey the searchKey to set
   */
  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public String getRepresentPrjIds() {
    return representPrjIds;
  }

  public void setRepresentPrjIds(String representPrjIds) {
    this.representPrjIds = representPrjIds;
  }

  public List<WorkHistory> getWorkList() {
    return workList;
  }

  public void setWorkList(List<WorkHistory> workList) {
    this.workList = workList;
  }

  public Long getWorkId() {
    return workId;
  }

  public void setWorkId(Long workId) {
    this.workId = workId;
  }

  public String getEncodeWrokId() {
    if (StringUtils.isBlank(encodeWrokId) && this.workId != null) {
      encodeWrokId = Des3Utils.encodeToDes3(encodeWrokId);
    }
    return encodeWrokId;
  }

  public void setEncodeWrokId(String encodeWrokId) {
    this.encodeWrokId = encodeWrokId;
  }

  public WorkHistory getWorkHistory() {
    return workHistory;
  }

  public void setWorkHistory(WorkHistory workHistory) {
    this.workHistory = workHistory;
  }

  public List<EducationHistory> getEduList() {
    return eduList;
  }

  public void setEduList(List<EducationHistory> eduList) {
    this.eduList = eduList;
  }

  public Long getEduId() {
    return eduId;
  }

  public void setEduId(Long eduId) {
    this.eduId = eduId;
  }

  public String getEncodeEduId() {
    return encodeEduId;
  }

  public void setEncodeEduId(String encodeEduId) {
    this.encodeEduId = encodeEduId;
  }

  public EducationHistory getEduHistory() {
    return eduHistory;
  }

  public void setEduHistory(EducationHistory eduHistory) {
    this.eduHistory = eduHistory;
  }

  public Long getPosId() {
    return posId;
  }

  public void setPosId(Long posId) {
    this.posId = posId;
  }

  public Integer getPosGrades() {
    return posGrades;
  }

  public void setPosGrades(Integer posGrades) {
    this.posGrades = posGrades;
  }

  public String getDegreeName() {
    return degreeName;
  }

  public void setDegreeName(String degreeName) {
    this.degreeName = degreeName;
  }

  public List<CategoryMapBase> getCategoryList() {
    return categoryList;
  }

  public void setCategoryList(List<CategoryMapBase> categoryList) {
    this.categoryList = categoryList;
  }

  public String getScienceAreaIds() {
    return scienceAreaIds;
  }

  public void setScienceAreaIds(String scienceAreaIds) {
    this.scienceAreaIds = scienceAreaIds;
  }

  public Integer getOneScienceAreaId() {
    return oneScienceAreaId;
  }

  public void setOneScienceAreaId(Integer oneScienceAreaId) {
    this.oneScienceAreaId = oneScienceAreaId;
  }

  public Integer getIdenSum() {
    return idenSum;
  }

  public void setIdenSum(Integer idenSum) {
    this.idenSum = idenSum;
  }

  public boolean getIsMySelf() {
    return isMySelf;
  }

  public void setIsMySelf(boolean isMySelf) {
    this.isMySelf = isMySelf;
  }

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public boolean getIsFriend() {
    return isFriend;
  }

  public void setIsFriend(boolean isFriend) {
    this.isFriend = isFriend;
  }

  public Map<String, Object> getCategoryMap() {
    return categoryMap;
  }

  public void setCategoryMap(Map<String, Object> categoryMap) {
    this.categoryMap = categoryMap;
  }

  public Integer getAnyUser() {
    return anyUser;
  }

  public void setAnyUser(Integer anyUser) {
    this.anyUser = anyUser;
  }

  public Map<Long, String> getWorkSelect() {
    return workSelect;
  }

  public void setWorkSelect(Map<Long, String> workSelect) {
    this.workSelect = workSelect;
  }

  public Map<Long, String> getWorkAddr() {
    return workAddr;
  }

  public void setWorkAddr(Map<Long, String> workAddr) {
    this.workAddr = workAddr;
  }

  public Long getCurrentWorkId() {
    return currentWorkId;
  }

  public void setCurrentWorkId(Long currentWorkId) {
    this.currentWorkId = currentWorkId;
  }

  public String getCurrentWorkStr() {
    return currentWorkStr;
  }

  public void setCurrentWorkStr(String currentWorkStr) {
    this.currentWorkStr = currentWorkStr;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public Integer getAnyUserEmail() {
    return anyUserEmail;
  }

  public void setAnyUserEmail(Integer anyUserEmail) {
    this.anyUserEmail = anyUserEmail;
  }

  public Integer getAnyUserTel() {
    return anyUserTel;
  }

  public void setAnyUserTel(Integer anyUserTel) {
    this.anyUserTel = anyUserTel;
  }

  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  public String getPsnProfileUrl() {
    return psnProfileUrl;
  }

  public void setPsnProfileUrl(String psnProfileUrl) {
    this.psnProfileUrl = psnProfileUrl;
  }

  public String getShowDialogPub() {
    return showDialogPub;
  }

  public void setShowDialogPub(String showDialogPub) {
    this.showDialogPub = showDialogPub;
  }

  public boolean getPsnFirstEmail() {
    return psnFirstEmail;
  }

  public void setPsnFirstEmail(boolean psnFirstEmail) {
    this.psnFirstEmail = psnFirstEmail;
  }

  public boolean getEditKeywords() {
    return editKeywords;
  }

  public void setEditKeywords(boolean editKeywords) {
    this.editKeywords = editKeywords;
  }

  public String getDbUrl() {
    return dbUrl;
  }

  public void setDbUrl(String dbUrl) {
    this.dbUrl = dbUrl;
  }

  public boolean getIsLogin() {
    return isLogin;
  }

  public void setIsLogin(boolean isLogin) {
    this.isLogin = isLogin;
  }

  public Map<String, String> getResultMap() {
    return resultMap;
  }

  public void setResultMap(Map<String, String> resultMap) {
    this.resultMap = resultMap;
  }

  public String getOldShortUrl() {
    return oldShortUrl;
  }

  public void setOldShortUrl(String oldShortUrl) {
    this.oldShortUrl = oldShortUrl;
  }

  public String getNewShortUrl() {
    return newShortUrl;
  }

  public void setNewShortUrl(String newShortUrl) {
    this.newShortUrl = newShortUrl;
  }

  public String getPsnRegionInfo() {
    return psnRegionInfo;
  }

  public void setPsnRegionInfo(String psnRegionInfo) {
    this.psnRegionInfo = psnRegionInfo;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getOtherName() {
    return otherName;
  }

  public void setOtherName(String otherName) {
    this.otherName = otherName;
  }

  public String getZhFirstName() {
    return zhFirstName;
  }

  public void setZhFirstName(String zhFirstName) {
    this.zhFirstName = zhFirstName;
  }

  public String getZhLastName() {
    return zhLastName;
  }

  public void setZhLastName(String zhLastName) {
    this.zhLastName = zhLastName;
  }

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public List<PsnDiscScm> getDiscList() {
    return discList;
  }

  public void setDiscList(List<PsnDiscScm> discList) {
    this.discList = discList;
  }

  public Long getSuperRegionId() {
    return superRegionId;
  }

  public void setSuperRegionId(Long superRegionId) {
    this.superRegionId = superRegionId;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public boolean getNeedArea() {
    return needArea;
  }

  public void setNeedArea(boolean needArea) {
    this.needArea = needArea;
  }

  public boolean getNeedKeywords() {
    return needKeywords;
  }

  public void setNeedKeywords(boolean needKeywords) {
    this.needKeywords = needKeywords;
  }

  public Long getDownloadCount() {
    return downloadCount;
  }

  public void setDownloadCount(Long downloadCount) {
    this.downloadCount = downloadCount;
  }

  public boolean isNeedWorkEdu() {
    return needWorkEdu;
  }

  public void setNeedWorkEdu(boolean needWorkEdu) {
    this.needWorkEdu = needWorkEdu;
  }

  public Integer getWordLength() {
    return wordLength;
  }

  public void setWordLength(Integer wordLength) {
    this.wordLength = wordLength;
  }

  public Boolean getPayAttention() {
    return PayAttention;
  }

  public void setPayAttention(Boolean payAttention) {
    PayAttention = payAttention;
  }

  public Long getAttentionId() {
    return attentionId;
  }

  public void setAttentionId(Long attentionId) {
    this.attentionId = attentionId;
  }

  public String getOpttype() {
    return opttype;
  }

  public void setOpttype(String opttype) {
    this.opttype = opttype;
  }

  public String getSortBy() {
    return sortBy;
  }

  public void setSortBy(String sortBy) {
    this.sortBy = sortBy;
  }

  public Integer getOneKeyWordId() {
    return oneKeyWordId;
  }

  public void setOneKeyWordId(Integer oneKeyWordId) {
    this.oneKeyWordId = oneKeyWordId;
  }

  public String getFrompage() {
    return frompage;
  }

  public void setFrompage(String frompage) {
    this.frompage = frompage;
  }

  public String getFilterPubType() {
    return filterPubType;
  }

  public void setFilterPubType(String filterPubType) {
    this.filterPubType = filterPubType;
  }

  public Integer getOpenPrjSum() {
    return openPrjSum;
  }

  public void setOpenPrjSum(Integer openPrjSum) {
    this.openPrjSum = openPrjSum;
  }

  public AppForm getAppForm() {
    return appForm;
  }

  public void setAppForm(AppForm appForm) {
    this.appForm = appForm;
  }

  public String getDbCode() {
    return dbCode;
  }

  public void setDbCode(String dbCode) {
    this.dbCode = dbCode;
  }

  public Integer getScienceAreaNum() {
    return scienceAreaNum;
  }

  public void setScienceAreaNum(Integer scienceAreaNum) {
    this.scienceAreaNum = scienceAreaNum;
  }

  public String getAreaStr() {
    return areaStr;
  }

  public void setAreaStr(String areaStr) {
    this.areaStr = areaStr;
  }

  public String getNowTime() {
    return nowTime;
  }

  public void setNowTime(String nowTime) {
    this.nowTime = nowTime;
  }

  public Long getPsnOpenId() {
    return psnOpenId;
  }

  public void setPsnOpenId(Long psnOpenId) {
    this.psnOpenId = psnOpenId;
  }

  public String getWordType() {
    return wordType;
  }

  public void setWordType(String wordType) {
    this.wordType = wordType;
  }

  public String getSeoTitle() {
    return seoTitle;
  }

  public void setSeoTitle(String seoTitle) {
    this.seoTitle = seoTitle;
  }

}
