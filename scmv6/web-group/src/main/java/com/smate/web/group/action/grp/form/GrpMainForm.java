package com.smate.web.group.action.grp.form;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.group.model.group.pub.Discipline;
import com.smate.web.group.model.grp.grpbase.GrpBaseinfo;
import com.smate.web.group.model.grp.grpbase.GrpControl;
import com.smate.web.group.model.grp.member.CategoryMapBase;

public class GrpMainForm implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -3507121321809781832L;
  private Long psnId;// 当前人psnId
  private Long grpId;// 群组ID
  private String des3GrpId;
  private String groupCode; // demo 系统的
  private Long targetGrpId;
  private String targetdes3GrpId;
  @JsonIgnore
  private List<GrpShowInfo> grpShowInfoList;
  @JsonIgnore
  private List<Discipline> disciplineList;
  @JsonIgnore
  private GrpShowInfo grpShowInfo;
  private Integer searchByRole = 1;// 1=所有我的群组， 2=我管理的群组，3=我是普通成员的群组，4=待批准的群组
  private Integer grpCategory;// 群组分类 10:兴趣群组 ， 11项目群组
  private Integer firstResult;
  private Integer maxResults;
  @JsonIgnore
  private Map<String, String> resultMap;
  @JsonIgnore
  private Map<String, Object> result2Map;
  private Integer rcmdStatus = 0; // 0：推荐 1：申请 9 忽略
  @JsonIgnore
  private Page<GrpShowInfo> page = new Page<GrpShowInfo>();
  private Integer role;// 群组角色权限[1=创建人,2=管理员, 3=组员]
  private Integer setTopOpt;// 1=设置置顶；0=取消置顶
  private String isIndexDiscussOpen; // 主页是否显示群组动态 [1=是 ， 0=否 ] 默认1
  private String isIndexMemberOpen; // 主页是否显示群组成员 [1=是 ， 0=否 ] 默认1
  private String isIndexPubOpen; // 主页是否显示群组成果 [1=是 ， 0=否 ] 默认1
  private String isIndexFileOpen; // 主页是否显示群组文件 [1=是 ， 0=否 ] 默认1
  private String isCurwareFileShow; // 群组外成员是否显示项目成果 [1=是 ， 0=否 ] 默认1
  private String isWorkFileShow; // 群组外成员是否显示课件 [1=是 ， 0=否 ] 默认1
  private String isPrjPubShow; // 群组外成员是否显示课件 [1=是 ， 0=否 ] 默认1
  private String isPrjRefShow; // 群组外成员是否显示作业 [1=是 ， 0=否 ] 默认1
  private String grpName;// 群组名字
  private String grpDescription;// 群组简介
  private String grpKeywords;// 群组关键词串，seo使用
  private String projectNo;// 项目批准号、编号
  private String openType; // 群组开放类型 公开类型【O = 开放 ， H = 半开放 ， P=隐私】 默认 H
  private String disciplines; // 学科领域 ，逗号分隔 ， 记录是常量编码
  private String keywords; // 关键词 ，分号分隔
  private Integer firstCategoryId; // 学科 一级领域 ，常量
  private String firstDisciplinetName;
  private Integer secondCategoryId; // 学科 二级领域 ，，常量
  private String secondDisciplinetName;
  private Date date;
  private Long fCategoryId; // 学科 一级领域
  private String searchKey; // 检索关键字
  private Integer isAll = 0;// 个人主页-查看全部 1:是 0:否
  private Integer beforehand = 0;
  @JsonIgnore
  private List<GrpBaseinfo> grpBaseInfoList;
  private String ispending;// 是否是从群组列表点击未处理事项过来的 1=是 0=否
  private String model = "discess";// 优先显示模块 myGrp , rcmdGrp
  private String hasIvite;// 是否有被邀请的群组，1=是，0=否
  private String hasReqGrp = "0";// 是否有成员请求群组，1=是，0=否
  private Long grpCount;// 群组数量
  private String isCopyBaseinfo;// 是否复制群组基本信息，1=是，0=否
  private String isCopyGrpPubs;// 是否复制群组文献，1=是，0=否
  private String isCopyGrpCourseware;// 是否复制群组课件，1=是，0=否
  // 项目群组 //isCopyGrpCourseware
  private String isCopyProjectGrpPubs = "0";
  private String isCopyProjectGrpRefs = "0";
  // 课程群组 //isCopyGrpPubs
  private String isCopyCourseGrpCourseware = "0";
  private String isCopyCourseGrpWork = "0";
  private String oldShortUrl;// 旧的短地址
  private String newShortUrl;// 新的短地址
  private String grpAvartars; // 群组头像地址
  private Integer pageValue = 1; // 页码 , 默认为第一页
  private String des3FirstPsnId;// 成果最多的群组成员Id
  private Integer keywordsSize; // 提示关键词的数量
  private String jumpto;// 跳转到具体某处
  private String frompage;// 返回标志用来判断是否显示原来的查询条件
  private String scienceAreaIds;
  private List<CategoryMapBase> categoryList; // 科技领域（固定的56个）
  private Map<String, Object> categoryMap; // 用科技领域构建成的map
  private GrpControl grpControl;// 当前群组显示模块
  private String flag;// 是否展示群组首页动态内容
  private GrpBaseinfo grpBaseinfo;// 当前群组详情
  private String seoTitle;// 页面title不能用js拼接
  private String des3PsnId; // 加密的人员ID
  private boolean selectAll = true;// 是否查询全部
  private String isViewGrpDetail;// 查看群组详情页面 1是 0否
  private String viewIp;// ip

  public Map<String, Object> getCategoryMap() {
    return categoryMap;
  }

  public void setCategoryMap(Map<String, Object> categoryMap) {
    this.categoryMap = categoryMap;
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

  public Long getPsnId() {
    if (psnId == null || psnId == 0L) {
      psnId = SecurityUtils.getCurrentUserId();
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getGrpId() {
    if ((grpId == null || grpId == 0L) && StringUtils.isNotBlank(des3GrpId)) {
      grpId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3GrpId));
    }
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public Integer getSearchByRole() {
    return searchByRole;
  }

  public void setSearchByRole(Integer searchByRole) {
    this.searchByRole = searchByRole;
  }

  public Integer getFirstResult() {
    return firstResult;
  }

  public void setFirstResult(Integer firstResult) {
    this.firstResult = firstResult;
  }

  public Integer getMaxResults() {
    return maxResults;
  }

  public void setMaxResults(Integer maxResults) {
    this.maxResults = maxResults;
  }

  public GrpShowInfo getGrpShowInfo() {
    return grpShowInfo;
  }

  public void setGrpShowInfo(GrpShowInfo grpShowInfo) {
    this.grpShowInfo = grpShowInfo;
  }

  public List<GrpShowInfo> getGrpShowInfoList() {
    return grpShowInfoList;
  }

  public void setGrpShowInfoList(List<GrpShowInfo> grpShowInfoList) {
    this.grpShowInfoList = grpShowInfoList;
  }

  public Integer getGrpCategory() {
    return grpCategory;
  }

  public void setGrpCategory(Integer grpCategory) {
    this.grpCategory = grpCategory;
  }

  public Map<String, String> getResultMap() {
    return resultMap;
  }

  public void setResultMap(Map<String, String> resultMap) {
    this.resultMap = resultMap;
  }

  public Integer getRcmdStatus() {
    return rcmdStatus;
  }

  public void setRcmdStatus(Integer rcmdStatus) {
    this.rcmdStatus = rcmdStatus;
  }

  public Page getPage() {
    if (page == null) {
      page = new Page<GrpShowInfo>();
    }
    return page;
  }

  @JsonIgnore
  public void setPage(Page page) {
    this.page = page;
  }

  public Integer getRole() {
    return role;
  }

  public void setRole(Integer role) {
    this.role = role;
  }

  public Integer getSetTopOpt() {
    return setTopOpt;
  }

  public void setSetTopOpt(Integer setTopOpt) {
    this.setTopOpt = setTopOpt;
  }

  public String getDes3GrpId() {
    if (StringUtils.isBlank(des3GrpId) && grpId != null) {
      des3GrpId = Des3Utils.encodeToDes3(grpId.toString());
    }
    if (StringUtils.isBlank(des3GrpId) && grpShowInfo.getGrpBaseInfo().getGrpId() != null) {
      des3GrpId = Des3Utils.encodeToDes3(grpShowInfo.getGrpBaseInfo().getGrpId().toString());
    }
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public String getIsIndexDiscussOpen() {
    return isIndexDiscussOpen;
  }

  public void setIsIndexDiscussOpen(String isIndexDiscussOpen) {
    this.isIndexDiscussOpen = isIndexDiscussOpen;
  }

  public String getIsIndexMemberOpen() {
    return isIndexMemberOpen;
  }

  public void setIsIndexMemberOpen(String isIndexMemberOpen) {
    this.isIndexMemberOpen = isIndexMemberOpen;
  }

  public String getIsIndexPubOpen() {
    return isIndexPubOpen;
  }

  public void setIsIndexPubOpen(String isIndexPubOpen) {
    this.isIndexPubOpen = isIndexPubOpen;
  }

  public String getIsIndexFileOpen() {
    return isIndexFileOpen;
  }

  public void setIsIndexFileOpen(String isIndexFileOpen) {
    this.isIndexFileOpen = isIndexFileOpen;
  }

  public String getGrpName() {
    return grpName;
  }

  public void setGrpName(String grpName) {
    this.grpName = grpName;
  }

  public String getGrpDescription() {
    return grpDescription;
  }

  public void setGrpDescription(String grpDescription) {
    this.grpDescription = grpDescription;
  }

  public String getGrpKeywords() {
    return grpKeywords;
  }

  public void setGrpKeywords(String grpKeywords) {
    this.grpKeywords = grpKeywords;
  }

  public String getProjectNo() {
    return projectNo;
  }

  public void setProjectNo(String projectNo) {
    this.projectNo = projectNo;
  }

  public String getOpenType() {
    return openType;
  }

  public void setOpenType(String openType) {
    this.openType = openType;
  }

  public String getDisciplines() {
    return disciplines;
  }

  public void setDisciplines(String disciplines) {
    this.disciplines = disciplines;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public Integer getFirstCategoryId() {
    return firstCategoryId;
  }

  public void setFirstCategoryId(Integer firstCategoryId) {
    this.firstCategoryId = firstCategoryId;
  }

  public Integer getSecondCategoryId() {
    return secondCategoryId;
  }

  public void setSecondCategoryId(Integer secondCategoryId) {
    this.secondCategoryId = secondCategoryId;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getIspending() {
    return ispending;
  }

  public void setIspending(String ispending) {
    this.ispending = ispending;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public List<GrpBaseinfo> getGrpBaseInfoList() {
    return grpBaseInfoList;
  }

  public void setGrpBaseInfoList(List<GrpBaseinfo> grpBaseInfoList) {
    this.grpBaseInfoList = grpBaseInfoList;
  }

  public Long getfCategoryId() {
    return fCategoryId;
  }

  public void setfCategoryId(Long fCategoryId) {
    this.fCategoryId = fCategoryId;
  }

  public List<Discipline> getDisciplineList() {
    return disciplineList;
  }

  public void setDisciplineList(List<Discipline> disciplineList) {
    this.disciplineList = disciplineList;
  }

  public String getFirstDisciplinetName() {
    return firstDisciplinetName;
  }

  public void setFirstDisciplinetName(String firstDisciplinetName) {
    this.firstDisciplinetName = firstDisciplinetName;
  }

  public String getSecondDisciplinetName() {
    return secondDisciplinetName;
  }

  public void setSecondDisciplinetName(String secondDisciplinetName) {
    this.secondDisciplinetName = secondDisciplinetName;
  }

  public String getHasIvite() {
    return hasIvite;
  }

  public void setHasIvite(String hasIvite) {
    this.hasIvite = hasIvite;
  }

  public Long getGrpCount() {
    return grpCount;
  }

  public void setGrpCount(Long grpCount) {
    this.grpCount = grpCount;
  }

  public String getIsCopyBaseinfo() {
    return isCopyBaseinfo;
  }

  public void setIsCopyBaseinfo(String isCopyBaseinfo) {
    this.isCopyBaseinfo = isCopyBaseinfo;
  }

  public String getIsCopyGrpPubs() {
    return isCopyGrpPubs;
  }

  public void setIsCopyGrpPubs(String isCopyGrpPubs) {
    this.isCopyGrpPubs = isCopyGrpPubs;
  }

  public String getIsCopyGrpCourseware() {
    return isCopyGrpCourseware;
  }

  public void setIsCopyGrpCourseware(String isCopyGrpCourseware) {
    this.isCopyGrpCourseware = isCopyGrpCourseware;
  }

  public String getTargetdes3GrpId() {
    return targetdes3GrpId;
  }

  public void setTargetdes3GrpId(String targetdes3GrpId) {
    this.targetdes3GrpId = targetdes3GrpId;
  }

  public Long getTargetGrpId() {
    return targetGrpId;
  }

  public void setTargetGrpId(Long targetGrpId) {
    this.targetGrpId = targetGrpId;
  }

  public String getGroupCode() {
    return groupCode;
  }

  public void setGroupCode(String groupCode) {
    this.groupCode = groupCode;
  }

  public String getNewShortUrl() {
    return newShortUrl;
  }

  public void setNewShortUrl(String newShortUrl) {
    this.newShortUrl = newShortUrl;
  }

  public String getOldShortUrl() {
    return oldShortUrl;
  }

  public void setOldShortUrl(String oldShortUrl) {
    this.oldShortUrl = oldShortUrl;
  }

  public Map<String, Object> getResult2Map() {
    return result2Map;
  }

  public void setResult2Map(Map<String, Object> result2Map) {
    this.result2Map = result2Map;
  }

  public String getGrpAvartars() {
    return grpAvartars;
  }

  public void setGrpAvartars(String grpAvartars) {
    this.grpAvartars = grpAvartars;
  }

  public Integer getIsAll() {
    return isAll;
  }

  public void setIsAll(Integer isAll) {
    this.isAll = isAll;
  }

  public String getHasReqGrp() {
    return hasReqGrp;
  }

  public void setHasReqGrp(String hasReqGrp) {
    this.hasReqGrp = hasReqGrp;
  }

  public String getIsCurwareFileShow() {
    return isCurwareFileShow;
  }

  public void setIsCurwareFileShow(String isCurwareFileShow) {
    this.isCurwareFileShow = isCurwareFileShow;
  }

  public String getIsWorkFileShow() {
    return isWorkFileShow;
  }

  public void setIsWorkFileShow(String isWorkFileShow) {
    this.isWorkFileShow = isWorkFileShow;
  }

  public String getIsPrjPubShow() {
    return isPrjPubShow;
  }

  public void setIsPrjPubShow(String isPrjPubShow) {
    this.isPrjPubShow = isPrjPubShow;
  }

  public String getIsPrjRefShow() {
    return isPrjRefShow;
  }

  public void setIsPrjRefShow(String isPrjRefShow) {
    this.isPrjRefShow = isPrjRefShow;
  }

  public Integer getBeforehand() {
    return beforehand;
  }

  public void setBeforehand(Integer beforehand) {
    this.beforehand = beforehand;
  }

  public String getIsCopyProjectGrpPubs() {
    return isCopyProjectGrpPubs;
  }

  public void setIsCopyProjectGrpPubs(String isCopyProjectGrpPubs) {
    this.isCopyProjectGrpPubs = isCopyProjectGrpPubs;
  }

  public String getIsCopyProjectGrpRefs() {
    return isCopyProjectGrpRefs;
  }

  public void setIsCopyProjectGrpRefs(String isCopyProjectGrpRefs) {
    this.isCopyProjectGrpRefs = isCopyProjectGrpRefs;
  }

  public String getIsCopyCourseGrpCourseware() {
    return isCopyCourseGrpCourseware;
  }

  public void setIsCopyCourseGrpCourseware(String isCopyCourseGrpCourseware) {
    this.isCopyCourseGrpCourseware = isCopyCourseGrpCourseware;
  }

  public String getIsCopyCourseGrpWork() {
    return isCopyCourseGrpWork;
  }

  public void setIsCopyCourseGrpWork(String isCopyCourseGrpWork) {
    this.isCopyCourseGrpWork = isCopyCourseGrpWork;
  }

  public Integer getPageValue() {
    return pageValue;
  }

  public void setPageValue(Integer pageValue) {
    this.pageValue = pageValue;
  }

  public String getDes3FirstPsnId() {
    return des3FirstPsnId;
  }

  public void setDes3FirstPsnId(String des3FirstPsnId) {
    this.des3FirstPsnId = des3FirstPsnId;
  }

  public Integer getKeywordsSize() {
    return keywordsSize;
  }

  public void setKeywordsSize(Integer keywordsSize) {
    this.keywordsSize = keywordsSize;
  }

  public String getJumpto() {
    return jumpto;
  }

  public void setJumpto(String jumpto) {
    this.jumpto = jumpto;
  }

  public String getFrompage() {
    return frompage;
  }

  public void setFrompage(String frompage) {
    this.frompage = frompage;
  }

  public GrpControl getGrpControl() {
    return grpControl;
  }

  public void setGrpControl(GrpControl grpControl) {
    this.grpControl = grpControl;
  }

  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }

  public GrpBaseinfo getGrpBaseinfo() {
    return grpBaseinfo;
  }

  public void setGrpBaseinfo(GrpBaseinfo grpBaseinfo) {
    this.grpBaseinfo = grpBaseinfo;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public boolean getSelectAll() {
    return selectAll;
  }

  public void setSelectAll(boolean selectAll) {
    this.selectAll = selectAll;
  }



  public String getSeoTitle() {
    return seoTitle;
  }

  public void setSeoTitle(String seoTitle) {
    this.seoTitle = seoTitle;
  }

  public String getIsViewGrpDetail() {
    return isViewGrpDetail;
  }

  public void setIsViewGrpDetail(String isViewGrpDetail) {
    this.isViewGrpDetail = isViewGrpDetail;
  }

  public String getViewIp() {
    return viewIp;
  }

  public void setViewIp(String viewIp) {
    this.viewIp = viewIp;
  }


}
