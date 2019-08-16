package com.smate.web.prj.form;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.project.model.PrjReport;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.prj.dto.PrjExpenditureDTO;
import com.smate.web.prj.dto.ProjectExpenAccessoryDTO;
import com.smate.web.prj.dto.ProjectExpenRecordDTO;

/**
 * 项目详情form
 * 
 * @author zzx
 *
 */
public class ProjectDetailsForm implements Serializable {
  private static final long serialVersionUID = 1L;
  private boolean isVIP;
  private Long psnId;
  private String des3PsnId;
  private String des3PrjPsnId;
  private Long prjId;// 项目id
  private String des3PrjId;// 加密项目id
  private String title;// 项目标题
  private String authorNames;// 项目作者列表
  private String briefDesc;// 来源
  private String agencyName;// 资助机构名称
  private String schemeName; // 资助机构类别
  private String insName;// 依托单位名称

  private Integer awardCount;// 赞统计数
  private Integer commentCount;// 评论统计数
  private Integer shareCount;// 分享统计数
  private Integer readCount;// 阅读统计数
  private boolean isAward;// 是否已赞过

  private boolean isOwn;// 是否是自己的项目
  private boolean isFriend;// 项目拥有人和自己是否是好友
  private String ownerName;// 项目拥有人显示姓名
  private String ownerAvatars;// 项目拥有人显示头像
  private String ownerPosition;// 项目拥有人职称
  private String ownerInsName;// 项目拥有人机构信息
  private String ownerDepartment;// 项目拥有人部门信息
  private String ownerPsnIndexUrl;// 人员短地址

  private String visitAvatars;// 访问者头像

  private String keywords; // 项目关键词
  private String prjAbstract; // 项目摘要
  private String prjExternalNo; // 项目批准号 资助机构
  private String prjInternalNo; // 项目批准号本机构

  private String ownerMessage; // 拥有人的机构 部门 职称信息

  private Integer hIndex = 0;// H指数
  private String currentLocale;// 当前系统语言

  private String prjAmt;// 资金总数
  private String prjDate;// 项目时间

  private String comment;

  private Page page = new Page();
  private String seoTitle;// 页面的标题seo用
  private String des3GrpId; // 加密的群组ID

  /**
   * 项目经费列表
   */
  private List<PrjExpenditureDTO> prjExpens;
  private String schemeTotal; // 资助总金额
  private String allocatedTotal; // 已拨总金额
  private String usedTotal; // 已用总金额
  private String advanceTotal; // 预支总金额
  private String availableTotal; // 可用总金额

  private Date expenDate; // 支出日期
  private Long expenId; // 经费表的主键id
  private Float expenAmount; // 支出金额
  private String remark; // 备注信息
  private String des3fileids;// 项目经费附件id(可以存多个)

  private Long expenRecordId;// 经费记录id

  private List<ProjectExpenAccessoryDTO> accessorys;// 项目经费附件
  private List<ProjectExpenRecordDTO> expenRecords;// 经费支出列表

  private List<PrjReport> prjRepostList;// 项目报告列表
  private Long reportCount;
  private String reportType;
  private List<Integer> reportTypeList;
  private Long reportId;
  private String des3FileId;

  private List<PrjPubInfo> prjPubList;// 项目成果列表
  private String pubType = "";// 成果类型
  private String searchKey = "";
  private Long pubCount;

  private List<PrjCommentInfo> commentList; // 评论列表
  private String fulltextFileId;// 全文文件Id
  private Map<String, Object> resultMap;// 响应数据

  /**
   * 项目详情项目成果列表-成果匹配-查看全部 1:是 0:否
   */
  public Integer isAll = 0;
  public Long pubId;
  private Integer confirmResult;// 认领结果

  private String module;// 返回到哪个模块 "pub":成果模块 "report":报告模块 "exp":经费模块
  private String des3AgencyId;

  public String getPrjAmt() {
    return prjAmt;
  }

  public void setPrjAmt(String prjAmt) {
    this.prjAmt = prjAmt;
  }

  public String getPrjDate() {
    return prjDate;
  }

  public void setPrjDate(String prjDate) {
    this.prjDate = prjDate;
  }

  public boolean getIsOwn() {
    return isOwn;
  }

  public void setIsOwn(boolean isOwn) {
    this.isOwn = isOwn;
  }

  public boolean getIsFriend() {
    return isFriend;
  }

  public void setIsFriend(boolean isFriend) {
    this.isFriend = isFriend;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }

  public String getOwnerAvatars() {
    return ownerAvatars;
  }

  public void setOwnerAvatars(String ownerAvatars) {
    this.ownerAvatars = ownerAvatars;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    if (StringUtils.isBlank(des3PsnId) && psnId != null) {
      des3PsnId = Des3Utils.encodeToDes3(psnId.toString());
    }
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthorNames() {
    return authorNames;
  }

  public void setAuthorNames(String authorNames) {
    this.authorNames = authorNames;
  }

  public String getBriefDesc() {
    return briefDesc;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  public String getFulltextFileId() {
    return fulltextFileId;
  }

  public void setFulltextFileId(String fulltextFileId) {
    this.fulltextFileId = fulltextFileId;
  }

  public Map<String, Object> getResultMap() {
    return resultMap;
  }

  public void setResultMap(Map<String, Object> resultMap) {
    this.resultMap = resultMap;
  }

  public Long getPrjId() {
    if (prjId == null && StringUtils.isNotBlank(des3PrjId)) {
      String prjIdStr = Des3Utils.decodeFromDes3(des3PrjId);
      if (prjIdStr != null) {// 防止解密失败,抛出异常，返回null,导致下面的强转操作出错
        prjId = Long.parseLong(Des3Utils.decodeFromDes3(des3PrjId));
      }
    }
    return prjId;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public String getDes3PrjId() {
    if (StringUtils.isBlank(des3PrjId) && prjId != null) {
      des3PrjId = Des3Utils.encodeToDes3(prjId.toString());
    }
    return des3PrjId;
  }

  public void setDes3PrjId(String des3PrjId) {
    this.des3PrjId = des3PrjId;
  }

  public String getAgencyName() {
    return agencyName;
  }

  public void setAgencyName(String agencyName) {
    this.agencyName = agencyName;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getCurrentLocale() {
    return currentLocale;
  }

  public void setCurrentLocale(String currentLocale) {
    this.currentLocale = currentLocale;
  }

  public Integer gethIndex() {
    return hIndex;
  }

  public void sethIndex(Integer hIndex) {
    this.hIndex = hIndex;
  }

  public String getOwnerPsnIndexUrl() {
    return ownerPsnIndexUrl;
  }

  public void setOwnerPsnIndexUrl(String ownerPsnIndexUrl) {
    this.ownerPsnIndexUrl = ownerPsnIndexUrl;
  }

  public Integer getShareCount() {
    return shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

  public Integer getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }

  public Integer getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(Integer awardCount) {
    this.awardCount = awardCount;
  }

  public boolean getIsAward() {
    return isAward;
  }

  public void setIsAward(boolean isAward) {
    this.isAward = isAward;
  }

  public String getOwnerPosition() {
    return ownerPosition;
  }

  public void setOwnerPosition(String ownerPosition) {
    this.ownerPosition = ownerPosition;
  }

  public String getOwnerInsName() {
    return ownerInsName;
  }

  public String getSchemeTotal() {
    return schemeTotal;
  }

  public void setSchemeTotal(String schemeTotal) {
    this.schemeTotal = schemeTotal;
  }

  public String getAllocatedTotal() {
    return allocatedTotal;
  }

  public void setAllocatedTotal(String allocatedTotal) {
    this.allocatedTotal = allocatedTotal;
  }

  public String getUsedTotal() {
    return usedTotal;
  }

  public void setUsedTotal(String usedTotal) {
    this.usedTotal = usedTotal;
  }

  public String getAdvanceTotal() {
    return advanceTotal;
  }

  public void setAdvanceTotal(String advanceTotal) {
    this.advanceTotal = advanceTotal;
  }

  public String getAvailableTotal() {
    return availableTotal;
  }

  public void setAvailableTotal(String availableTotal) {
    this.availableTotal = availableTotal;
  }

  public void setOwnerInsName(String ownerInsName) {
    this.ownerInsName = ownerInsName;
  }

  public String getOwnerDepartment() {
    return ownerDepartment;
  }

  public void setOwnerDepartment(String ownerDepartment) {
    this.ownerDepartment = ownerDepartment;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public List<PrjCommentInfo> getCommentList() {
    return commentList;
  }

  public void setCommentList(List<PrjCommentInfo> commentList) {
    this.commentList = commentList;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public String getOwnerMessage() {
    return ownerMessage;
  }

  public void setOwnerMessage(String ownerMessage) {
    this.ownerMessage = ownerMessage;
  }

  public String getPrjAbstract() {
    return prjAbstract;
  }

  public void setPrjAbstract(String prjAbstract) {
    this.prjAbstract = prjAbstract;
  }

  public String getPrjExternalNo() {
    return prjExternalNo;
  }

  public void setPrjExternalNo(String prjExternalNo) {
    this.prjExternalNo = prjExternalNo;
  }

  public String getPrjInternalNo() {
    return prjInternalNo;
  }

  public void setPrjInternalNo(String prjInternalNo) {
    this.prjInternalNo = prjInternalNo;
  }

  public String getVisitAvatars() {
    return visitAvatars;
  }

  public void setVisitAvatars(String visitAvatars) {
    this.visitAvatars = visitAvatars;
  }

  public String getDes3PrjPsnId() {
    return des3PrjPsnId;
  }

  public void setDes3PrjPsnId(String des3PrjPsnId) {
    this.des3PrjPsnId = des3PrjPsnId;
  }

  public String getSeoTitle() {
    return seoTitle;
  }

  public void setSeoTitle(String seoTitle) {
    this.seoTitle = seoTitle;
  }

  public String getDes3GrpId() {
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public Integer getReadCount() {
    return readCount;
  }

  public void setReadCount(Integer readCount) {
    this.readCount = readCount;
  }

  public String getSchemeName() {
    return schemeName;
  }

  public void setSchemeName(String schemeName) {
    this.schemeName = schemeName;
  }

  public boolean getIsVIP() {
    return isVIP;
  }

  public void setIsVIP(boolean isVIP) {
    this.isVIP = isVIP;
  }

  public List<PrjExpenditureDTO> getPrjExpens() {
    return prjExpens;
  }

  public void setPrjExpens(List<PrjExpenditureDTO> prjExpens) {
    this.prjExpens = prjExpens;
  }

  public List<PrjReport> getPrjRepostList() {
    return prjRepostList;
  }

  public void setPrjRepostList(List<PrjReport> prjRepostList) {
    this.prjRepostList = prjRepostList;
  }

  public Long getReportCount() {
    return reportCount;
  }

  public void setReportCount(Long reportCount) {
    this.reportCount = reportCount;
  }

  public String getReportType() {
    return reportType;
  }

  public void setReportType(String reportType) {
    this.reportType = reportType;
  }

  public List<Integer> getReportTypeList() {
    return reportTypeList;
  }

  public void setReportTypeList(List<Integer> reportTypeList) {
    this.reportTypeList = reportTypeList;
  }

  public Long getReportId() {
    return reportId;
  }

  public void setReportId(Long reportId) {
    this.reportId = reportId;
  }

  public String getDes3FileId() {
    return des3FileId;
  }

  public void setDes3FileId(String des3FileId) {
    this.des3FileId = des3FileId;
  }

  public Date getExpenDate() {
    return expenDate;
  }

  public void setExpenDate(Date expenDate) {
    this.expenDate = expenDate;
  }

  public Long getExpenId() {
    return expenId;
  }

  public void setExpenId(Long expenId) {
    this.expenId = expenId;
  }

  public Float getExpenAmount() {
    return expenAmount;
  }

  public void setExpenAmount(Float expenAmount) {
    this.expenAmount = expenAmount;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getDes3fileids() {
    return des3fileids;
  }

  public void setDes3fileids(String des3fileids) {
    this.des3fileids = des3fileids;
  }

  public List<ProjectExpenAccessoryDTO> getAccessorys() {
    return accessorys;
  }

  public void setAccessorys(List<ProjectExpenAccessoryDTO> accessorys) {
    this.accessorys = accessorys;
  }


  public Long getExpenRecordId() {
    return expenRecordId;
  }

  public void setExpenRecordId(Long expenRecordId) {
    this.expenRecordId = expenRecordId;
  }

  public List<PrjPubInfo> getPrjPubList() {
    return prjPubList;
  }

  public void setPrjPubList(List<PrjPubInfo> prjPubList) {
    this.prjPubList = prjPubList;
  }

  public String getPubType() {
    return pubType;
  }

  public void setPubType(String pubType) {
    this.pubType = pubType;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public Long getPubCount() {
    return pubCount;
  }

  public void setPubCount(Long pubCount) {
    this.pubCount = pubCount;
  }

  public List<ProjectExpenRecordDTO> getExpenRecords() {
    return expenRecords;
  }

  public void setExpenRecords(List<ProjectExpenRecordDTO> expenRecords) {
    this.expenRecords = expenRecords;
  }

  public Integer getIsAll() {
    return isAll;
  }

  public void setIsAll(Integer isAll) {
    this.isAll = isAll;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getConfirmResult() {
    return confirmResult;
  }

  public void setConfirmResult(Integer confirmResult) {
    this.confirmResult = confirmResult;
  }

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public String getDes3AgencyId() {
    return des3AgencyId;
  }

  public void setDes3AgencyId(String des3AgencyId) {
    this.des3AgencyId = des3AgencyId;
  }

}
